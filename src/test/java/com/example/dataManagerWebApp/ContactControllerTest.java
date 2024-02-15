package com.example.dataManagerWebApp;


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
@ContextConfiguration(classes = ContactController.class)
public class ContactControllerTest {
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
    void testGetAllContacts() throws Exception {
        List<Contact> contacts = PersonStorage.getAllContacts();
        ResultActions request = mockMvc.perform(get("/contact")).andDo(print()).andExpect(status().isOk());
        List<Contact> actualContacts = Arrays.stream(objectMapper.readValue(request.andReturn().getResponse().getContentAsString(),
                Contact[].class)).collect(Collectors.toList());
        assertEquals(contacts.size(), actualContacts.size());
        for (int i = 0; i < contacts.size(); i++){
            assertEquals(contacts.get(i).getId(),actualContacts.get(i).getId());
            assertEquals(contacts.get(i).getContactType(),actualContacts.get(i).getContactType());
            assertEquals(contacts.get(i).getContact(),actualContacts.get(i).getContact());
        }
    }

    @Test
    void testGetContactByIdExisting() throws Exception {
        String id = PersonStorage.getAllContacts().get(0).getId();
        Contact contact = PersonStorage.getContactById(id);
        ResultActions request = mockMvc.perform(get("/contact/"+id)).andDo(print()).andExpect(status().isOk());
        Contact actualContact = objectMapper.readValue(request.andReturn().getResponse().getContentAsString(), Contact.class);
        assertEquals(contact.getId(),actualContact.getId());
        assertEquals(contact.getContactType(),actualContact.getContactType());
        assertEquals(contact.getContact(),actualContact.getContact());    }

    @Test
    void testGetContactByIdNotExisting() throws Exception {
        String id = "0";
        mockMvc.perform(get("/contact/"+id)).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void testCreateContact() throws Exception {
        int numberOfContactsBefore = PersonStorage.getAllContacts().size();
        String id = "sampleId";
        Contact contact = new Contact(id,"email", "email@email.com");
        String jsonContact = asJsonString(contact);
        ResultActions request = mockMvc.perform(post("/contact").contentType(MediaType.APPLICATION_JSON)
                .content(jsonContact)).andDo(print()).andExpect(status().isOk());
        //.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        Contact actualContact = objectMapper.readValue(request.andReturn().getResponse().getContentAsString(), Contact.class);
        //ID is reset in controller.
        assertEquals(numberOfContactsBefore + 1, PersonStorage.getAllContacts().size());
        assertNotEquals(id, actualContact.getId());
    }

    @Test
    void testUpdateContact() throws Exception {
        int sizeBeforeUpdate = PersonStorage.getAllContacts().size();
        int nextId = sizeBeforeUpdate+1;
        String id = PersonStorage.getAllContacts().get(0).getId();
        Contact contact = PersonStorage.getContactById(id);
        Contact contactCopied = new Contact(contact.getId(),contact.getContactType(), contact.getContact());
        contactCopied.setContactType("email");
        contactCopied.setContact("email@exampleemail.com");
        String jsonContact = asJsonString(contactCopied);
        ResultActions request = mockMvc.perform(put("/contact").contentType(MediaType.APPLICATION_JSON)
                .content(jsonContact)).andDo(print()).andExpect(status().isOk());
        //.andExpect(MockMvcResultMatchers.jsonPath("$.color").value("red"));
        Contact actualContact = objectMapper.readValue(request.andReturn().getResponse().getContentAsString(), Contact.class);
        assertEquals(PersonStorage.getAllContacts().size(), sizeBeforeUpdate);
        assertEquals(actualContact.getId(), PersonStorage.getContactById(id).getId());
        assertEquals(actualContact.getContactType(), PersonStorage.getContactById(id).getContactType());
        assertEquals(actualContact.getContact(), PersonStorage.getContactById(id).getContact());
        assertEquals("email", actualContact.getContactType());
        assertEquals("email", PersonStorage.getContactById(id).getContactType());
        assertEquals("email@exampleemail.com", actualContact.getContact());
        assertEquals("email@exampleemail.com", PersonStorage.getContactById(id).getContact());
        assertEquals(200, request.andReturn().getResponse().getStatus());
    }

    @Test
    void testDeleteContact() throws Exception {
        String id = PersonStorage.getAllContacts().get(0).getId();
        Contact contact = PersonStorage.getContactById(id);
        mockMvc.perform(delete("/contact/"+id)).andDo(print()).andExpect(status().isOk());
        List<Contact> contactList = PersonStorage.getAllContacts();
        assertTrue(!contactList.contains(contact));
        assertFalse(contactList.contains(contact));
    }
}
