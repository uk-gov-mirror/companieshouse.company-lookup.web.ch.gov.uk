package uk.gov.companieshouse.lookup.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import uk.gov.companieshouse.lookup.internationalisation.ChSessionLocaleResolver;

@Configuration
public class MessageConfig implements WebMvcConfigurer {
    @Bean("messageSource")
    public MessageSource messageSource() {
        var messageSource = new ResourceBundleMessageSource();

        // This will match the resources folder files that begin with 'messages'
        // e.g. messages_wl.properties, messages_fr.properties
        messageSource.setBasenames("locales/messages", "locales/common-messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    // The LocaleResolver interface has implementations that determine
    // the current locale based on the session, cookies,
    // the Accept-Language header, or a fixed value.
    @Bean
    public LocaleResolver localeResolver(ChSessionLocaleResolver chSessionLocaleResolver) {
        chSessionLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        return chSessionLocaleResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        var localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
