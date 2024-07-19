package mk.ukim.finki.wp.sep2022;

import com.fasterxml.jackson.core.JsonProcessingException;
import mk.ukim.finki.wp.exam.util.CodeExtractor;
import mk.ukim.finki.wp.exam.util.ExamAssert;
import mk.ukim.finki.wp.sep2022.model.Match;
import mk.ukim.finki.wp.sep2022.model.MatchLocation;
import mk.ukim.finki.wp.sep2022.model.MatchType;
import mk.ukim.finki.wp.sep2022.selenium.AbstractPage;
import mk.ukim.finki.wp.sep2022.selenium.AddOrEditEvent;
import mk.ukim.finki.wp.sep2022.selenium.ItemsPage;
import mk.ukim.finki.wp.sep2022.selenium.LoginPage;
import mk.ukim.finki.wp.sep2022.service.MatchLocationService;
import mk.ukim.finki.wp.sep2022.service.MatchService;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SeleniumScenarioTest {


    @Autowired
    MatchLocationService matchLocationService;

    @Autowired
    MatchService matchService;

    @Test
    public void test_list_20pt() {

        List<Match> entities = this.matchService.listAllMatches();
        int itemNum = entities.size();

        ExamAssert.assertNotEquals("Empty db", 0, itemNum);

        ItemsPage listPage = ItemsPage.to(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");
        listPage.assertItems(itemNum);

    }

    @Test
    public void test_filter_10pt() {

        ItemsPage listPage = ItemsPage.to(this.driver);

        listPage.filter("", "");
        listPage.assertItems(10);

        listPage.filter("45.0", "");
        listPage.assertItems(2);

        listPage.filter("", MatchType.FRIENDLY.name());
        listPage.assertItems(3);

        listPage.filter("100.0", MatchType.FRIENDLY.name());
        listPage.assertItems(1);

    }

    @Test
    public void test_filter_service_10pt() {

        ExamAssert.assertEquals("without filter", 10, this.matchService.listMatchesWithPriceLessThanAndType(null, null).size());
        ExamAssert.assertEquals("by price less than only", 2, this.matchService.listMatchesWithPriceLessThanAndType(45.0, null).size());
        ExamAssert.assertEquals("by type only", 3, this.matchService.listMatchesWithPriceLessThanAndType(null, MatchType.FRIENDLY).size());
        ExamAssert.assertEquals("by price less than and type", 1, this.matchService.listMatchesWithPriceLessThanAndType(100.0, MatchType.FRIENDLY).size());


    }

    @Test
    public void test_create_10pt() {

        List<MatchLocation> locations = this.matchLocationService.listAll();
        List<Match> entities = this.matchService.listAllMatches();

        int itemNum = entities.size();
        ItemsPage listPage = null;

        try {
            LoginPage loginPage = LoginPage.openLogin(this.driver);
            listPage = LoginPage.doLogin(this.driver, loginPage, admin, admin);
        } catch (Exception e) {
        }
        listPage = AddOrEditEvent.add(this.driver, EVENTS_ADD_URL, "testName", "testDescription", "100", MatchType.CHARITY.name(), locations.get(0).getId().toString());
        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);
        listPage.assertNoError();
        listPage.assertItems(itemNum + 1);

    }

    @Test
    public void test_create_mvc_10pt() throws Exception {

        List<MatchLocation> locations = this.matchLocationService.listAll();
        List<Match> matches = this.matchService.listAllMatches();

        int itemNum = matches.size();

        MockHttpServletRequestBuilder addRequest = MockMvcRequestBuilders
                .post("/matches")
                .param("name", "testName")
                .param("description", "testDescription")
                .param("price", "45.0")
                .param("type", MatchType.CHARITY.name())
                .param("location", locations.get(0).getId().toString());

        this.mockMvc.perform(addRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        matches = this.matchService.listAllMatches();
        ExamAssert.assertEquals("Number of items", itemNum + 1, matches.size());

        addRequest = MockMvcRequestBuilders
                .get("/matches/add");

        this.mockMvc.perform(addRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name(new ViewMatcher("form")));


    }

    @Test
    public void test_edit_mvc_10pt() throws Exception {

        List<MatchLocation> locations = this.matchLocationService.listAll();
        List<Match> entities = this.matchService.listAllMatches();

        int itemNum = entities.size();

        MockHttpServletRequestBuilder editRequest = MockMvcRequestBuilders
                .post("/matches/" + entities.get(itemNum - 1).getId())
                .param("name", "testEventName")
                .param("description", "testEventDescription")
                .param("price", "100")
                .param("type", MatchType.CHARITY.name())
                .param("location", locations.get(0).getId().toString());

        this.mockMvc.perform(editRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        entities = this.matchService.listAllMatches();
        ExamAssert.assertEquals("Number of items", itemNum, entities.size());
        ExamAssert.assertEquals("The updated entity name is not as expected.", "testEventName", entities.get(itemNum - 1).getName());

        editRequest = MockMvcRequestBuilders
                .get("/matches/" + entities.get(itemNum - 1).getId() + "/edit");

        this.mockMvc.perform(editRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name(new ViewMatcher("form")));


    }

    @Test
    public void test_delete_mvc_5pt() throws Exception {

        List<Match> entities = this.matchService.listAllMatches();
        int itemNum = entities.size();

        MockHttpServletRequestBuilder deleteRequest = MockMvcRequestBuilders
                .post("/matches/" + entities.get(itemNum - 1).getId() + "/delete");

        this.mockMvc.perform(deleteRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        entities = this.matchService.listAllMatches();
        ExamAssert.assertEquals("Number of items", itemNum - 1, entities.size());


    }

    @Test
    public void test_security_urls_7pt() {

        List<Match> entities = this.matchService.listAllMatches();
        String editUrl = "/matches/" + entities.get(0).getId() + "/edit";

        ItemsPage.to(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");

        AbstractPage.get(this.driver, LIST_URL);
        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);
        AbstractPage.get(this.driver, EVENTS_ADD_URL);
        AbstractPage.assertRelativeUrl(this.driver, LOGIN_URL);
        AbstractPage.get(this.driver, editUrl);
        AbstractPage.assertRelativeUrl(this.driver, LOGIN_URL);
        AbstractPage.get(this.driver, "/random");
        AbstractPage.assertRelativeUrl(this.driver, LOGIN_URL);

        LoginPage loginPage = LoginPage.openLogin(this.driver);
        LoginPage.doLogin(this.driver, loginPage, admin, admin);
        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);

        AbstractPage.get(this.driver, LIST_URL);
        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);

        AbstractPage.get(this.driver, EVENTS_ADD_URL);
        AbstractPage.assertRelativeUrl(this.driver, EVENTS_ADD_URL);

        AbstractPage.get(this.driver, editUrl);
        AbstractPage.assertRelativeUrl(this.driver, editUrl);

        LoginPage.logout(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");

    }

    @Test
    public void test_follow_mvc_3pt() throws Exception {

        List<Match> entities = this.matchService.listAllMatches();

        int itemNum = entities.size();

        MockHttpServletRequestBuilder followRequest = MockMvcRequestBuilders
                .post("/matches/" + entities.get(0).getId() + "/follow");

        this.mockMvc.perform(followRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        entities = this.matchService.listAllMatches();
        ExamAssert.assertEquals("Number of likes", entities.get(0).getFollows(), 1);


    }

    private HtmlUnitDriver driver;
    private MockMvc mockMvc;

    private static String admin = "admin";
    private static String user = "user";

    @BeforeEach
    private void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.driver = new HtmlUnitDriver(true);
    }

    @AfterEach
    public void destroy() {
        if (this.driver != null) {
            this.driver.close();
        }
    }

    @AfterAll
    public static void finializeAndSubmit() throws JsonProcessingException {
        CodeExtractor.submitSourcesAndLogs();
    }

    public static final String LIST_URL = "/matches";
    public static final String EVENTS_ADD_URL = "/matches/add";
    public static final String LOGIN_URL = "/login";

    static class ViewMatcher implements Matcher<String> {

        final String baseName;

        ViewMatcher(String baseName) {
            this.baseName = baseName;
        }

        @Override
        public boolean matches(Object o) {
            if (o instanceof String) {
                String s = (String) o;
                return s.startsWith(baseName);
            }
            return false;
        }

        @Override
        public void describeMismatch(Object o, Description description) {
        }

        @Override
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
        }

        @Override
        public void describeTo(Description description) {
        }
    }
}

