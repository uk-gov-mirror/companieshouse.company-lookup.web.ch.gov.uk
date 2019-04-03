package uk.gov.companieshouse.lookup.controller;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.companieshouse.lookup.model.CompanyConfirmation;

@RunWith(SpringRunner.class)
@WebMvcTest(CompanyConfirmationController.class)
public class CompanyConfirmationControllerTest {

    private static final String COMPANY_LOOKUP_URL = "/company-lookup/{companyNumber}/confirmation?forward={forward}";
    private static final String FORWARD_URL = "/test/{companyNumber}/test";
    private static final String ACCOUNT_NUMBER = "01234567";

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CompanyConfirmation companyConfirmation;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getCompanyLookup() throws Exception {
        this.mockMvc.perform(
            get(COMPANY_LOOKUP_URL, ACCOUNT_NUMBER, FORWARD_URL))
            .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void postCompanyLookup() throws Exception {
        when(companyConfirmation.getCompanyNumber()).thenReturn(ACCOUNT_NUMBER);
        this.mockMvc.perform(post(COMPANY_LOOKUP_URL, ACCOUNT_NUMBER, FORWARD_URL)
            .flashAttr("companyConfirmation", companyConfirmation))
            .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/test/01234567/test"));
    }

}