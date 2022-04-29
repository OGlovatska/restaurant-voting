package voting.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import voting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Override
    @Cacheable("restaurants")
    List<Restaurant> findAll();

    @Cacheable("restaurants")
    Optional<Restaurant> findById(int id);

    @Override
    @Modifying
    @Transactional
    @CachePut(value = "restaurants", key = "#restaurant.name")
    Restaurant save(Restaurant restaurant);

    @Override
    @Modifying
    @Transactional
    @CacheEvict(value = "restaurants", key = "#restaurant.name")
    void delete(Restaurant restaurant);

    @Override
    @Modifying
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    void deleteById(Integer id);

    @Cacheable("restaurants")
    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.dishes d WHERE d.date =:date")
    List<Restaurant> getWithDishes(LocalDate date);
}
