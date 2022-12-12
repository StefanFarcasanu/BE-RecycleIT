package com.security;

import com.domain.enums.RoleEnum;
import com.domain.service.UserDetailsService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.repo.UserRepository;
import com.utils.GeneralConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity()
public class SecurityConfiguration {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeneralConfig generalConfig;

    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager() throws  Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Bean for setting up the Spring Security filter chain
     *
     * @param http - HttpSecurity object for filters setup
     * @return - SecurityFilterChain object
     * @throws Exception - if it cannot build the security filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtEncoder()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtDecoder(), userRepository))
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/users/register").permitAll()
                .antMatchers("/users").hasRole(RoleEnum.CLIENT.name())
                //endpoints that start with /voucher
                .antMatchers(HttpMethod.GET, "/vouchers/client").hasAnyRole(RoleEnum.CLIENT.name(), RoleEnum.RETAILER.name())
                .antMatchers(HttpMethod.PUT, "/vouchers").hasRole(RoleEnum.CLIENT.name())
                .antMatchers(HttpMethod.GET, "/vouchers/total").hasRole(RoleEnum.CLIENT.name())
                .antMatchers(HttpMethod.POST, "/vouchers/create").hasRole(RoleEnum.RETAILER.name())
                .antMatchers("/vouchers/**").hasAnyRole()
                //endpoints that start with /request
                .antMatchers(HttpMethod.GET, "/requests/history").hasRole(RoleEnum.CLIENT.name())
                .antMatchers(HttpMethod.POST, "/requests").hasRole(RoleEnum.CLIENT.name())
                .antMatchers(HttpMethod.GET, "/requests/milestone").hasRole(RoleEnum.CLIENT.name())
                .antMatchers(HttpMethod.GET, "/requests/total").hasRole(RoleEnum.CLIENT.name())
                .antMatchers(HttpMethod.GET, "/requests/company").hasRole(RoleEnum.COMPANY.name())
                .antMatchers(HttpMethod.PUT, "requests/{\\d+}").hasRole(RoleEnum.COMPANY.name())
                .antMatchers("/requests/**").hasAnyRole()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                );
        return http.build();
    }

    /**
     * Bean for setting up the authentication provider
     * @return - DaoAuthenticationProvider object
     */
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    /**
     * Bean for setting up the password encoder
     * @return - PasswordEncoder object
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(13);
    }

    /**
     * Bean for setting up the JWT decoder
     * @return - JwtDecoder object
     */
    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(generalConfig.getKey()).build();
    }

    /**
     * Bean for setting up the JWT encoder
     * @return - JwtEncoder object
     */
    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(generalConfig.getKey()).privateKey(generalConfig.getPriv()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

}
