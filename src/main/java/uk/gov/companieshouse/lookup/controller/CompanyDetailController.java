package uk.gov.companieshouse.lookup.controller;



import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.lookup.exception.ServiceException;
import uk.gov.companieshouse.lookup.model.CompanyDetail;
import uk.gov.companieshouse.lookup.service.CompanyLookupService;

@Controller
@RequestMapping("/company-lookup/{companyNumber}/detail")
public class CompanyDetailController {

    @Autowired
    private CompanyLookupService companyLookupService;

    @GetMapping
    public String getCompanyDetail(
        @ModelAttribute("companyDetail") @Valid CompanyDetail companyDetail, Model model) throws ServiceException {
        if (companyDetail.getCompanyName() ==  null){
            CompanyDetail company = companyLookupService
                .getCompanyProfile(companyDetail.getCompanyNumber());
            model.addAttribute("companyDetail", company);
        }
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