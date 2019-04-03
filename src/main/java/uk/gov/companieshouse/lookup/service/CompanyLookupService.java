package uk.gov.companieshouse.lookup.service;

import java.util.List;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.lookup.exception.ServiceException;
import uk.gov.companieshouse.lookup.model.CompanyConfirmation;
import uk.gov.companieshouse.lookup.model.CompanyLookup;
import uk.gov.companieshouse.lookup.validation.ValidationError;

@Service
public interface CompanyLookupService {

    CompanyConfirmation getCompanyProfile(String companyNumber)
        throws ServiceException;

    List<ValidationError> validateCompanyLookup(CompanyLookup companyLookup);

}
