package uk.gov.companieshouse.lookup.controller;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.lookup.exception.InvalidRequestException;
import uk.gov.companieshouse.lookup.exception.ServiceException;
import uk.gov.companieshouse.lookup.model.Company;
import uk.gov.companieshouse.lookup.model.CompanyLookup;
import uk.gov.companieshouse.lookup.model.ForwardUrl;
import uk.gov.companieshouse.lookup.service.CompanyLookupService;
import uk.gov.companieshouse.lookup.validation.ValidationError;
import uk.gov.companieshouse.lookup.validation.ValidationHandler;

@Controller
@RequestMapping("/company-lookup")
public class CompanyLookupController {

    private static final String COMPANY_LOOKUP = "lookup/companyLookup";

    @Autowired
    private CompanyLookupService companyLookupService;

    @Autowired
    private ValidationHandler validationHandler;

    @GetMapping("/search")
    public String getCompanyLookup(@Valid ForwardUrl forward, BindingResult forwardResult, Model model,
        @RequestParam(name = "noCompanyOption", required = false) String noCompanyOption) throws InvalidRequestException {
        if(forwardResult.hasErrors()) {
            throw new InvalidRequestException(String.format("Invalid forward URL: [%s]", forward.getForward()));
        }
        CompanyLookup companyLookup = new CompanyLookup();
        model.addAttribute("companyLookup", companyLookup);
        model.addAttribute("noCompanyOption", noCompanyOption);
        return COMPANY_LOOKUP;
    }
    
    @GetMapping("/no-company")
    public String getCompanyLookupNoCompany(@Valid ForwardUrl forward, BindingResult forwardResult,
        Model model) throws InvalidRequestException {
        if(forwardResult.hasErrors()) {
            throw new InvalidRequestException(String.format("Invalid forward URL: [%s]", forward.getForward()));
        }
        UriTemplate forwardURI = new UriTemplate(forward.getForward());
        
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + forwardURI.expand("noCompany");
    }
    

    @PostMapping("/search")
    public String postCompanyLookup(@Valid ForwardUrl forward, BindingResult forwardResult,
        @ModelAttribute("companyLookup") @Valid CompanyLookup companyLookup,
        BindingResult bindingResult, Model model,
        @RequestParam(name = "noCompanyOption", required = false) String noCompanyOption)
        throws InvalidRequestException, ServiceException {
        if(forwardResult.hasErrors()){
            throw new InvalidRequestException(String.format("Invalid forward URL: [%s]", forward.getForward()));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("noCompanyOption", noCompanyOption);
            return COMPANY_LOOKUP;
        }

        Company company = companyLookupService
                .getCompanyProfile(companyLookup.getCompanyNumber());

        if (company == null) {
            List<ValidationError> validationErrors = new ArrayList<>();
            ValidationError error = new ValidationError();
            error.setFieldPath("companyNumber");
            error.setMessageKey("company.not.found");
            validationErrors.add(error);
            validationHandler.bindValidationErrors(bindingResult, validationErrors);
            model.addAttribute("noCompanyOption", noCompanyOption);
            return COMPANY_LOOKUP;
        }

        UriTemplate forwardURI = new UriTemplate(forward.getForward());
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                forwardURI.expand(company.getCompanyNumber()).toString();
    }
}
