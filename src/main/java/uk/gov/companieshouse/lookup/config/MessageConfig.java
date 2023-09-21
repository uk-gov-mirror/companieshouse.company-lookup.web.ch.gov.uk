package uk.gov.companieshouse.lookup.config;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MessageConfig implements WebMvcConfigurer {

    /*
    @Autowired
    private ResourceBundleMessageSource messageSource;

    public Set<String> getSupportedLanguages() {
        Set<String> languages = new HashSet<>();

        Locale[] locales = messageSource.getSupportedLocales();
        for (Locale locale : locales) {
            languages.add(locale.getLanguage());
        }

        return languages;
    }
    */


    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        //messageSource.setBasenames("language/messages");
        messageSource.setBasenames("messages");
        messageSource.setDefaultEncoding("UTF-8");

        //Set<String> languages = new HashSet<>();
        //Locale[] locales = messageSource.getSupportedLocales();
        //for (Locale locale : locales) {
        //    languages.add(locale.getLanguage());
        //}

        return messageSource;
    }

    // The LocaleResolver interface has implementations that determine
    // the current locale based on the session, cookies,
    // the Accept-Language header, or a fixed value.
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.UK);
        slr.setLocaleAttributeName("current.locale");
        slr.setTimeZoneAttributeName("current.timezone");
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

}
