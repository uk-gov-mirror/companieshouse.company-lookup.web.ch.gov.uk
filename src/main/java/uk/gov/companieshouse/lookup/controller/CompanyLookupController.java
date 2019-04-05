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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;
import uk.gov.companieshouse.lookup.Application;
import uk.gov.companieshouse.lookup.exception.ServiceException;
import uk.gov.companieshouse.lookup.model.CompanyDetail;
import uk.gov.companieshouse.lookup.model.CompanyLookup;
import uk.gov.companieshouse.lookup.service.CompanyLookupService;
import uk.gov.companieshouse.lookup.validation.ValidationError;
import uk.gov.companieshouse.lookup.validation.ValidationHandler;

@Controller
@RequestMapping("/company-lookup/search")
public class CompanyLookupController {

    private static final UriTemplate FOUND_REDIRECT = new UriTemplate(
        "/company-lookup/{companyNumber}/detail");
    private static final String COMPANY_LOOKUP = "lookup/companyLookup";

    protected static final Logger LOGGER = LoggerFactory
        .getLogger(Application.APPLICATION_NAME_SPACE);


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
        BindingResult bindingResult, RedirectAttributes attributes) throws ServiceException {

        if (bindingResult.hasErrors()) {
            return COMPANY_LOOKUP;
        }

        CompanyDetail companyDetail = companyLookupService
            .getCompanyProfile(companyLookup.getCompanyNumber());

        if (companyDetail == null) {
            List<ValidationError> validationErrors = new ArrayList<>();
            ValidationError error = new ValidationError();
            error.setMessageKey("company.not.found");
            validationErrors.add(error);
            validationHandler.bindValidationErrors(bindingResult, validationErrors);
            return COMPANY_LOOKUP;
        }

        attributes.addAttribute("forward", forward);
        attributes.addFlashAttribute("companyDetail", companyDetail);

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
            FOUND_REDIRECT.expand(companyLookup.getCompanyNumber()).toString();

    }
}
