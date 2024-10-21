package uk.gov.companieshouse.lookup.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.lookup.internationalisation.InternationalisationConfig;
import uk.gov.companieshouse.lookup.model.Company;
import uk.gov.companieshouse.lookup.service.CompanyLookupService;
import uk.gov.companieshouse.lookup.validation.ValidationHandler;

@WebMvcTest(CompanyLookupController.class)
@TestPropertySource("classpath:application-test.properties")
@Import(InternationalisationConfig.class)
class CompanyLookupControllerTest {

    private static final String COMPANY_LOOKUP_URL = "/company-lookup/search?forward={forward}";
    private static final String COMPANY_LOOKUP_NO_NUMBER_URL = "/company-lookup/no-number?forward={forward}";
    private static final String TEST_PATH = "companyLookup.companyNumber";
    private static final String TEMPLATE = "lookup/companyLookup";
    private static final String ERROR_TEMPLATE = "error";
    private static final String MODEL_ATTRIBUTE = "companyLookup";
    private static final String FORWARD_URL_PARAM = "forwardURL";
    private static final String COMPANY_NUMBER = "12345678";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyLookupService companyLookupService;

    @MockBean
    private ValidationHandler validationHandler;

    @MockBean
    private Company company;

    @MockBean
    private ApiErrorResponseException apiErrorResponseException;

    @BeforeEach
    public void setUpBeforeEAch() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Get Company Lookup - Success")
    void getCompanyLookup() throws Exception {
        this.mockMvc.perform(get(COMPANY_LOOKUP_URL, FORWARD_URL_PARAM))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(view().name(TEMPLATE))
            .andExpect(model().attributeExists(MODEL_ATTRIBUTE));
    }

    @Test
    @DisplayName("Get Company Lookup - Failed, bad forward URL")
    void getCompanyLookupWhenForwardUrlBad() throws Exception {
        this.mockMvc.perform(get(COMPANY_LOOKUP_URL, "@:bad-forward-url"))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(view().name(ERROR_TEMPLATE))
            .andExpect(model().attributeDoesNotExist(MODEL_ATTRIBUTE));
    }

    @Test
    @DisplayName("Get Company Lookup Without Number - Success")
    void getCompanyLookupWithoutNumber() throws Exception {
        this.mockMvc.perform(get(COMPANY_LOOKUP_NO_NUMBER_URL, FORWARD_URL_PARAM))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + FORWARD_URL_PARAM))
            .andReturn();
    }

    @Test
    @DisplayName("Get Company Lookup Without Number - Failed, bad forward URL")
    void getCompanyLookupWithoutNumberWhenForwardUrlBad() throws Exception {
        this.mockMvc.perform(get(COMPANY_LOOKUP_NO_NUMBER_URL, "@:bad-forward-url"))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(view().name(ERROR_TEMPLATE))
            .andExpect(model().attributeDoesNotExist(MODEL_ATTRIBUTE));
    }

    @Test
    @DisplayName("Post Company Lookup - Success")
    void postCompanyLookup() throws Exception {
        when(companyLookupService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(company);
        this.mockMvc
            .perform(post(COMPANY_LOOKUP_URL, FORWARD_URL_PARAM).param("companyNumber", COMPANY_NUMBER))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + FORWARD_URL_PARAM))
            .andReturn();
    }

    @Test
    @DisplayName("Post Company Lookup - Fail bind error")
    void postCompanyLookupBindFail() throws Exception {
        this.mockMvc.perform(post(COMPANY_LOOKUP_URL, FORWARD_URL_PARAM)
            .param(TEST_PATH, "test"))
            .andExpect(status().isOk())
            .andExpect(view().name(TEMPLATE))
            .andExpect(model().attributeExists(MODEL_ATTRIBUTE));
    }

    @Test
    @DisplayName("Post Company Lookup - Failed to find the company")
    void postCompanyLookupFail() throws Exception {
        when(companyLookupService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(null);
        this.mockMvc
            .perform(post(COMPANY_LOOKUP_URL, FORWARD_URL_PARAM).param("companyNumber", COMPANY_NUMBER))
            .andExpect(status().isOk())
            .andExpect(view().name(TEMPLATE));
    }

    @Test
    @DisplayName("Post Company Lookup - Absolute URL specified")
    void postCompanyLookupAbsoluteFail() throws Exception {
        this.mockMvc.perform(post(COMPANY_LOOKUP_URL, "http://0.0.0.0"))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name(ERROR_TEMPLATE));
    }

    @Test
    @DisplayName("Test Company Lookup Controller with Language Parameter")
    void testCompanyLookupController() throws Exception{

        MvcResult result = mockMvc.perform(get(COMPANY_LOOKUP_URL, FORWARD_URL_PARAM)
        .param("lang", "cy"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        Document doc = Jsoup.parse(responseContent);
        
        assertThat(doc.selectFirst("title").text()).contains("Beth yw rhif y cwmni?");
        assertThat(doc.selectFirst("label").text()).contains("Beth yw rhif y cwmni?");
        assertThat(doc.getElementById("company-lookup-hint").text()).contains("Cofnodwch y rif cwmni sy'n 8 nod");
        assertThat(doc.getElementsByClass("govuk-inset-text").first().text()).contains("Os oes gennych rif cwmni sy'n 7 nod neu lai, nodwch seroau ar y dechrau fel ei fod yn 8 nod i gyd. Er enghraifft, os yw'n 12345, rhowch 00012345");
        assertThat(doc.getElementById("company-number-help-text-link").text()).contains("Sut ydw i'n dod o hyd i rif y cwmni?");
        assertThat(doc.getElementById("company-number-help-text").text()).contains("Gallwch ddod o hyd i hyn trwy chwilio am y cwmni ar gofrestr Tŷ'r Cwmnïau (yn agor mewn tab newydd).");
        assertThat(doc.select("input").get(1).val()).contains("Parhau");
    }

    @Test
    @DisplayName("Test Company Lookup Welsh Errors for null company number")
    void testNullCompanyNumberErrorMessages() throws Exception{

        MvcResult result = mockMvc.perform(post(COMPANY_LOOKUP_URL, FORWARD_URL_PARAM)
        .param("lang", "cy"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        Document doc = Jsoup.parse(responseContent);
        
        assertThat(doc.selectFirst("#error-summary-heading").text()).contains("Mae yna broblem");
        assertTrue(doc.toString().contains("Cofnodwch rif y cwmni"));
    }

    @Test
    @DisplayName("Test Company Lookup Welsh Errors for no empty company number")
    void testEmptyCompanyNumberErrorMessage() throws Exception{

        MvcResult result = mockMvc.perform(post(COMPANY_LOOKUP_URL, FORWARD_URL_PARAM)
        .param("lang", "cy").param("companyNumber", ""))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        Document doc = Jsoup.parse(responseContent);
        
        assertThat(doc.selectFirst("#error-summary-heading").text()).contains("Mae yna broblem");
        assertTrue(doc.toString().contains("Cofnodwch rif y cwmni"));
    }

    @Test
    @DisplayName("Test Company Lookup Welsh Errors for wrong length company number")
    void testCompanyNumberLengthErrorMessage() throws Exception{

        MvcResult result = mockMvc.perform(post(COMPANY_LOOKUP_URL, FORWARD_URL_PARAM)
        .param("lang", "cy").param("companyNumber", "12"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        Document doc = Jsoup.parse(responseContent);
        
        assertThat(doc.selectFirst("#error-summary-heading").text()).contains("Mae yna broblem");
        assertTrue(doc.toString().contains("Rhaid i rif y cwmni fod ag 8 nod. Os yw'n 7 nod neu lai, nodwch seroau ar y dechrau fel ei fod yn 8 nod i gyd."));
    }

    @Test
    @DisplayName("Test Company Lookup Welsh Errors for invalid company number")
    void testInvalidCompanyNumberErrorMessage() throws Exception{

        MvcResult result = mockMvc.perform(post(COMPANY_LOOKUP_URL, FORWARD_URL_PARAM)
        .param("lang", "cy").param("companyNumber", "san-goku"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        Document doc = Jsoup.parse(responseContent);
        
        assertThat(doc.selectFirst("#error-summary-heading").text()).contains("Mae yna broblem");
        assertTrue(doc.toString().contains("Rhaid i rif cwmni gynnwys rhifau a llythrennau A i Z yn unig"));
    }
}