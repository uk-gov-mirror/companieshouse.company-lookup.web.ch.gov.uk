package uk.gov.companieshouse.lookup.model;

import javax.validation.constraints.Size;

public class CompanyLookup {

    @Size(min = 8)
    private String companyNumber;

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }
}
