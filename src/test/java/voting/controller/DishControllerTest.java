package voting.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import voting.DishTestData;
import voting.model.Dish;
import voting.repository.DishRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static voting.DishTestData.*;
import static voting.RestaurantTestData.RESTAURANT_1_ID;
import static voting.TestUtil.readFromJson;
import static voting.UserTestData.ADMIN_MAIL;
import static voting.UserTestData.USER_MAIL;
import static voting.controller.DishController.URL;
import static voting.util.JsonUtil.writeValue;

public class DishControllerTest extends AbstractControllerTest {

    @Autowired
    private DishRepository dishRepository;

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void findAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/dishes" + "?restaurantId=" + RESTAURANT_1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dishes))
                .andDo(print());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void findAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get("/dishes/by-date?restaurantId=" + RESTAURANT_1_ID + "&date=2022-04-26"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dishesForDate))
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(URL + "/" + DISH_3_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andExpect(status().isNoContent())
                .andDo(print());
        DISH_MATCHER.assertMatch(updated, dishRepository.getById(DISH_3_ID));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "/" + DISH_3_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(DISH_MATCHER.contentJson(DISH_3))
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + "/" + DISH_1_ID))
                .andExpect(status().isNoContent())
                .andDo(print());
        Assertions.assertFalse(dishRepository.findById(DISH_1_ID).isPresent());
        Assertions.assertTrue(dishRepository.findById(DISH_2_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        Dish newDish = DishTestData.getNew();
        MvcResult result = perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writeValue(newDish)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        Dish registered = readFromJson(result, Dish.class);
        int newId = registered.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(registered, newDish);
        DISH_MATCHER.assertMatch(registered, dishRepository.findById(newId).orElseThrow());
    }
}
