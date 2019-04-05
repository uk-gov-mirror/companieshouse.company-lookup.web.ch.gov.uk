package uk.gov.companieshouse.lookup.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import uk.gov.companieshouse.lookup.validation.ValidationHandler;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyLookupControllerTest {

    private static final String COMPANY_LOOKUP_URL = "/company-lookup/search?forward={forward}";
    private static final String TEST_PATH = "companyLookup.companyNumber";
    private static final String TEMPLATE = "lookup/companyLookup";
    public static final String MODEL_ATTRIBUTE = "companyLookup";
    public static final String FORWARD_URL_PARAM = "forwardURL";
    public static final String COMPANY_NUMBER = "12345678";

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

    @InjectMocks
    private CompanyLookupController companyLookupController;


    @BeforeEach
    private void setUpBeforeEAch() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(companyLookupController).build();
    }

    @Test
    @DisplayName("Get Company Lookup - Success")
    public void getCompanyLookup() throws Exception {
        this.mockMvc.perform(get(COMPANY_LOOKUP_URL, FORWARD_URL_PARAM))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(view().name(TEMPLATE))
            .andExpect(model().attributeExists(MODEL_ATTRIBUTE)).andReturn();
    }

    @Test
    @DisplayName("Post Company Lookup - Success")
    public void postCompanyLookup() throws Exception {
        when(companyLookupService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyDetail);
        this.mockMvc
            .perform(post(COMPANY_LOOKUP_URL, FORWARD_URL_PARAM).param("companyNumber", COMPANY_NUMBER))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/company-lookup/12345678/detail"));
    }

    @Test
    @DisplayName("Post Company Lookup - Fail bind error")
    public void postCompanyLookupBindFail() throws Exception {
        this.mockMvc.perform(post(COMPANY_LOOKUP_URL, FORWARD_URL_PARAM)
            .param(TEST_PATH, "test"))
            .andExpect(status().isOk())
            .andExpect(view().name(TEMPLATE))
            .andExpect(model().attributeExists(MODEL_ATTRIBUTE));
    }

    @Test
    @DisplayName("Post Company Lookup - Failed to find the company")
    public void postCompanyLookupFail() throws Exception {
        when(companyLookupService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(null);
        this.mockMvc
            .perform(post(COMPANY_LOOKUP_URL, FORWARD_URL_PARAM).param("companyNumber", COMPANY_NUMBER))
            .andExpect(status().isOk())
            .andExpect(view().name(TEMPLATE));
    }

}