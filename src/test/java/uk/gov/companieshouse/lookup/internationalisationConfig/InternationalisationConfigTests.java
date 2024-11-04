package uk.gov.companieshouse.lookup.internationalisationConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import uk.gov.companieshouse.lookup.internationalisation.ChSessionLocaleResolver;
import uk.gov.companieshouse.lookup.internationalisation.InternationalisationConfig;
import uk.gov.companieshouse.lookup.internationalisation.SessionProvider;

@ExtendWith(MockitoExtension.class)
class InternationalisationConfigTests {

    @InjectMocks
    private InternationalisationConfig config;

    @Mock
    private SessionProvider sessionProvider;

    @Test
    @DisplayName("Test MessageSource bean configuration")
    void testMessageSource() throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
        MessageSource messageSource = config.messageSource();
        assertThat(messageSource).isInstanceOf(ResourceBundleMessageSource.class);

        ResourceBundleMessageSource rbms = (ResourceBundleMessageSource) messageSource;
        assertThat(rbms.getBasenameSet()).contains("locales/messages", "locales/common-messages");

        Method getDefaultEncodingMethod = ResourceBundleMessageSource.class
                .getSuperclass()
                .getDeclaredMethod("getDefaultEncoding");

        getDefaultEncodingMethod.setAccessible(true);

        String defaultEncoding = (String) getDefaultEncodingMethod.invoke(rbms);
        
        assertThat(defaultEncoding).isEqualTo("UTF-8");
    }

    @Test
    @DisplayName("Test LocaleResolver bean configuration")
    void testLocaleResolver() {
        ChSessionLocaleResolver chSessionLocaleResolver = spy(ChSessionLocaleResolver.class);
        chSessionLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        LocaleResolver localeResolver = config.localeResolver(new ChSessionLocaleResolver(sessionProvider));
        
        assertThat(localeResolver).isInstanceOf(ChSessionLocaleResolver.class);

        Map<String, Object> sessionData = new HashMap<>();
        Map<String, String> extraData = new HashMap<>();
        sessionData.put("extra_data", extraData);
        when(sessionProvider.getSessionDataFromContext()).thenReturn(sessionData);
        ((ChSessionLocaleResolver)localeResolver).setSessionProvider(sessionProvider);

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        Locale resolvedLocale = localeResolver.resolveLocale(httpServletRequest);

        assertThat(resolvedLocale).isEqualTo(Locale.ENGLISH);
    }

    @Test
    @DisplayName("Test LocaleChangeInterceptor bean configuration")
    void testLocaleChangeInterceptor() {
        LocaleChangeInterceptor interceptor = config.localeChangeInterceptor();
        assertThat(interceptor).isInstanceOf(LocaleChangeInterceptor.class);
        assertThat(interceptor.getParamName()).isEqualTo("lang");
    }
}
