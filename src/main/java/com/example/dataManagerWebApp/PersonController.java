package com.example.dataManagerWebApp;

import com.example.ResourceClasses.Person;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {
    @GetMapping("/")
    public String index(){
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/person")
    public List<Person> getAllPeople(){
        return PersonStorage.getAllPeople();
    }
    @GetMapping("/person/{id}")
    public Person getPersonById(@PathVariable String id){
        if (PersonStorage.getPersonById(id) != null) {
            return PersonStorage.getPersonById(id);
        }
        else {
            throw new PersonNotFoundException();
        }
    }

    @PostMapping("/person")
    public Person createPerson(@RequestBody Person newPerson){
        newPerson.setId("p"+(PersonStorage.getAllPeople().size()+1));
        PersonStorage.savePerson(newPerson);
        return newPerson;
    }

    @PutMapping("/person")
    public Person updatePerson(@RequestBody Person updatedPerson){
        Person person = PersonStorage.getPersonById(updatedPerson.getId());
        person.setName(updatedPerson.getName());
        if(updatedPerson.getPermanentAdress() != null){
            person.setAdress(updatedPerson.getPermanentAdress());
        }
        if(updatedPerson.getTemporaryAdress() != null){
            person.setAdress(updatedPerson.getTemporaryAdress());
        }
        PersonStorage.savePerson(person);
        return person;
    }

    @DeleteMapping("/person/{id}")
    public Person deletePerson(@PathVariable String id) {
        Person person = PersonStorage.getPersonById(id);
        if (person != null) {
            PersonStorage.deletePerson(PersonStorage.getPersonById(id));
            return person;
        }
        else {
            throw new PersonNotFoundException();
        }
    }


}
