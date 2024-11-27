package uk.gov.companieshouse.lookup.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import uk.gov.companieshouse.csrf.config.ChsCsrfMitigationHttpSecurityBuilder;

@EnableWebSecurity
@Configuration
public class WebSecurityConfigurer {

    /**
     * Security filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return ChsCsrfMitigationHttpSecurityBuilder.configureWebCsrfMitigations(
                        http.authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll())
                                .sessionManagement(
                                        session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.ALWAYS)))
                .build();
    }
}
