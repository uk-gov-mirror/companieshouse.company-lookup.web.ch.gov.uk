package uk.gov.companieshouse.lookup.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.lookup.exception.ServiceException;
import uk.gov.companieshouse.lookup.model.Company;
import uk.gov.companieshouse.lookup.service.ApiClientService;
import uk.gov.companieshouse.lookup.service.CompanyLookupService;

@Service
public class CompanyLookupServiceImpl implements CompanyLookupService {

    private static final UriTemplate GET_COMPANY_URI =
        new UriTemplate("/company/{companyNumber}");

    @Autowired
    private ApiClientService apiClientService;

    @Override
    public Company getCompanyProfile(String companyNumber)
        throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();
        String uri = GET_COMPANY_URI.expand(companyNumber).toString();

        try {
            return mapCompany(apiClient.company().get(uri).execute().getData());
        } catch (URIValidationException e) {
            throw new ServiceException("Invalid URI for company resource", e);
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                return null;
            }
            throw new ServiceException("Failed to retrieve company profile for " + companyNumber,
                e);
        }
    }

    private Company mapCompany(CompanyProfileApi companyProfileApi) {

        Company company = new Company();
        company.setCompanyNumber(companyProfileApi.getCompanyNumber());

        return company;
    }
}