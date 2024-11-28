package uk.gov.companieshouse.lookup.internationalisation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.session.Session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChSessionLocaleResolverTest {

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private Session session;

    @Mock
    private SessionProvider sessionProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    private Map<String, Object> sessionData;
    private Map<String, Object> extraData;

    @InjectMocks
    private ChSessionLocaleResolver localeResolver;


    @BeforeEach
    public void setUp() {
        localeResolver.setSessionProvider(sessionProvider);
        ReflectionTestUtils.setField(localeResolver, "defaultLocale", Locale.ENGLISH);
    }

    private void initSessionDataMaps(String lang) {
        sessionData = new HashMap<>();
        extraData = new HashMap<>();
        sessionData.put("extra_data", extraData);
        if (lang != null) {
            extraData.put("lang", lang);
        }
    }

    private void setupSessionData(String lang) {
        initSessionDataMaps(lang);
        when(sessionProvider.getSessionDataFromContext()).thenReturn(sessionData);
    }

    private void setupSession() {
        initSessionDataMaps(null);
        when(sessionProvider.getSessionFromContext()).thenReturn(session);
        when(session.getData()).thenReturn(sessionData);
    }

    @Test
    @DisplayName("Resolves to the default locale if no locale is in the session")
    void testResolveDefaultLocale() {
        setupSessionData(null);
        Locale locale = localeResolver.resolveLocale(request);

        assertEquals(Locale.ENGLISH, locale);
    }

    @Test
    @DisplayName("Resolves to the locale in the session when present")
    void testResolveLocale() {
        setupSessionData("cy");
        Locale locale = localeResolver.resolveLocale(request);

        Locale welshLocale = new Locale("cy");
        assertEquals(welshLocale, locale);
    }

    @Test
    @DisplayName("Locale gets set in session when resolver sets locale")
    void testSetLocale() {
        setupSession();
        Locale welshLocale = new Locale("cy");

        localeResolver.setLocale(request, response, welshLocale);

        String languageTagInSession = (String) extraData.get("lang");

        assertEquals(welshLocale.toLanguageTag(), languageTagInSession);
        verify(session).store();
    }
}