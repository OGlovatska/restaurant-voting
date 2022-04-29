package voting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import voting.VoteTestData;
import voting.model.Vote;
import voting.repository.VoteRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static voting.TestUtil.readFromJson;
import static voting.UserTestData.ADMIN_MAIL;
import static voting.UserTestData.USER_MAIL;
import static voting.VoteTestData.VOTE_1;
import static voting.VoteTestData.VOTE_MATCHER;
import static voting.controller.VoteController.URL;
import static voting.util.JsonUtil.writeValue;

public class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForDate() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "?date=2022-04-26"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(VOTE_MATCHER.contentJson(VOTE_1))
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void vote() throws Exception {
        Vote newVote = VoteTestData.getNew();
        MvcResult result = perform(MockMvcRequestBuilders.post(URL + "?restaurantId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newVote)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        Vote voted = readFromJson(result, Vote.class);
        int newId = voted.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(voted, newVote);
        VOTE_MATCHER.assertMatch(voted, voteRepository.findById(newId).orElseThrow());
    }
}
