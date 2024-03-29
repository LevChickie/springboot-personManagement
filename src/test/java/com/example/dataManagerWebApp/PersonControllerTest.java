package com.example.dataManagerWebApp;

import com.example.ResourceClasses.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = PersonController.class)
public class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        PersonStorage.createSampleStorage();
    }

    @Test
    void testGetAllPeople() throws Exception {
        List<Person> people = PersonStorage.getAllPeople();
        ResultActions request = mockMvc.perform(get("/person")).andDo(print()).andExpect(status().isOk());
        List<Person> actualPeople = Arrays.stream(objectMapper.readValue(request.andReturn().getResponse().getContentAsString(),
                Person[].class)).collect(Collectors.toList());
        assertEquals(people.size(), actualPeople.size());
        for (int i = 0; i < people.size(); i++){

            assertEquals(people.get(i).getId(),actualPeople.get(i).getId());
            assertEquals(people.get(i).getName(),actualPeople.get(i).getName());

            //Test if the adresses correct that belong to the person
            if(people.get(i).getPermanentAdress() != null){
                assertEquals(people.get(i).getPermanentAdress().getCity(),actualPeople.get(i).getPermanentAdress().getCity());
                assertEquals(people.get(i).getPermanentAdress().getStreet(),actualPeople.get(i).getPermanentAdress().getStreet());
                assertEquals(people.get(i).getPermanentAdress().getStreetNumber(),actualPeople.get(i).getPermanentAdress().getStreetNumber());

            }
            if(people.get(i).getTemporaryAdress() != null){
                assertEquals(people.get(i).getTemporaryAdress().getCity(),actualPeople.get(i).getTemporaryAdress().getCity());
                assertEquals(people.get(i).getTemporaryAdress().getStreet(),actualPeople.get(i).getTemporaryAdress().getStreet());
                assertEquals(people.get(i).getTemporaryAdress().getStreetNumber(),actualPeople.get(i).getTemporaryAdress().getStreetNumber());

            }
        }
    }

    @Test
    void testGetPersonByIdExisting() throws Exception {
        String id = PersonStorage.getAllPeople().get(0).getId();
        Person person = PersonStorage.getPersonById(id);
        ResultActions request = mockMvc.perform(get("/person/"+id)).andDo(print()).andExpect(status().isOk());
        Person actualPerson = objectMapper.readValue(request.andReturn().getResponse().getContentAsString(), Person.class);

        assertEquals(person.getId(), actualPerson.getId());
        assertEquals(person.getName(), actualPerson.getName());

        //Test if the adresses correct that belong to the person
        if(person.getPermanentAdress() != null){
            assertEquals(person.getPermanentAdress().getCity(),actualPerson.getPermanentAdress().getCity());
            assertEquals(person.getPermanentAdress().getStreet(),actualPerson.getPermanentAdress().getStreet());
            assertEquals(person.getPermanentAdress().getStreetNumber(),actualPerson.getPermanentAdress().getStreetNumber());

        }
        if(person.getTemporaryAdress() != null){
            assertEquals(person.getTemporaryAdress().getCity(),actualPerson.getTemporaryAdress().getCity());
            assertEquals(person.getTemporaryAdress().getStreet(),actualPerson.getTemporaryAdress().getStreet());
            assertEquals(person.getTemporaryAdress().getStreetNumber(),actualPerson.getTemporaryAdress().getStreetNumber());

        }
    }

    @Test
    void testGetPersonByIdNotExisting() throws Exception {
        String id = "0";
        mockMvc.perform(get("/person/"+id)).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void testCreatePerson() throws Exception {
        int numberOfPeopleBefore = PersonStorage.getAllPeople().size();
        String id = "sampleId";
        Person person = new Person(id,"Mora Ferenc");
        String jsonPerson = asJsonString(person);
        ResultActions request = mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                .content(jsonPerson)).andDo(print()).andExpect(status().isOk());
        //.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        Person actualPerson = objectMapper.readValue(request.andReturn().getResponse().getContentAsString(), Person.class);
        //ID is reset in controller.
        assertEquals(numberOfPeopleBefore + 1, PersonStorage.getAllPeople().size());
        assertNotEquals(id, actualPerson.getId());
    }

    @Test
    void testUpdatePerson() throws Exception {
        int sizeBeforeUpdate = PersonStorage.getAllPeople().size();
        int nextId = sizeBeforeUpdate+1;
        String id = PersonStorage.getAllPeople().get(0).getId();
        Person person = PersonStorage.getPersonById(id);
        Person personCopied = new Person(person.getId(),person.getName());
        personCopied.setName("John Fawkes");
        String jsonPerson = asJsonString(personCopied);
        ResultActions request = mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON)
                .content(jsonPerson)).andDo(print()).andExpect(status().isOk());
        Person actualPerson = objectMapper.readValue(request.andReturn().getResponse().getContentAsString(), Person.class);
        assertEquals(PersonStorage.getAllPeople().size(), sizeBeforeUpdate);
        assertEquals(actualPerson.getId(), PersonStorage.getPersonById(id).getId());
        assertEquals(actualPerson.getName(), PersonStorage.getPersonById(id).getName());
        //Check if the values stored are the updated values
        assertEquals("John Fawkes", actualPerson.getName());
        assertEquals("John Fawkes", PersonStorage.getPersonById(id).getName());

        //Test if the adresses correct that belong to the person
        if(actualPerson.getPermanentAdress() != null){
            assertEquals(actualPerson.getPermanentAdress().getCity(),PersonStorage.getPersonById(id).getPermanentAdress().getCity());
            assertEquals(actualPerson.getPermanentAdress().getStreet(),PersonStorage.getPersonById(id).getPermanentAdress().getStreet());
            assertEquals(actualPerson.getPermanentAdress().getStreetNumber(),PersonStorage.getPersonById(id).getPermanentAdress().getStreetNumber());

        }
        if(actualPerson.getTemporaryAdress() != null){
            assertEquals(actualPerson.getTemporaryAdress().getCity(),PersonStorage.getPersonById(id).getTemporaryAdress().getCity());
            assertEquals(actualPerson.getTemporaryAdress().getStreet(),PersonStorage.getPersonById(id).getTemporaryAdress().getStreet());
            assertEquals(actualPerson.getTemporaryAdress().getStreetNumber(),PersonStorage.getPersonById(id).getTemporaryAdress().getStreetNumber());

        }

        assertEquals(200, request.andReturn().getResponse().getStatus());
    }

    @Test
    void testDeletePerson() throws Exception {
        String id = PersonStorage.getAllPeople().get(0).getId();
        Person person = PersonStorage.getPersonById(id);
        mockMvc.perform(delete("/person/"+id)).andDo(print()).andExpect(status().isOk());
        List<Person> personList = PersonStorage.getAllPeople();
        assertTrue(!personList.contains(person));
        assertFalse(personList.contains(person));
    }

}
