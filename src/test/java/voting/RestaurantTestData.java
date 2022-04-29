package voting;

import voting.model.Restaurant;

import java.util.List;

public class RestaurantTestData {

    public static final TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingEqualsComparator(Restaurant.class);

    public static final int RESTAURANT_1_ID = 1;
    public static final int RESTAURANT_2_ID = 2;
    public static final Restaurant RESTAURANT_1 = new Restaurant(RESTAURANT_1_ID, "Mario Trattoria");
    public static final Restaurant RESTAURANT_2 = new Restaurant(RESTAURANT_2_ID, "Nikos Greek Bistro");
    public static final List<Restaurant> restaurants = List.of(RESTAURANT_1, RESTAURANT_2);

    public static Restaurant getNew() {
        return new Restaurant(null, "New Restaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_1_ID, "New Mario Trattoria");
    }
}
