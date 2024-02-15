package com.example.dataManagerWebApp;

import com.example.ResourceClasses.Adress;
import com.example.ResourceClasses.Contact;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = AdressController.class)
public class AdressControllerTest {
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
    void testGetAllAdresses() throws Exception {
        List<Adress> adresses = PersonStorage.getAllAdresses();
        ResultActions request = mockMvc.perform(get("/adress")).andDo(print()).andExpect(status().isOk());
        List<Adress> actualAdresses = Arrays.stream(objectMapper.readValue(request.andReturn().getResponse().getContentAsString(),
                Adress[].class)).collect(Collectors.toList());
        assertEquals(adresses.size(), actualAdresses.size());
        for (int i = 0; i < adresses.size(); i++){
            assertEquals(adresses.get(i).getId(),actualAdresses.get(i).getId());
            assertEquals(adresses.get(i).getCity(),actualAdresses.get(i).getCity());
            assertEquals(adresses.get(i).getContacts().size(),actualAdresses.get(i).getContacts().size());
            List<String> keys = adresses.get(i).getContacts().keySet().stream().toList();
            List<String> actualKeys = actualAdresses.get(i).getContacts().keySet().stream().toList();
            for(int j = 0; j < adresses.get(i).getContacts().size();j++){
                assertEquals(adresses.get(i).getContacts().get(keys.get(j)).getId(),actualAdresses.get(i).getContacts().get(actualKeys.get(j)).getId());
                assertEquals(adresses.get(i).getContacts().get(keys.get(j)).getContactType(),actualAdresses.get(i).getContacts().get(actualKeys.get(j)).getContactType());
                assertEquals(adresses.get(i).getContacts().get(keys.get(j)).getContact(),actualAdresses.get(i).getContacts().get(actualKeys.get(j)).getContact());
            }
        }
    }

    @Test
    void testGetAdressByIdExisting() throws Exception {
        String id = PersonStorage.getAllAdresses().get(0).getId();
        Adress adress = PersonStorage.getAdressById(id);
        ResultActions request = mockMvc.perform(get("/adress/"+id)).andDo(print()).andExpect(status().isOk());
        Adress actualAdress = objectMapper.readValue(request.andReturn().getResponse().getContentAsString(), Adress.class);
        assertEquals(adress.getId(), actualAdress.getId());
        assertEquals(adress.getCity(), actualAdress.getCity());
    }

    @Test
    void testGetAdressByIdNotExisting() throws Exception {
        String id = "0";
        mockMvc.perform(get("/adress/"+id)).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void testCreateAdress() throws Exception {
        int numberOfAdressesBefore = PersonStorage.getAllAdresses().size();
        String id = "sampleId";
        Adress adress = new Adress(id,"Szolnok", true);
        Contact newContact = new Contact("c"+(PersonStorage.getAllContacts().size()+1), "telephone","+36201119999");
        adress.addContact(newContact);
        String jsonAdress = asJsonString(adress);
        ResultActions request = mockMvc.perform(post("/adress").contentType(MediaType.APPLICATION_JSON)
                .content(jsonAdress)).andDo(print()).andExpect(status().isOk());
        //.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        Adress actualAdress= objectMapper.readValue(request.andReturn().getResponse().getContentAsString(), Adress.class);
        //ID is reset in controller.
        assertEquals(numberOfAdressesBefore + 1, PersonStorage.getAllAdresses().size());
        assertNotEquals(id, actualAdress.getId());
        assertEquals(adress.getContacts().size(), actualAdress.getContacts().size());
    }

    @Test
    void testUpdateAdress() throws Exception {
        int sizeBeforeUpdate = PersonStorage.getAllAdresses().size();
        int nextId = sizeBeforeUpdate+1;
        String id = PersonStorage.getAllAdresses().get(0).getId();
        Adress adress = PersonStorage.getAdressById(id);
        Adress adressCopied = new Adress(adress.getId(),adress.getCity(),adress.isPermanentAdress());
        adressCopied.setContacts(adress.getContacts());
        adressCopied.setCity("New York");
        String jsonAdress = asJsonString(adressCopied);
        ResultActions request = mockMvc.perform(put("/adress").contentType(MediaType.APPLICATION_JSON)
                .content(jsonAdress)).andDo(print()).andExpect(status().isOk());
        //.andExpect(MockMvcResultMatchers.jsonPath("$.color").value("red"));
        Adress actualAdress = objectMapper.readValue(request.andReturn().getResponse().getContentAsString(), Adress.class);
        assertEquals(PersonStorage.getAllAdresses().size(), sizeBeforeUpdate);
        assertEquals(actualAdress.getId(), PersonStorage.getAdressById(id).getId());
        assertEquals(actualAdress.getCity(), PersonStorage.getAdressById(id).getCity());
        assertEquals("New York", actualAdress.getCity());
        assertEquals("New York", PersonStorage.getAdressById(id).getCity());
        assertEquals(200, request.andReturn().getResponse().getStatus());
    }

    @Test
    void testDeleteAdress() throws Exception {
        String id = PersonStorage.getAllAdresses().get(0).getId();
        Adress adress = PersonStorage.getAdressById(id);
        mockMvc.perform(delete("/adress/"+id)).andDo(print()).andExpect(status().isOk());
        List<Adress> adressList = PersonStorage.getAllAdresses();
        assertTrue(!adressList.contains(adress));
        assertFalse(adressList.contains(adress));
    }
}
