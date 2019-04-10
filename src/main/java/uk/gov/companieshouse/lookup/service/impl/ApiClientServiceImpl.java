package uk.gov.companieshouse.lookup.service.impl;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.lookup.service.ApiClientService;
import uk.gov.companieshouse.sdk.manager.ApiClientManager;

@Service
public class ApiClientServiceImpl implements ApiClientService {

    @Override
    public ApiClient getApiClient() {
        return ApiClientManager.getSDK();
    }
}

