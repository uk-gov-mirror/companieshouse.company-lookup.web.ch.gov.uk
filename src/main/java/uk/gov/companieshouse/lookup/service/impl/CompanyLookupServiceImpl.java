package uk.gov.companieshouse.lookup.service.impl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.api.model.company.account.CompanyAccountApi;
import uk.gov.companieshouse.api.model.company.account.LastAccountsApi;
import uk.gov.companieshouse.lookup.exception.ServiceException;
import uk.gov.companieshouse.lookup.model.CompanyDetail;
import uk.gov.companieshouse.lookup.service.ApiClientService;
import uk.gov.companieshouse.lookup.service.CompanyLookupService;

@Service
public class CompanyLookupServiceImpl implements CompanyLookupService {

    private static final UriTemplate GET_COMPANY_URI =
        new UriTemplate("/company/{companyNumber}");

    @Autowired
    private ApiClientService apiClientService;

    @Override
    public CompanyDetail getCompanyProfile(String companyNumber)
        throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();
        String uri = GET_COMPANY_URI.expand(companyNumber).toString();

        try {
            return mapCompany(apiClient.company().get(uri).execute());
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

    private CompanyDetail mapCompany(CompanyProfileApi companyProfileApi) {

        CompanyDetail companyDetail = new CompanyDetail();
        RegisteredOfficeAddressApi registeredOfficeAddress = companyProfileApi
            .getRegisteredOfficeAddress();

        companyDetail.setCompanyName(companyProfileApi.getCompanyName());
        companyDetail.setCompanyNumber(companyProfileApi.getCompanyNumber());

        if (registeredOfficeAddress != null) {

            companyDetail.setRegisteredOfficeAddress(
                ((registeredOfficeAddress.getAddressLine1() == null) ? "": registeredOfficeAddress
                    .getAddressLine1()) +
                    ((registeredOfficeAddress.getAddressLine2() == null) ? "" :", "
                        + registeredOfficeAddress.getAddressLine2() ) +
                    ((registeredOfficeAddress.getPostalCode() == null) ? "" : ", "
                        + registeredOfficeAddress.getPostalCode()));
        }

        companyDetail
            .setAccountsNextMadeUpTo(Optional.of(companyProfileApi)
                .map(CompanyProfileApi::getAccounts)
                .map(CompanyAccountApi::getNextMadeUpTo)
                .orElse(null));

        companyDetail.setLastAccountsNextMadeUpTo(Optional.of(companyProfileApi)
            .map(CompanyProfileApi::getAccounts)
            .map(CompanyAccountApi::getLastAccounts)
            .map(LastAccountsApi::getMadeUpTo)
            .orElse(null));

        return companyDetail;

    }
}