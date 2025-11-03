package uk.gov.companieshouse.lookup.internationalisation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import uk.gov.companieshouse.session.Session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

@Component
public class ChSessionLocaleResolver implements LocaleResolver {
    private static final String EXTRA_DATA_SESSION_KEY = "extra_data";
    private static final String LANG_SESSION_KEY = "lang";
    private SessionProvider sessionProvider;
    private Locale defaultLocale = Locale.getDefault();

    @Autowired
    public ChSessionLocaleResolver(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        Locale locale = getLocaleFromSession();
        httpServletRequest.setAttribute("onWelshJourney", locale != null);
        if (locale == null) {
            locale = defaultLocale;
        }

        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
        setLocaleInSession(locale);
    }

    private Locale getLocaleFromSession() {
        Map<String, Object> sessionData = sessionProvider.getSessionDataFromContext();
        Object extraDataObj = sessionData.get(EXTRA_DATA_SESSION_KEY);

        if (extraDataObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, String> extraData = (Map<String, String>) extraDataObj;
            String languageTag = extraData.get(LANG_SESSION_KEY);
            if (StringUtils.isEmpty(languageTag)) {
                return null;
            }

            return Locale.forLanguageTag(languageTag);
        }

        return null;
    }

    private void setLocaleInSession(Locale locale) {
        Session session = sessionProvider.getSessionFromContext();
        Map<String, Object> sessionData = session.getData();
        Object extraDataObj = sessionData.get(EXTRA_DATA_SESSION_KEY);

        if (extraDataObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, String> extraData = (Map<String, String>) extraDataObj;
            extraData.put(LANG_SESSION_KEY, locale.toLanguageTag());
            session.store();
        }
    }

    public void setSessionProvider(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }
}
