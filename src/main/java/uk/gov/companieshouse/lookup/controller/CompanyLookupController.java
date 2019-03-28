package uk.gov.companieshouse.lookup.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.lookup.exception.ServiceException;
import uk.gov.companieshouse.lookup.model.CompanyLookup;
import uk.gov.companieshouse.lookup.service.CompanyLookupService;

@Controller
@RequestMapping("/accounts/lookup")
public class CompanyLookupController {

    private static final UriTemplate FOUND_REDIRECT = new UriTemplate("/transaction/{transactionId}/confirmation");

    @Autowired
    private CompanyLookupService companyLookupService;

    @GetMapping
    public String getCompanyLookup(Model model, HttpServletRequest request) throws ServiceException {

        model.addAttribute("companyLookup", new CompanyLookup());
        model.addAttribute("backButton", "/nowhere");
        return "lookup/companyLookup";
    }

    @PostMapping
    public String postCompanyLookup(@ModelAttribute("companyLookup") @Valid CompanyLookup companyLookup,
        BindingResult bindingResult, Model model, HttpServletRequest request) throws ServiceException {

        CompanyProfileApi companyProfile = companyLookupService
            .getCompanyProfile(companyLookup.getCompanyId());

        model.addAttribute("companyProfile", companyProfile);

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
            FOUND_REDIRECT.expand("").toString();
    }
}
