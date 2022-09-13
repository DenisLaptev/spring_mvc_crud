package com.alishev_course.spring_mvc.util;

import com.alishev_course.spring_mvc.dao.PersonDAO;
import com.alishev_course.spring_mvc.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(@Qualifier("PersonDAOJdbcTemplate") PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        //check if there is a person with the same email, as our person.
        if (personDAO.show(person.getEmail()).isPresent()) {

            //rejectValue(field_where_error,error_code,message)
            errors.rejectValue("email", "", "This email is already used");
        }

    }
}
