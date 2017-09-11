package ua.kiev.prog.menu.dao;

import ua.kiev.prog.menu.entity.Dish;

import java.util.List;

/**
 * Created by Вадим on 10.09.2017.
 */
public interface DishDao {

    void save(Dish dish);
    List<Dish> getAll();
    List<Dish> getAllWithDiscount();
    List<Dish> getByPrice(double from, double to);
    List<Dish> getUpToKg();
}
