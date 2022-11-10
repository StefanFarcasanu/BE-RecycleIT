package com.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableConfigurationProperties
@Getter
@Setter
public class GeneralConfig {
//    @Value("${validator.url")
//    private String validatorURL;
//
//    @Value("${validator.auth.username}")
//    private String validatorAuthUsername;
//
//    @Value("${validator.auth.password}")
//    private String validatorAuthPassword;
//
//    @Value("${bootstrap}")
//    private boolean bootstrap;

    @Value("${jwt.public.key}")
    private RSAPublicKey key;

    @Value("${jwt.private.key}")
    private RSAPrivateKey priv;

}
