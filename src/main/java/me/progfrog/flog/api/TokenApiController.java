package me.progfrog.flog.api;

import lombok.RequiredArgsConstructor;
import me.progfrog.flog.dto.accesstoken.AccessTokenReqCreateDto;
import me.progfrog.flog.dto.accesstoken.AccessTokenResCreateDto;
import me.progfrog.flog.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenApiController {

    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<AccessTokenResCreateDto> createNewAccessToken(@RequestBody AccessTokenReqCreateDto reqCreateDto) {
        String newAccessToken = tokenService.createNewAccessToken(reqCreateDto.refreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AccessTokenResCreateDto(newAccessToken));
    }
}
