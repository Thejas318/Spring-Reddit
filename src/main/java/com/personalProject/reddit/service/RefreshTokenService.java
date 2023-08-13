package com.personalProject.reddit.service;

import com.personalProject.reddit.Repository.RefreshTokenRepository;
import com.personalProject.reddit.exceptionHandler.SpringRedditException;
import com.personalProject.reddit.model.RefreshToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    public void validateRefreshToken(String token){

        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid refresh token"));
    }

    public void deleteRefreshToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }
}
