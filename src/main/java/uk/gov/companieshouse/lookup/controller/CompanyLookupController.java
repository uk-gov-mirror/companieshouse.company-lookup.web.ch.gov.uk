package uk.gov.companieshouse.lookup.controller;


import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.lookup.model.CompanyLookup;
import uk.gov.companieshouse.lookup.service.CompanyLookupService;
import uk.gov.companieshouse.lookup.validation.ValidationError;
import uk.gov.companieshouse.lookup.validation.ValidationHandler;

@Controller
@RequestMapping("/accounts/lookup")
public class CompanyLookupController {

    private static final UriTemplate FOUND_REDIRECT = new UriTemplate("/accounts/confirmation");
    private static final String COMPANY_LOOKUP = "lookup/companyLookup";

    @Autowired
    private CompanyLookupService companyLookupService;

    @Autowired
    private ValidationHandler validationHandler;

    @GetMapping
    public String getCompanyLookup(Model model, HttpServletRequest request) {

        model.addAttribute("companyLookup", new CompanyLookup());
        model.addAttribute("backButton", "/nowhere");
        return COMPANY_LOOKUP;
    }

    @PostMapping
    public String postCompanyLookup(
        @ModelAttribute("companyLookup") @Valid CompanyLookup companyLookup,
        BindingResult bindingResult, Model model, HttpServletRequest request,
        RedirectAttributes attributes) throws URIValidationException, ApiErrorResponseException {

        List<ValidationError> validationErrors = companyLookupService
            .validateCompanyLookup(companyLookup);
        if (!validationErrors.isEmpty()) {
            validationHandler.bindValidationErrors(bindingResult, validationErrors);
            return COMPANY_LOOKUP;
        }

        try {
            CompanyProfileApi companyProfile = companyLookupService
                .getCompanyProfile(companyLookup.getCompanyNumber());
            attributes.addFlashAttribute("companyProfile", companyProfile);
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                FOUND_REDIRECT.expand("").toString();

        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                ValidationError error = new ValidationError();
                error.setMessageKey("company.not.found");
                validationErrors.add(error);
                validationHandler.bindValidationErrors(bindingResult, validationErrors);
                return COMPANY_LOOKUP;
            }
            throw e;
        }
    }
}
