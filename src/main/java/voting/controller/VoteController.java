package voting.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import voting.AuthorizedUser;
import voting.error.IllegalRequestDataException;
import voting.model.Dish;
import voting.model.Restaurant;
import voting.model.User;
import voting.model.Vote;
import voting.repository.RestaurantRepository;
import voting.repository.UserRepository;
import voting.repository.VoteRepository;
import voting.util.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(VoteController.URL)
@Tag(name = "Vote Controller")
public class VoteController {
    static final String URL = "/vote";
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    public Vote getByDate(@AuthenticationPrincipal AuthorizedUser authUser,
                          @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get vote for date={}", date);
        return voteRepository.findByDate(authUser.id(), date);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Vote> vote(@RequestParam("restaurantId") Integer restaurantId,
                                       @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("make vote for restaurant with id={}", restaurantId);
        LocalDateTime timeNow = LocalDateTime.now();
        User user = authUser.getUser();
        Restaurant restaurant = restaurantRepository.getById(restaurantId);
        Vote vote = voteRepository.findByDate(user.id(), timeNow.toLocalDate());
        if (vote != null) {
            ValidationUtil.checkTime(timeNow);
            vote.setRestaurant(restaurant);
        } else {
            vote = new Vote(timeNow.toLocalDate(), restaurant, user);
        }
        voteRepository.save(vote);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "?date=" + timeNow.toLocalDate())
                .build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(vote);
    }
}
