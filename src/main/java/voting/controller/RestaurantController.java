package voting.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import voting.model.Restaurant;
import voting.repository.RestaurantRepository;
import voting.util.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static voting.util.ValidationUtil.assureIdConsistent;

@RestController
@Slf4j
@AllArgsConstructor
@Tag(name = "Restaurant Controller")
public class RestaurantController {
    static final String URL = "/restaurant";
    private final RestaurantRepository restaurantRepository;

    @GetMapping("restaurants")
    public List<Restaurant> findAll() {
        log.info("get all restaurants");
        return restaurantRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update restaurant{} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantRepository.save(restaurant);
    }

    @GetMapping( URL + "/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get restaurant with id={}", id);
        return ResponseEntity.of(restaurantRepository.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant with id={}", id);
        restaurantRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        log.info("create restaurant{}", restaurant);
        ValidationUtil.checkNew(restaurant);
        restaurant = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/restaurant/{id}")
                .buildAndExpand(restaurant.id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(restaurant);
    }

    @GetMapping(value = "restaurants/today-menu")
    public List<Restaurant> findAllWithDishesByDate() {
        log.info("get all restaurants with dishes menu for this day");
        return restaurantRepository.getWithDishes(LocalDate.now());
    }
}
