package uk.gov.companieshouse.lookup.service;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.ApiClient;

@Service
public interface ApiClientService {

    ApiClient getApiClient();
}
