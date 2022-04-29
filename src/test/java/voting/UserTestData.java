package voting;

import voting.model.Role;
import voting.model.User;

import java.util.List;

public class UserTestData {

    public static final TestMatcher<User> USER_MATCHER = TestMatcher.usingIgnoringFieldsComparator(User.class, "password");

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final String USER_MAIL = "user@gmail.com";
    public static final String ADMIN_MAIL = "admin@gmail.com";
    public static final User user = new User(USER_ID, USER_MAIL, "User_First", "User_Last", "password", List.of(Role.USER));
    public static final User admin = new User(ADMIN_ID, ADMIN_MAIL, "Admin_First", "Admin_Last", "admin", List.of(Role.ADMIN, Role.USER));

    public static User getNew() {
        return new User(null, "new@gmail.com", "New_First", "New_Last", "newpass", List.of(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER_ID, "user_update@gmail.com", "User_First_Update", "User_Last_Update", "password_update", List.of(Role.USER));
    }
}
