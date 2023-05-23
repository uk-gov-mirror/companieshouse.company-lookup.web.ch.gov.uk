package uk.gov.companieshouse.lookup.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import uk.gov.companieshouse.session.handler.SessionHandler;


@Configuration
public class WebSecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.addFilterBefore(new SessionHandler(), BasicAuthenticationFilter.class);

        http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        
        return http.build();
    }
}
