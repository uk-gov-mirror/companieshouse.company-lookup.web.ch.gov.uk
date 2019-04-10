package uk.gov.companieshouse.lookup.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
import uk.gov.companieshouse.lookup.model.CompanyDetail;
import uk.gov.companieshouse.lookup.service.CompanyLookupService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyDetailControllerTest {

    private static final String COMPANY_LOOKUP_URL = "/company-lookup/{companyNumber}/detail?forward={forward}";
    private static final String FORWARD_URL = "/test/{companyNumber}/test";
    private static final String COMPANY_NUMBER = "01234567";

    private MockMvc mockMvc;

    @Mock
    private CompanyDetail companyDetail;

    @InjectMocks
    private CompanyDetailController companyDetailController;

    @Mock
    private CompanyLookupService companyLookupService;

    @BeforeEach
    private void setUpBeforeEach() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(companyDetailController).build();
    }

    @Test
    @DisplayName("Get Company Lookup - Success")
    public void getCompanyDetail() throws Exception {
        this.mockMvc.perform(
            get(COMPANY_LOOKUP_URL, COMPANY_NUMBER, FORWARD_URL))
            .andExpect(status().isOk()).andReturn();
    }

    @Test
    @DisplayName("Post Company Detail - Success")
    public void postCompanyDetail() throws Exception {
        this.mockMvc.perform(post(COMPANY_LOOKUP_URL, COMPANY_NUMBER, FORWARD_URL))
            .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/test/"+COMPANY_NUMBER+"/test"));
    }

}