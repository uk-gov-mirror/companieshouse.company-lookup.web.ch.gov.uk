package uk.gov.companieshouse.lookup.service;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.ApiClient;

/**
 * The {@code ApiClientService} interface provides an abstraction that can be used when testing
 * {@code ApiClientManager} static methods, without imposing the use of a test framework that
 * supports mocking of static methods.
 */
@Service
public interface ApiClientService {

    ApiClient getApiClient();
}
