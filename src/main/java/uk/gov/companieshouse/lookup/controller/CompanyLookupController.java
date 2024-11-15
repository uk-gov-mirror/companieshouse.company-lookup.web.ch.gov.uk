package uk.gov.companieshouse.lookup.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
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

    public static final String NO_COMPANY_OPTION = "noCompanyOption";
    private static final String COMPANY_LOOKUP = "lookup/companyLookup";
    private static final String INVALID_FORWARD_URL = "Invalid forward URL: [%s]";

    private final CompanyLookupService companyLookupService;

    private final ValidationHandler validationHandler;

    @Autowired
    public CompanyLookupController(CompanyLookupService companyLookupService,
            ValidationHandler validationHandler) {
        this.companyLookupService = companyLookupService;
        this.validationHandler = validationHandler;
    }

    /**
     * Gets company lookup.
     *
     * @param forward         the forward
     * @param forwardResult   the forward result
     * @param model           the model
     * @param noCompanyOption the no company option
     * @return the company lookup
     * @throws InvalidRequestException the invalid request exception
     */
    @GetMapping("/search")
    public String getCompanyLookup(@Valid @ModelAttribute ForwardUrl forward,
            BindingResult forwardResult,
            HttpServletRequest httpServletRequest,
            Model model,
            @RequestParam(name = NO_COMPANY_OPTION, required = false) String noCompanyOption)
            throws InvalidRequestException {
        if (forwardResult.hasErrors()) {
            throw new InvalidRequestException(
                    String.format(INVALID_FORWARD_URL, forward.getForward()));
        }
        CompanyLookup companyLookup = new CompanyLookup();

        model.addAttribute("companyLookup", companyLookup);
        model.addAttribute(NO_COMPANY_OPTION, noCompanyOption);
        model.addAttribute("onWelshJourney", httpServletRequest.getAttribute("onWelshJourney"));

        return COMPANY_LOOKUP;
    }

    /**
     * Gets company lookup no company.
     *
     * @param forward       the forward
     * @param forwardResult the forward result
     * @param model         the model
     * @return the company lookup no company
     * @throws InvalidRequestException the invalid request exception
     */
    @GetMapping("/no-number")
    public String getCompanyLookupNoCompany(@Valid ForwardUrl forward, BindingResult forwardResult,
            Model model) throws InvalidRequestException {

        if (forwardResult.hasErrors()) {
            throw new InvalidRequestException(
                    String.format(INVALID_FORWARD_URL, forward.getForward()));
        }
        UriTemplate forwardURI = new UriTemplate(forward.getForward());

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + forwardURI.expand("noCompany");
    }


    /**
     * Post company lookup string.
     *
     * @param forward         the forward
     * @param forwardResult   the forward result
     * @param companyLookup   the company lookup
     * @param bindingResult   the binding result
     * @param model           the model
     * @param noCompanyOption the no company option
     * @return the string
     * @throws InvalidRequestException the invalid request exception
     * @throws ServiceException        the service exception
     */
    @PostMapping("/search")
    public String postCompanyLookup(@Valid ForwardUrl forward, BindingResult forwardResult,
            @ModelAttribute("companyLookup") @Valid CompanyLookup companyLookup,
            BindingResult bindingResult, Model model,
            @RequestParam(name = NO_COMPANY_OPTION, required = false) String noCompanyOption)
            throws InvalidRequestException, ServiceException {

        if (forwardResult.hasErrors()) {
            throw new InvalidRequestException(
                    String.format(INVALID_FORWARD_URL, forward.getForward()));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute(NO_COMPANY_OPTION, noCompanyOption);
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
            model.addAttribute(NO_COMPANY_OPTION, noCompanyOption);
            return COMPANY_LOOKUP;
        }

        UriTemplate forwardUri = new UriTemplate(forward.getForward());
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                + forwardUri.expand(company.getCompanyNumber()).toString();
    }
}
