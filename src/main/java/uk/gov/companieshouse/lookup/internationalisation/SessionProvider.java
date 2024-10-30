package uk.gov.companieshouse.lookup.internationalisation;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.session.Session;
import uk.gov.companieshouse.session.handler.SessionHandler;

import java.util.Map;

@Component
public class SessionProvider {
    public Map<String, Object> getSessionDataFromContext() {
        return SessionHandler.getSessionDataFromContext();
    }

    public Session getSessionFromContext() {
        return SessionHandler.getSessionFromContext();
    }
}