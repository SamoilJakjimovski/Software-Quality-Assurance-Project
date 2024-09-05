package mk.ukim.finki.wp.tests.selenium;

import mk.ukim.finki.wp.assertions.Assertions;
import mk.ukim.finki.wp.tests.model.Match;
import mk.ukim.finki.wp.tests.model.MatchLocation;
import mk.ukim.finki.wp.tests.model.MatchType;
import mk.ukim.finki.wp.tests.service.MatchLocationService;
import mk.ukim.finki.wp.tests.service.MatchService;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SeleniumScenarioTest {


    @Autowired
    MatchLocationService matchLocationService;

    @Autowired
    MatchService matchService;

    @Test
    public void testMatchListing() {

        List<Match> entities = this.matchService.listAllMatches();
        int itemNum = entities.size();

        Assertions.assertNotEquals("Empty db", 0, itemNum);

        ItemsPage listPage = ItemsPage.to(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");
        listPage.assertItems(itemNum);

    }

    @Test
    public void testFiltering() {

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
    public void testFilterService() {

        Assertions.assertEquals("without filter", 10, this.matchService.listMatchesWithPriceLessThanAndType(null, null).size());
        Assertions.assertEquals("by price less than only", 2, this.matchService.listMatchesWithPriceLessThanAndType(45.0, null).size());
        Assertions.assertEquals("by type only", 3, this.matchService.listMatchesWithPriceLessThanAndType(null, MatchType.FRIENDLY).size());
        Assertions.assertEquals("by price less than and type", 1, this.matchService.listMatchesWithPriceLessThanAndType(100.0, MatchType.FRIENDLY).size());


    }

    @Test
    public void testCreate() throws Exception {

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
        Assertions.assertEquals("Number of items", itemNum + 1, matches.size());

        addRequest = MockMvcRequestBuilders
                .get("/matches/add");

        this.mockMvc.perform(addRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name(new ViewMatcher("form")));


    }

    @Test
    public void testEdit() throws Exception {

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
        Assertions.assertEquals("Number of items", itemNum, entities.size());
        Assertions.assertEquals("The updated entity name is not as expected.", "testEventName", entities.get(itemNum - 1).getName());

        editRequest = MockMvcRequestBuilders
                .get("/matches/" + entities.get(itemNum - 1).getId() + "/edit");

        this.mockMvc.perform(editRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name(new ViewMatcher("form")));


    }

    @Test
    public void testDelete() throws Exception {

        List<Match> entities = this.matchService.listAllMatches();
        int itemNum = entities.size();

        MockHttpServletRequestBuilder deleteRequest = MockMvcRequestBuilders
                .post("/matches/" + entities.get(itemNum - 1).getId() + "/delete");

        this.mockMvc.perform(deleteRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        entities = this.matchService.listAllMatches();
        Assertions.assertEquals("Number of items", itemNum - 1, entities.size());


    }

    @Test
    public void testSecurityUrls() {

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

        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);

        AbstractPage.get(this.driver, LIST_URL);
        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);

        AbstractPage.get(this.driver, EVENTS_ADD_URL);
        AbstractPage.assertRelativeUrl(this.driver, EVENTS_ADD_URL);

        AbstractPage.get(this.driver, editUrl);
        AbstractPage.assertRelativeUrl(this.driver, editUrl);

        AbstractPage.assertRelativeUrl(this.driver, "/");

    }

    @Test
    public void testFollow() throws Exception {

        List<Match> entities = this.matchService.listAllMatches();

        int itemNum = entities.size();

        MockHttpServletRequestBuilder followRequest = MockMvcRequestBuilders
                .post("/matches/" + entities.get(0).getId() + "/follow");

        this.mockMvc.perform(followRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        entities = this.matchService.listAllMatches();
        Assertions.assertEquals("Number of likes", entities.get(0).getFollows(), 1);


    }

    private HtmlUnitDriver driver;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.driver = new HtmlUnitDriver(true);
    }

    @AfterEach
    public void destroy() {
        if (this.driver != null) {
            this.driver.close();
        }
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

