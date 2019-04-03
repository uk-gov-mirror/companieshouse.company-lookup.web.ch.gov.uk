package uk.gov.companieshouse.lookup.controller;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.lookup.model.CompanyConfirmation;
import uk.gov.companieshouse.lookup.model.CompanyLookup;
import uk.gov.companieshouse.lookup.service.CompanyLookupService;
import uk.gov.companieshouse.lookup.validation.ValidationError;
import uk.gov.companieshouse.lookup.validation.ValidationHandler;

@RunWith(SpringRunner.class)
@WebMvcTest(CompanyLookupController.class)
public class CompanyLookupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyLookupService companyLookupService;

    @MockBean
    private ValidationHandler validationHandler;

    @Mock
    private CompanyConfirmation companyConfirmation;

    @Mock
    private CompanyLookup companyLookup;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private List<ValidationError> validationErrors;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getCompanyLookup() throws Exception {
        this.mockMvc.perform(get("/company-lookup/search?forward={forward}", "forwardURL"))
            .andDo(print()).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void postCompanyLookup() throws Exception {
        when(companyLookupService.getCompanyProfile(anyString())).thenReturn(companyConfirmation);
        when(companyLookup.getCompanyNumber()).thenReturn("123");
        this.mockMvc.perform(post("/company-lookup/search?forward={forward}", "forwardURL")
            .flashAttr("companyLookup", companyLookup)).andDo(print())
            .andExpect(status().is3xxRedirection());
    }

    @Test
    public void postCompanyLookupFail() throws Exception {
        when(companyLookupService.validateCompanyLookup(any())).thenReturn(validationErrors);
        this.mockMvc.perform(post("/company-lookup/search?forward={forward}", "forwardURL"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("What is the company number")));
    }

    @Test
    public void postCompanyLookupNoneFound() throws Exception {
        when(companyLookupService.getCompanyProfile(anyString())).thenReturn(null);
        this.mockMvc.perform(post("/company-lookup/search?forward={forward}", "forwardURL"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("What is the company number")));
    }
}