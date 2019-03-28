package uk.gov.companieshouse.lookup.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;

@Controller
@RequestMapping("/accounts/confirmation")
public class CompanyConfirmationController {

    private static final UriTemplate SERVICE_EXIT_URL = new UriTemplate("/success");

    @GetMapping
    public String getCompanyConfirmation(Model model, HttpServletRequest request) {

        model.addAttribute("backButton", "/accounts/lookup");
        return "lookup/companyConfirm";
    }

    @PostMapping
    public String postCompanyConfirmation(
        @ModelAttribute("companyProfile") @Valid CompanyProfileApi companyProfile,
        BindingResult bindingResult, Model model, HttpServletRequest request,
        RedirectAttributes attributes) {

        attributes.addFlashAttribute("companyProfile", companyProfile);
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX +
            SERVICE_EXIT_URL.expand("").toString();
    }

}
