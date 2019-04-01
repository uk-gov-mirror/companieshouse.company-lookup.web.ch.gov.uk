package uk.gov.companieshouse.lookup.model;

import java.time.LocalDate;

public class CompanyConfirmation {

    private String companyName;

    private String companyNumber;

    private String registeredOfficeAddress;

    private LocalDate accountsNextMadeUpTo;

    private LocalDate lastAccountsNextMadeUpTo;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getRegisteredOfficeAddress() {
        return registeredOfficeAddress;
    }

    public void setRegisteredOfficeAddress(String registeredOfficeAddress) {
        this.registeredOfficeAddress = registeredOfficeAddress;
    }

    public LocalDate getAccountsNextMadeUpTo() {
        return accountsNextMadeUpTo;
    }

    public void setAccountsNextMadeUpTo(LocalDate accountsNextMadeUpTo) {
        this.accountsNextMadeUpTo = accountsNextMadeUpTo;
    }

    public LocalDate getLastAccountsNextMadeUpTo() {
        return lastAccountsNextMadeUpTo;
    }

    public void setLastAccountsNextMadeUpTo(LocalDate lastAccountsNextMadeUpTo) {
        this.lastAccountsNextMadeUpTo = lastAccountsNextMadeUpTo;
    }
}