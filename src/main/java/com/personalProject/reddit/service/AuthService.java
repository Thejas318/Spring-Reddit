package com.personalProject.reddit.service;

import com.personalProject.reddit.Repository.UserRepository;
import com.personalProject.reddit.Repository.VerificationTokenRepository;
import com.personalProject.reddit.dto.AuthenticationResponse;
import com.personalProject.reddit.dto.LoginRequest;
import com.personalProject.reddit.dto.RefreshTokenRequest;
import com.personalProject.reddit.dto.RegisterRequest;
import com.personalProject.reddit.exceptionHandler.SpringRedditException;
import com.personalProject.reddit.exceptionHandler.UserNotFoundException;
import com.personalProject.reddit.model.NotificationEmail;
import com.personalProject.reddit.model.RefreshToken;
import com.personalProject.reddit.model.User;
import com.personalProject.reddit.model.VerificationToken;
import com.personalProject.reddit.security.JwtProvider;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signUp(RegisterRequest registerRequest){

        User newUser = new User();
        newUser.setUserName(registerRequest.getUserName());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassWord()));                //Always encode the password before storing in db
        newUser.setCreated(Instant.now());
        newUser.setEnabled(false);

        userRepository.save(newUser);                                                  //Saving the new user in the db

        String userToken = generateVerificationToken(newUser);                          //A token will be generated once the user data is saved in db. This token will be sent in email ti user to verify.
        NotificationEmail notificationEmail = prepareNotificationEmail(newUser, userToken);
        mailService.sendMail(notificationEmail);

    }

    private String generateVerificationToken(User newUser) {

        String token = UUID.randomUUID().toString();                               //Generating a random verification token

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(newUser);
        verificationToken.setToken(token);

        verificationTokenRepository.save(verificationToken);                //Saving the verification token with user details for later verification.

        return token;
    }

    //Prepares thr Email notification to be sent to the user
    private NotificationEmail prepareNotificationEmail(User user, String token) {
        NotificationEmail notificationEmail = new NotificationEmail("Please Activate your Account",
                user.getEmail(),
                "Thank you for signing up to spring Reddit: " +
                        "Please click on the below link to activate your account: " +
                        "http://localhost:8080/api/auth/accountVerification/" + token);
        return notificationEmail;
    }

    //We query the db for user with the token received, if found we call the findUserAndEnable method to enable them
    public void verifyAccount(String token) {

        Optional<VerificationToken> userBasedOnToken = verificationTokenRepository.findByToken(token);
        userBasedOnToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));

        findUserAndEnable(userBasedOnToken.get());

    }

    //We will find the user by token, if found we enable them and save the user enable flag in db as enabled. Now they will be able to login
    @Transactional
    private void findUserAndEnable(VerificationToken verificationToken) {
        String userName = verificationToken.getUser().getUserName();

        User user = userRepository.findByUserName(userName).orElseThrow(() -> new SpringRedditException("User not found with name: " + userName));
        user.setEnabled(true);

        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUserName(),
                loginRequest.getPassWord()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String jwtToken = jwtProvider.generateToken(authenticate);

        return AuthenticationResponse.builder()
                .authenticationToken(jwtToken)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getExpirationTime()))
                .userName(loginRequest.getUserName())
                .build();
    }

    @Transactional
    public User getCurrentUser(){

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findByUserName(principal.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User name not found - " + principal.getUsername()));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUserName());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getExpirationTime()))
                .userName(refreshTokenRequest.getUserName())
                .build();
    }
}
