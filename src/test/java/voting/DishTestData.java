package voting;

import voting.model.Dish;

import java.time.LocalDate;
import java.util.List;

import static voting.RestaurantTestData.RESTAURANT_1;

public class DishTestData {

    public static final TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingIgnoringFieldsComparator(
            Dish.class, "restaurant");

    public static final int DISH_1_ID = 1;
    public static final int DISH_2_ID = 2;
    public static final int DISH_3_ID = 3;
    public static final int DISH_4_ID = 4;

    public static final Dish DISH_1 = new Dish(DISH_1_ID, "Pizza Calzone", 12, LocalDate.of(2022, 4, 26));
    public static final Dish DISH_2 = new Dish(DISH_2_ID, "Pasta", 17, LocalDate.of(2022, 4, 26));
    public static final Dish DISH_3 = new Dish(DISH_3_ID, "Caprese Salad", 10, LocalDate.now());
    public static final Dish DISH_4 = new Dish(DISH_4_ID, "Pizza Mozzarella", 20, LocalDate.now());

    public static final List<Dish> dishes = List.of(DISH_1, DISH_2, DISH_3, DISH_4);
    public static final List<Dish> dishesForDate = List.of(DISH_1, DISH_2);

    public static Dish getNew() {
        Dish dish =  new Dish(null, "New Dish", 30, LocalDate.now());
        dish.setRestaurant(RESTAURANT_1);
        return dish;
    }

    public static Dish getUpdated() {
        return new Dish(DISH_3_ID, "New SALAD CAPRESE", 12, LocalDate.now());
    }
}
