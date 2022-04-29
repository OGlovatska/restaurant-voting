package voting.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import voting.RestaurantTestData;
import voting.model.Restaurant;
import voting.repository.RestaurantRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static voting.RestaurantTestData.*;
import static voting.TestUtil.readFromJson;
import static voting.UserTestData.ADMIN_MAIL;
import static voting.controller.RestaurantController.URL;
import static voting.util.JsonUtil.writeValue;

public class RestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurants))
                .andDo(print());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "/" + RESTAURANT_1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANT_1))
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + "/" + RESTAURANT_1_ID))
                .andExpect(status().isNoContent())
                .andDo(print());
        Assertions.assertFalse(restaurantRepository.findById(RESTAURANT_1_ID).isPresent());
        Assertions.assertTrue(restaurantRepository.findById(RESTAURANT_2_ID).isPresent());
    }


    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void register() throws Exception {
        Restaurant newRestaurant = RestaurantTestData.getNew();
        MvcResult result = perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newRestaurant)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        Restaurant registered = readFromJson(result, Restaurant.class);
        int newId = registered.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(registered, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(registered, restaurantRepository.findById(newId).orElseThrow());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = RestaurantTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(URL + "/" + RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andExpect(status().isNoContent())
                .andDo(print());
        RESTAURANT_MATCHER.assertMatch(updated, restaurantRepository.findById(RESTAURANT_1_ID).orElseThrow());
    }
}
