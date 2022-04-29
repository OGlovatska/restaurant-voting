package voting;

import voting.model.Vote;

import java.time.LocalDate;

public class VoteTestData {
    public static final TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Vote.class, "restaurant", "user");

    public static final int VOTE_1_ID = 1;
    public static final int VOTE_2_ID = 2;
    public static final Vote VOTE_1 = new Vote(VOTE_1_ID, LocalDate.of(2022, 4, 26));
    public static final Vote VOTE_2 = new Vote(VOTE_2_ID, LocalDate.now());

    public static Vote getNew(){
        return new Vote(null, LocalDate.now());
    }
}
