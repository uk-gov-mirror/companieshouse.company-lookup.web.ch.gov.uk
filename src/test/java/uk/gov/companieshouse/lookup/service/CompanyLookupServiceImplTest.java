package uk.gov.companieshouse.lookup.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.company.CompanyResourceHandler;
import uk.gov.companieshouse.api.handler.company.request.CompanyGet;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.lookup.exception.ServiceException;
import uk.gov.companieshouse.lookup.model.CompanyDetail;
import uk.gov.companieshouse.lookup.service.impl.ApiClientServiceImpl;
import uk.gov.companieshouse.lookup.service.impl.CompanyLookupServiceImpl;

@ExtendWith(MockitoExtension.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class CompanyLookupServiceImplTest {

        @Mock
        private ApiClient apiClient;

        @Mock
        private ApiClientServiceImpl apiClientService;

        @Mock
        private CompanyResourceHandler companyResourceHandler;

        @Mock
        private CompanyGet companyGet;

        @InjectMocks
        private CompanyLookupService companyService = new CompanyLookupServiceImpl();

        private static final String COMPANY_NUMBER = "companyNumber";

        private static final String COMPANY_URI = "/company/" + COMPANY_NUMBER;

        @BeforeEach
        private void init() {

            when(apiClientService.getApiClient()).thenReturn(apiClient);
            when(apiClient.company()).thenReturn(companyResourceHandler);
            when(companyResourceHandler.get(COMPANY_URI)).thenReturn(companyGet);
        }

        @Test
        @DisplayName("Get Company Profile - Success Path")
        private void getCompanyProfileSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

            when(companyGet.execute()).thenReturn(new CompanyProfileApi());

            CompanyDetail companyDetail = companyService.getCompanyProfile(COMPANY_NUMBER);

            assertNotNull(companyDetail);
        }

        @Test
        @DisplayName("Get Company Profile - Throws ApiErrorResponseException")
        private void getBalanceSheetThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException {

            when(companyGet.execute()).thenThrow(ApiErrorResponseException.class);

            assertThrows(ServiceException.class, () ->
                companyService.getCompanyProfile(COMPANY_NUMBER));
        }

        @Test
        @DisplayName("Get Company Profile - Throws URIValidationException")
        private void getBalanceSheetThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException {

            when(companyGet.execute()).thenThrow(URIValidationException.class);

            assertThrows(ServiceException.class, () ->
                companyService.getCompanyProfile(COMPANY_NUMBER));
        }
    }
