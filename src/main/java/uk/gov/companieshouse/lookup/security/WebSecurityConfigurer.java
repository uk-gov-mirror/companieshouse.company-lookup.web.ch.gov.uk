package uk.gov.companieshouse.lookup.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import uk.gov.companieshouse.session.handler.SessionHandler;

@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Configuration
    @Order(1)
    public static class CompanyAccountsSecurityFilterConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) {

            http.addFilterBefore(new SessionHandler(), BasicAuthenticationFilter.class);
        }
    }

}
