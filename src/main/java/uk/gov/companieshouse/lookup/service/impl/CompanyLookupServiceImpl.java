package uk.gov.companieshouse.lookup.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.lookup.model.CompanyConfirmation;
import uk.gov.companieshouse.lookup.model.CompanyLookup;
import uk.gov.companieshouse.lookup.service.CompanyLookupService;
import uk.gov.companieshouse.lookup.validation.ValidationError;
import uk.gov.companieshouse.sdk.manager.ApiSdkManager;

@Service
public class CompanyLookupServiceImpl implements CompanyLookupService {

    private static final UriTemplate GET_COMPANY_URI =
        new UriTemplate("/company/{companyNumber}");

    @Override
    public CompanyConfirmation getCompanyProfile(String companyNumber)
        throws ApiErrorResponseException, URIValidationException {

        ApiClient apiClient = ApiSdkManager.getSDK();
        String uri = GET_COMPANY_URI.expand(companyNumber).toString();
        CompanyProfileApi companyProfileApi = apiClient.company().get(uri).execute();

        return mapCompany(companyProfileApi);
    }

    @Override
    public List<ValidationError> validateCompanyLookup(CompanyLookup companyLookup) {

        List<ValidationError> validationErrors = new ArrayList<>();

        if (StringUtils.isBlank(companyLookup.getCompanyNumber()) || !StringUtils
            .isNumeric(companyLookup.getCompanyNumber())) {
            ValidationError error = new ValidationError();
            error.setFieldPath("companyNumber");
            error.setMessageKey("validation.number.invalid");
            validationErrors.add(error);
        }

        return validationErrors;
    }

    private CompanyConfirmation mapCompany(CompanyProfileApi companyProfileApi) {

        CompanyConfirmation companyConfirmation = new CompanyConfirmation();
        RegisteredOfficeAddressApi registeredOfficeAddress = companyProfileApi
            .getRegisteredOfficeAddress();

        companyConfirmation.setCompanyName(companyProfileApi.getCompanyName());
        companyConfirmation.setCompanyNumber(companyProfileApi.getCompanyNumber());
        companyConfirmation.setRegisteredOfficeAddress(
            registeredOfficeAddress.getAddressLine1() + ", " + registeredOfficeAddress
                .getAddressLine2() + ", " + registeredOfficeAddress.getPostalCode());
        companyConfirmation
            .setAccountsNextMadeUpTo(companyProfileApi.getAccounts().getNextMadeUpTo());
        companyConfirmation.setLastAccountsNextMadeUpTo(
            companyProfileApi.getAccounts().getLastAccounts().getMadeUpTo());
        return companyConfirmation;
    }
}