package com.alishev_course.spring_mvc.dao;

import com.alishev_course.spring_mvc.model.Person;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
@Qualifier("PersonDAOList")
public class PersonDAOList implements PersonDAO {

    private static int PEOPLE_COUNT;
    private List<Person> people;

    {
        people = new ArrayList<>();

        people.add(new Person(++PEOPLE_COUNT, "Tom", 24, "tom@mail.ru", "some address"));
        people.add(new Person(++PEOPLE_COUNT, "Bob", 52, "bob@mail.ru", "some address"));
        people.add(new Person(++PEOPLE_COUNT, "Mike", 18, "mike@yahoo.ru", "some address"));
        people.add(new Person(++PEOPLE_COUNT, "Katy", 34, "katy@mail.ru", "some address"));
    }

    @Override
    public List<Person> index() {
        return people;
    }

    @Override
    public Person show(int id) {
        return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }

    @Override
    public Optional<Person> show(String email) {
        return people.stream().filter(person -> person.getEmail().equals(email)).findAny();
    }

    @Override
    public void save(Person person) {
        person.setId(++PEOPLE_COUNT);
        people.add(person);
    }

    @Override
    public void update(int id, Person updatedPerson) {
        Person personToUpdate = show(id);
        personToUpdate.setName(updatedPerson.getName());
        personToUpdate.setAge(updatedPerson.getAge());
        personToUpdate.setEmail(updatedPerson.getEmail());
        personToUpdate.setAddress(updatedPerson.getAddress());
    }

    @Override
    public void delete(int id) {
        people.removeIf(p -> p.getId() == id);
    }

    @Override
    public void testMultipleUpdate() {

    }

    @Override
    public void testBatchUpdate() {

    }
}
