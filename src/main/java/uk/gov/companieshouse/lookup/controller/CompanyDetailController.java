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
import uk.gov.companieshouse.lookup.model.CompanyDetail;

@Controller
@RequestMapping("/company-lookup/{companyNumber}/detail")
public class CompanyDetailController {

    @GetMapping
    public String getCompanyDetail(
        @ModelAttribute("companyDetail") @Valid CompanyDetail companyDetail) {
        return "lookup/companyDetail";
    }

    @PostMapping
    public String postCompanyDetail(@RequestParam("forward") String forward,
        @ModelAttribute("companyDetail") CompanyDetail companyDetail) {

        UriTemplate forwardURI = new UriTemplate(forward);
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
            forwardURI.expand(companyDetail.getCompanyNumber()).toString();
    }

}