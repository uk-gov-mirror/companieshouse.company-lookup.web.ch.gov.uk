package uk.gov.companieshouse.lookup.internationalisationConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import uk.gov.companieshouse.lookup.internationalisation.InternationalisationConfig;

public class InternationalisationConfigTests {

    private final InternationalisationConfig config = new InternationalisationConfig();

    @Test
    @DisplayName("Test MessageSource bean configuration")
    void testMessageSource() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        MessageSource messageSource = config.messageSource();
        assertThat(messageSource).isInstanceOf(ResourceBundleMessageSource.class);

        ResourceBundleMessageSource rbms = (ResourceBundleMessageSource) messageSource;
        assertThat(rbms.getBasenameSet()).contains("messages");

        Method getDefaultEncodingMethod = ResourceBundleMessageSource.class
                .getSuperclass()
                .getDeclaredMethod("getDefaultEncoding");

        getDefaultEncodingMethod.setAccessible(true);

        String defaultEncoding = (String) getDefaultEncodingMethod.invoke(rbms);
        
        assertThat(defaultEncoding).isEqualTo("UTF-8");
    }

    @Test
    @DisplayName("Test LocaleResolver bean configuration")
    void testLocaleResolver() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        LocaleResolver localeResolver = config.localeResolver();
        
        assertThat(localeResolver).isInstanceOf(SessionLocaleResolver.class);

        MockHttpServletRequest request = new MockHttpServletRequest();

        Locale resolvedLocale = localeResolver.resolveLocale(request);

        assertThat(resolvedLocale).isEqualTo(Locale.ENGLISH);
    }

    @Test
    @DisplayName("Test LocaleChangeInterceptor bean configuration")
    void testLocaleChangeInterceptor() {
        LocaleChangeInterceptor interceptor = config.localeChangeInterceptor();
        assertThat(interceptor).isNotNull();
        assertThat(interceptor).isInstanceOf(LocaleChangeInterceptor.class);
        assertThat(interceptor.getParamName()).isEqualTo("lang");
    }
}
