package voting.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import voting.model.Dish;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {

    @Cacheable("dishes")
    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:id")
    List<Dish> findAllForRestaurant(Integer id);

    @Cacheable("dishes")
    Optional<Dish> findById(int id);

    @Override
    @Modifying
    @Transactional
    @CachePut(value = "dishes", key = "#dish.name")
    Dish save(Dish dish);

    @Override
    @Modifying
    @Transactional
    @CacheEvict(value = "dishes", key = "#dish.name")
    void delete(Dish dish);

    @Override
    @Modifying
    @Transactional
    @CacheEvict(value = "dishes", allEntries = true)
    void deleteById(Integer id);

    @Cacheable("dishes")
    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.date=:date")
    List<Dish> findAllByDate(Integer restaurantId, LocalDate date);
}
