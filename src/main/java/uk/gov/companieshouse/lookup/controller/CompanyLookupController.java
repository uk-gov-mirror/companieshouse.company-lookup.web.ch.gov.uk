package uk.gov.companieshouse.lookup.controller;


import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.lookup.model.CompanyConfirmation;
import uk.gov.companieshouse.lookup.model.CompanyLookup;
import uk.gov.companieshouse.lookup.service.CompanyLookupService;
import uk.gov.companieshouse.lookup.validation.ValidationError;
import uk.gov.companieshouse.lookup.validation.ValidationHandler;

@Controller
@RequestMapping("/company-lookup/search")
public class CompanyLookupController {

    private static final UriTemplate FOUND_REDIRECT = new UriTemplate(
        "/company-lookup/{companyNumber}/confirmation");
    private static final String COMPANY_LOOKUP = "lookup/companyLookup";

    @Autowired
    private CompanyLookupService companyLookupService;

    @Autowired
    private ValidationHandler validationHandler;

    @GetMapping
    public String getCompanyLookup(@RequestParam("forward") String forward, Model model) {
        CompanyLookup companyLookup = new CompanyLookup();
        model.addAttribute("companyLookup", companyLookup);
        return COMPANY_LOOKUP;
    }

    @PostMapping
    public String postCompanyLookup(@RequestParam("forward") String forward,
        @ModelAttribute("companyLookup") @Valid CompanyLookup companyLookup,
        BindingResult bindingResult, RedirectAttributes attributes) throws URIValidationException,
        ApiErrorResponseException {

        List<ValidationError> validationErrors = companyLookupService
            .validateCompanyLookup(companyLookup);
        if (!validationErrors.isEmpty()) {
            validationHandler.bindValidationErrors(bindingResult, validationErrors);
            return COMPANY_LOOKUP;
        }

        try {
            CompanyConfirmation companyConfirmation = companyLookupService
                .getCompanyProfile(companyLookup.getCompanyNumber());
            attributes.addAttribute("forward", forward);
            attributes.addFlashAttribute("companyConfirmation", companyConfirmation);

            return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
                FOUND_REDIRECT.expand(companyLookup.getCompanyNumber()).toString();

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
