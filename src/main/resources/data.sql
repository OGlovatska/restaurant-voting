INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD)
VALUES ('user@gmail.com', 'User_First', 'User_Last', '{noop}password'),
       ('admin@gmail.com', 'Admin_First', 'Admin_Last', '{noop}admin');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (NAME)
VALUES ('Mario Trattoria'),
       ('Nikos Greek Bistro');

INSERT INTO DISH (NAME, PRICE, RESTAURANT_ID, DATE)
VALUES ('Pizza Calzone', 12, 1, '2022-04-26'),
       ('Pasta', 17, 1, '2022-04-26'),
       ('Caprese Salad', 10, 1, now()),
       ('Pizza Mozzarella', 20, 1, now()),
       ('Greek Salad', 15, 2, now()),
       ('Moussaka', 25, 2, now());

INSERT INTO VOTE (USER_ID, RESTAURANT_ID, DATE)
VALUES (1, 2, '2022-04-26'),
       (1, 1, now());