package uk.gov.companieshouse.lookup.service;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.lookup.exception.ServiceException;

@Service
public interface CompanyLookupService {

    CompanyProfileApi getCompanyProfile(String companyNumber) throws ServiceException;


}
