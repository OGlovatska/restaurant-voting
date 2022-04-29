Restaurant voting application
==========================

Build a voting system for deciding where to have lunch:

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
  * If it is before 11:00 we assume that he changed his mind.
  * If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

<a href="https://my-restaurant-voting.herokuapp.com/v3/api-docs/">Swagger API-docs </a>

<a href="https://my-restaurant-voting.herokuapp.com/swagger-ui.html/">Swagger UI </a>

## Swagger requests examples 


#### Authorization credentials:
User: 
* login: user@gmail.com
* password: password

Admin: 
* login: admin@gmail.com
* password: admin

### User Controller

| API endpoint   | Pre authorize| HTTP method | Response code | Request body example|
| -------------- | ------------ | ----------- | ------------- | ------------------- |
| /user          |User/Admin    |GET          | 200
| /user          |User/Admin    |PUT          | 204           |<pre>{<br>  "email": "user_new@gmail.com",<br>  "firstName": "Updated-First",<br>  "lastName": "Updated-Last",<br>  "password": "password",<br>  "roles":[<br>    "USER"<br> ]<br>}</pre>|
| /user          |User/Admin    |DELETE       | 204 
| /user/register |User/Admin    |POST         | 201           |<pre>{<br>  "email": "user_new@gmail.com",<br>  "firstName": "New-First",<br>  "lastName": "New-Last",<br>  "password": "password",<br>  "roles":[<br>    "USER"<br> ]<br>}</pre>|


### Restaurant Controller

| API endpoint           | Pre authorize  | HTTP method | Response code | Request parameter example| Request body example|
| ---------------------- | -------------- | ----------- | ------------- | -------------------------| ------------------- |
|/restaurant/{id}        |User/Admin      |GET          | 200           | id=1                     | 
|/restaurant/{id}        |Admin           |PUT          | 204           | id=1                     |<pre>{<br>  "name": "Updated Mario Trattoria"<br>}</pre>
|/restaurant/{id}        |Admin           |DELETE       | 204           | id=1
|/restaurant             |Admin           |POST         | 201           |                          |<pre>{<br>  "name": "Updated Mario Trattoria"<br>}</pre>
|/restaurants            |User/Admin      |GET          | 200
|/restaurants/today-menu |User/Admin      |GET          | 200

### Dish Controller

| API endpoint    | Pre authorize  | HTTP method | Response code | Request parameter example| Request body example|
| --------------- | -------------- | ----------- | ------------- | -------------------------| ------------------- |
|/dish/{id}       |Admin           |GET          | 200           | id=1                     |
|/dish/{id}       |Admin           |PUT          | 204           | id=1                     |<pre>{<br>  "date": "2022-04-26",<br>  "name": "Updated SALAD CAPRESE",<br>  "price": 12,<br>  "restaurant": {<br>    "id": 1<br>  }<br>}</pre>|
|/dish/{id}       |Admin           |DELETE       | 204           | id=1
|/dish            |Admin           |POST         | 201           |                          |<pre>{<br>  "date": "2022-04-26",<br>  "name": "New Dish",<br>  "price": 30,<br>  "restaurant": {<br>    "id": 1<br>  }<br>}</pre>|
|/dishes          |User/Admin      |GET          | 200           | restaurantId=1
|/dishes/by-date  |User/Admin      |GET          | 200           | restaurantId=1<br>date=2022-04-26

### Vote Controller

| API endpoint    | Pre authorize  | HTTP method | Response code | Request parameter example| 
| --------------- | -------------- | ----------- | ------------- | -------------------------| 
|/vote            |User            |GET          | 200           | date=2022-04-26          |
|/vote            |User            |POST         | 201           | restaurantId=1           |