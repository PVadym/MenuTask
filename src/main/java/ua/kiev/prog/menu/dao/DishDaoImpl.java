package ua.kiev.prog.menu.dao;

import org.hibernate.Transaction;
import ua.kiev.prog.menu.entity.Dish;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Вадим on 10.09.2017.
 */
public class DishDaoImpl implements DishDao{

    private EntityManager entityManager;

    public DishDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Dish dish) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(dish);
            entityManager.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }

    @Override
    public List<Dish> getAll() {
        List<Dish> dishes = new ArrayList<>();
        dishes.addAll(entityManager.createQuery("select d from Dish d").getResultList());
        return dishes;
    }

    @Override
    public List<Dish> getAllWithDiscount() {
        List<Dish> dishes = new ArrayList<>();
//        Query query = entityManager.createQuery("select d from Dish d where d.discount =:discount");
//        query.setParameter("discount",true);
//        dishes.addAll(query.getResultList());
        // Criteria
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Dish> cq = cb.createQuery(Dish.class);
        Root<Dish> dishRoot = cq.from(Dish.class);
        cq.select(dishRoot)
                .where(cb.equal(dishRoot.get("discount"), true));
        dishes = entityManager.createQuery(cq).getResultList();
        return dishes;
    }

    @Override
    public List<Dish> getByPrice(double from, double to) {
        List<Dish> dishes = new ArrayList<>();
        Query query = entityManager.createQuery("select d from Dish d where d.price between :from1 and :to");
        query.setParameter("from1",from);
        query.setParameter("to",to);
        dishes.addAll(query.getResultList());
        // Criteria
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Dish> cq = cb.createQuery(Dish.class);
//        Root<Dish> dishRoot = cq.from(Dish.class);
//        cq.select(dishRoot)
//                .where(cb.between(dishRoot.get("price"),from,to));
//        dishes = entityManager.createQuery(cq).getResultList();
        return dishes;
    }

    @Override
    public List<Dish> getUpToKg() {
        List<Dish> dishes = new ArrayList<>();
        List<Dish> result = new ArrayList<>();
        dishes.addAll(getAll());
        Collections.shuffle(dishes);
        int weight = 0;
        for(int i = 0; i < dishes.size()||weight <=1000; i++){
            weight+=dishes.get(i).getWeight();
            if(weight <= 1000){
                result.add(dishes.get(i));
            }
        }
        System.out.println("Total weigth = " + result.stream().reduce(0,(summ,dish) -> summ+=dish.getWeight(),(sum1,sum2) -> sum1+sum2));
        return result;
    }
}
