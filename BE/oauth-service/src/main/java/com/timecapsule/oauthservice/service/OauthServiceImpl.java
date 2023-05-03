package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.db.entity.jwt.LogoutAccessToken;
import com.timecapsule.oauthservice.db.entity.jwt.LogoutRefreshToken;
import com.timecapsule.oauthservice.db.repository.jwt.TokenRepository;
import com.timecapsule.oauthservice.dto.AuthResponseDto;
import com.timecapsule.oauthservice.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OauthServiceImpl implements OauthService{
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;


    @Override
    // 로그아웃 요청된 Access Token과 Refresh Token을 블랙리스트로 저장
    // 이때 블랙리스트로 등록하는 Access Token과 Refresh Token의 유효시간을 요청시 받은 토큰의 남은 유효시간만큼 부여
    public void logout(String accessToken, String refreshToken) {
        saveLogoutAccessToken(accessToken);
        saveLogoutRefreshToken(refreshToken);
    }

    private void saveLogoutAccessToken(String accessToken) {
        String removedTypeAccessToken = accessToken.substring(7);
        LogoutAccessToken logoutAccessToken = LogoutAccessToken.of(removedTypeAccessToken,
                tokenProvider.getRemainingTimeFromToken(removedTypeAccessToken));
        tokenRepository.saveLogoutAccessToken(logoutAccessToken);
    }

    private void saveLogoutRefreshToken(String refreshToken) {
        String removedTypeRefreshToken = refreshToken.substring(7);
        LogoutRefreshToken logoutRefreshToken = LogoutRefreshToken.of(removedTypeRefreshToken,
                tokenProvider.getRemainingTimeFromToken(removedTypeRefreshToken));
        tokenRepository.saveLogoutRefreshToken(logoutRefreshToken);
    }

    @Override
    public AuthResponseDto reissue(String refreshToken) {
        saveLogoutRefreshToken(refreshToken);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String newAccessToken = tokenProvider.createAccessToken(authentication);
        String newRefreshToken = tokenProvider.createRefreshToken(authentication);
        return AuthResponseDto.of(newAccessToken, newRefreshToken);
    }
}
