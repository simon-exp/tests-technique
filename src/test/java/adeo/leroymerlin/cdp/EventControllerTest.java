package adeo.leroymerlin.cdp;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private MockMvc mvc;



    /**
     * test of find events Rest method
     * @throws Exception  the exception
     */
    @Test
    @Order(1)
    public void testFindEvents() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/events/").accept(MediaType.APPLICATION_JSON))
                //test the return status code
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                //test body response json strucure
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].id").value(1000))
                .andExpect(jsonPath("$[0].title").value("GrasPop Metal Meeting"))
                .andExpect(jsonPath("$[0].imgUrl").value("img/1000.jpeg"))
                .andExpect(jsonPath("$[0].bands.length()").value(5))
                .andExpect(jsonPath("$[2].id").value(1002))
                .andExpect(jsonPath("$[2].title").value("Les Vieilles Charrues"))
                .andExpect(jsonPath("$[2].imgUrl").value("img/1002.jpeg"))
                .andExpect(jsonPath("$[2].bands.length()").value(1))
                .andExpect(jsonPath("$[2].bands[0].name").value("AC/DC"))
;

    }

    /**
     * test of find events filtered Rest method
     * @throws Exception the exception
     */
    @Test
    @Order(2)
    public void testFindEventsQuery() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/events/search/Wa").accept(MediaType.APPLICATION_JSON))
                //test the return status code
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                //test body response json strucure for Wa filter
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1000))
                .andExpect(jsonPath("$[0].title").value("GrasPop Metal Meeting[1]"))
                .andExpect(jsonPath("$[0].imgUrl").value("img/1000.jpeg"))
                .andExpect(jsonPath("$[0].bands.length()").value(1))
                .andExpect(jsonPath("$[0].bands[0].name").value("Metallica[1]"))
                .andExpect(jsonPath("$[0].bands[0].members.length()").value(1))
                .andExpect(jsonPath("$[0].bands[0].members[0].name").value("Queen Anika Walsh"))
        ;

    }

    @Test
    @Order(3)
    public void testUpdateEvent() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/events/1001")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1001,\"title\":\"Alcatraz Fest\",\"imgUrl\":\"img/1001.jpeg\",\"bands\":[{\"name\":\"Megadeth\",\"members\":[{\"name\":\"Queen Laila Shelton\"},{\"name\":\"Queen Robbie Bender\"},{\"name\":\"Queen Eleanor Fisher (Ellie)\"}]}],\"nbStars\":null,\"comment\":\"test comment\"}")
        ).andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.get("/api/events/").accept(MediaType.APPLICATION_JSON))
                //test the return status code
                .andExpect(status().isOk())
                //test body response json strucure
                .andExpect(jsonPath("$[1].id").value(1001))
                .andExpect(jsonPath("$[1].comment").value("test comment"));

    }

    @Test
    @Order(4)
    public void testDeleteEvent() throws Exception {
        //retrieve the complete event list
        mvc.perform(MockMvcRequestBuilders.get("/api/events/").accept(MediaType.APPLICATION_JSON))
                //test the return status code
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                //test body response json strucure
                .andExpect(jsonPath("$.length()").value(5));

        //delete first event
        mvc.perform(MockMvcRequestBuilders.delete("/api/events/1000").accept(MediaType.APPLICATION_JSON))
                //test the return status code
                .andExpect(status().isOk());
        //retrieve complete event list and note that there is only 4 event in json
        mvc.perform(MockMvcRequestBuilders.get("/api/events/").accept(MediaType.APPLICATION_JSON))
                //test the return status code
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                //test body response json strucure
                .andExpect(jsonPath("$.length()").value(4)).andReturn();
    }
}
