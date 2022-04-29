package voting.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import voting.model.Dish;
import voting.repository.DishRepository;
import voting.util.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static voting.util.ValidationUtil.assureIdConsistent;

@RestController
@Slf4j
@AllArgsConstructor
@Tag(name = "Dish Controller")
public class DishController {
    static final String URL = "/dish";
    private final DishRepository dishRepository;

    @GetMapping("dishes")
    public List<Dish> findAll(@RequestParam("restaurantId") Integer restaurantId) {
        log.info("get all dishes by restaurant id={}", restaurantId);
        return dishRepository.findAllForRestaurant(restaurantId);
    }

    @GetMapping("dishes/by-date")
    public List<Dish> findAllByDate(@RequestParam("restaurantId") Integer restaurantId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get all dishes by date={} and restaurant id={}", date, restaurantId);
        return dishRepository.findAllByDate(restaurantId, date);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable("id") int id) {
        log.info("update dish{} with id={}", dish, id);
        assureIdConsistent(dish, id);
        dishRepository.save(dish);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(URL + "/{id}")
    public ResponseEntity<Dish> get(@PathVariable int id) {
        log.info("get dish with id={}", id);
        return ResponseEntity.of(dishRepository.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant with id={}", id);
        dishRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Dish> create(@Valid @RequestBody Dish dish) {
        log.info("create dish{}", dish);
        ValidationUtil.checkNew(dish);
        dish = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(dish.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(dish);
    }
}