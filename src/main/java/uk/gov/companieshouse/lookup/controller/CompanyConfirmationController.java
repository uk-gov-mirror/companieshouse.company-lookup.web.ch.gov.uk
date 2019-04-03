package uk.gov.companieshouse.lookup.controller;


import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.lookup.model.CompanyConfirmation;

@Controller
@RequestMapping("/company-lookup/{companyNumber}/confirmation")
public class CompanyConfirmationController {

    @GetMapping
    public String getCompanyConfirmation(
        @ModelAttribute("companyConfirmation") @Valid CompanyConfirmation companyConfirmation) {
        return "lookup/companyConfirm";
    }

    @PostMapping
    public String postCompanyConfirmation(@RequestParam("forward") String forward,
        @ModelAttribute("companyConfirmation") CompanyConfirmation companyConfirmation) {

        UriTemplate forwardURI = new UriTemplate(forward);
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
            forwardURI.expand(companyConfirmation.getCompanyNumber()).toString();
    }

}