package uk.gov.companieshouse.lookup.service;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.lookup.exception.ServiceException;
import uk.gov.companieshouse.lookup.model.Company;

@Service
public interface CompanyLookupService {

    Company getCompanyProfile(String companyNumber)
        throws ServiceException;

}
