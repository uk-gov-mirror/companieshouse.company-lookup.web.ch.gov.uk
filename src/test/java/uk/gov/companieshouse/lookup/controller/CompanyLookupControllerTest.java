package uk.gov.companieshouse.lookup.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.lookup.model.CompanyDetail;
import uk.gov.companieshouse.lookup.model.CompanyLookup;
import uk.gov.companieshouse.lookup.service.CompanyLookupService;
import uk.gov.companieshouse.lookup.validation.ValidationError;
import uk.gov.companieshouse.lookup.validation.ValidationHandler;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyLookupControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CompanyLookupService companyLookupService;

    @Mock
    private ValidationHandler validationHandler;

    @Mock
    private CompanyDetail companyDetail;

    @Mock
    private CompanyLookup companyLookup;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private List<ValidationError> validationErrors;

    @InjectMocks
    private CompanyLookupController companyLookupController;

    @BeforeEach
    void setUpBeforeEAch() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(companyLookupController).build();
    }


    @Test
    public void getCompanyLookup() throws Exception {
        this.mockMvc.perform(get("/company-lookup/search?forward={forward}", "forwardURL"))
            .andDo(print()).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void postCompanyLookup() throws Exception {
        when(companyLookupService.getCompanyProfile(anyString())).thenReturn(companyDetail);
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
            .andExpect(forwardedUrl("lookup/companyLookup"));
    }

    @Test
    public void postCompanyLookupNoneFound() throws Exception {
        when(companyLookupService.getCompanyProfile(null)).thenReturn(null);
        this.mockMvc.perform(post("/company-lookup/search?forward={forward}", "forwardURL"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(forwardedUrl("lookup/companyLookup"));
    }
}