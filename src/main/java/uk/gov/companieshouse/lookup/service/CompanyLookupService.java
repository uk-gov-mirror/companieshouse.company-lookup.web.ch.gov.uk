package uk.gov.companieshouse.lookup.service;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.lookup.exception.ServiceException;
import uk.gov.companieshouse.lookup.model.CompanyDetail;

@Service
public interface CompanyLookupService {

    CompanyDetail getCompanyProfile(String companyNumber)
        throws ServiceException;

}
