package com.alishev_course.spring_mvc.dao;

import com.alishev_course.spring_mvc.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier("PersonDAOJdbcTemplate")
public class PersonDAOJdbcTemplate implements PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAOJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Person> index() {
//        return jdbcTemplate.query("SELECT * FROM Person", new PersonMapper());

        //For BeanPropertyRowMapper table column name=class field name
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    @Override
    public Person show(int id) {
//        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id}, new PersonMapper())
//                .stream().findAny().orElse(null);

        //For BeanPropertyRowMapper table column name=class field name
        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public Optional<Person> show(String email) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE email=?", new Object[]{email}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }

    @Override
    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(name, age, email, address) VALUES(?, ?, ?, ?)", person.getName(), person.getAge(), person.getEmail(), person.getAddress());
    }

    @Override
    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE Person SET name=?, age=?, email=?, address=? WHERE id=?",
                updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), updatedPerson.getAddress(), id);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }

    @Override
    public void testMultipleUpdate() {
        List<Person> people = create100People();

        long before = System.currentTimeMillis();

        for (Person person : people) {
            jdbcTemplate.update("INSERT INTO Person VALUES(?, ?, ?, ?, ?)",
                    person.getId(), person.getName(), person.getAge(), person.getEmail(), person.getAddress());
        }
        long after = System.currentTimeMillis();

        System.out.println("testMultipleUpdate --> Time: " + (after - before));
    }


    @Override
    public void testBatchUpdate() {
        List<Person> people = create100People();

        long before = System.currentTimeMillis();

        jdbcTemplate.batchUpdate("INSERT INTO Person VALUES(?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, people.get(i).getId());
                preparedStatement.setString(2, people.get(i).getName());
                preparedStatement.setInt(3, people.get(i).getAge());
                preparedStatement.setString(4, people.get(i).getEmail());
                preparedStatement.setString(5, people.get(i).getAddress());
            }

            @Override
            public int getBatchSize() {
                return people.size();
            }
        });

        long after = System.currentTimeMillis();

        System.out.println("testBatchUpdate --> Time: " + (after - before));
    }


    private List<Person> create100People() {
        List<Person> people = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            people.add(new Person(i, "Name" + i, 30, "test" + i + "@mail.ru", "some address"));
        }

        return people;
    }
}
