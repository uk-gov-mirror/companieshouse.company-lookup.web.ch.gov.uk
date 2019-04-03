package uk.gov.companieshouse.lookup.controller;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.companieshouse.lookup.model.CompanyDetail;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyDetailControllerTest {

    private static final String COMPANY_LOOKUP_URL = "/company-lookup/{companyNumber}/detail?forward={forward}";
    private static final String FORWARD_URL = "/test/{companyNumber}/test";
    private static final String ACCOUNT_NUMBER = "01234567";

    private MockMvc mockMvc;

    @Mock
    private CompanyDetail companyDetail;

    @InjectMocks
    private CompanyDetailController companyDetailController;

    @BeforeEach
    void setUpBeforeEAch() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(companyDetailController).build();
    }

    @Test
    public void getCompanyLookup() throws Exception {
        this.mockMvc.perform(
            get(COMPANY_LOOKUP_URL, ACCOUNT_NUMBER, FORWARD_URL))
            .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void postCompanyLookup() throws Exception {
        when(companyDetail.getCompanyNumber()).thenReturn(ACCOUNT_NUMBER);
        this.mockMvc.perform(post(COMPANY_LOOKUP_URL, ACCOUNT_NUMBER, FORWARD_URL)
            .flashAttr("companyDetail", companyDetail))
            .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/test/01234567/test"));
    }

}