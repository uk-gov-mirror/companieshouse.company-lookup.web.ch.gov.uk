package uk.gov.companieshouse.lookup.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
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
import uk.gov.companieshouse.lookup.service.ApiClientService;
import uk.gov.companieshouse.lookup.exception.ServiceException;
import uk.gov.companieshouse.lookup.model.CompanyConfirmation;
import uk.gov.companieshouse.lookup.model.CompanyLookup;
import uk.gov.companieshouse.lookup.service.CompanyLookupService;
import uk.gov.companieshouse.lookup.validation.ValidationError;

@Service
public class CompanyLookupServiceImpl implements CompanyLookupService {

    private static final UriTemplate GET_COMPANY_URI =
        new UriTemplate("/company/{companyNumber}");

    @Autowired
    private ApiClientService apiClientService;

    @Override
    public CompanyConfirmation getCompanyProfile(String companyNumber)
        throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();
        String uri = GET_COMPANY_URI.expand(companyNumber).toString();

        try {
            return mapCompany(apiClient.company().get(uri).execute());
        } catch (URIValidationException e) {
            throw new ServiceException("Exception building URI", e);
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                return null;
            }
            throw new ServiceException("API Error response", e);
        }
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
        CompanyAccountApi companyAccountApi = companyProfileApi.getAccounts();

        companyConfirmation.setCompanyName(companyProfileApi.getCompanyName());
        companyConfirmation.setCompanyNumber(companyProfileApi.getCompanyNumber());
        if (registeredOfficeAddress != null) {
            companyConfirmation.setRegisteredOfficeAddress(
                registeredOfficeAddress.getAddressLine1() + ", " + registeredOfficeAddress
                    .getAddressLine2() + ", " + registeredOfficeAddress.getPostalCode());
        }
        if (companyAccountApi != null) {
            companyConfirmation
                .setAccountsNextMadeUpTo(companyAccountApi.getNextMadeUpTo());
            companyConfirmation.setLastAccountsNextMadeUpTo(
                companyProfileApi.getAccounts().getLastAccounts().getMadeUpTo());
        }
        return companyConfirmation;
    }
}