package com.nursultan;

import com.nursultan.entity.Role;
import com.nursultan.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("default");


        EntityManager manager = factory.createEntityManager();

        /*
        User user = manager.find(User.class, 3L);
        if (user != null) {
            System.out.println(user.getFirstName() + " " + user.getLastName());
        } else {
            System.out.println("User not found");
        }*/

        // first step
        TypedQuery<User> query = manager.createQuery(
                "select u from User u order by u.firstName", User.class);

        List<User> users = query.getResultList();
        for (User user : users) {
            System.out.println(user.getFirstName() + " " + user.getLastName());
        }

        query.getResultStream()
                .map(user -> user.getFirstName() + " " + user.getLastName())
                .forEach(System.out::println);

        // second step
        try {
            manager.getTransaction().begin();
            User user = new User();
            user.setFirstName("Petr");
            user.setLastName("Ivanov");
            user.setBirthdate(LocalDate.of(1995, 8, 12));
            manager.persist(user);
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }

        // change data in table
        try {
            manager.getTransaction().begin();
            User user = manager.find(User.class, 7L);
            user.setFirstName("Petr");
            user.setLastName("Petrov");
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }

        // delete info in table
        try {
            manager.getTransaction().begin();
            User user = manager.find(User.class, 7L);
            manager.remove(user);
            manager.getTransaction().commit();

        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }

        // join
        /*User user = manager.find(User.class, 3L);
        System.out.println("User: " + user.getFirstName() + " " + user.getLastName());
        //System.out.println("Role: " + user.getRole().getName());
        System.out.println("Role: " + user.getRole().getId());

        Role role = manager.find(Role.class, 3L);
        List<User> users = role.getUser();

        for (User u : users) {
            System.out.println(u.getFirstName());
        }
        */
    }
}
