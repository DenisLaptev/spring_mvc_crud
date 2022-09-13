package com.alishev_course.spring_mvc.dao;

import com.alishev_course.spring_mvc.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonDAO {

    List<Person> index();

    Person show(int id);

    Optional<Person> show(String email);

    void save(Person person);

    void update(int id, Person updatedPerson);

    void delete(int id);

    void testMultipleUpdate();

    void testBatchUpdate();
}

