package uk.gov.companieshouse.lookup.model;

import uk.gov.companieshouse.lookup.validation.CompanyNumberValidation;

public class CompanyLookup {

    @CompanyNumberValidation
    private String companyNumber;

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }
}
