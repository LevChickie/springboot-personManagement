package com.example.dataManagerWebApp;

import com.example.ResourceClasses.Adress;
import com.example.ResourceClasses.Contact;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContactController {
    @GetMapping("/contact")
    public List<Contact> getAllContacts(){
        return PersonStorage.getAllContacts();
    }
    @GetMapping("/contact/{id}")
    public Contact getContactById(@PathVariable String id){
        if (PersonStorage.getContactById(id) != null) {
            return PersonStorage.getContactById(id);
        }
        else {
            throw new ContactNotFoundException();
        }
    }

    @PostMapping("/contact")
    public Contact createContact(@RequestBody Contact newContact){
        newContact.setId("c"+(PersonStorage.getAllContacts().size()+1));
        PersonStorage.saveContact(newContact);
        return newContact;
    }

    @PutMapping("/contact")
    public Contact updateContact(@RequestBody Contact updatedContact){
        Contact contact = PersonStorage.getContactById(updatedContact.getId());
        contact.setContactType(updatedContact.getContactType());
        contact.setContact(updatedContact.getContact());
        PersonStorage.saveContact(contact);
        return contact;
    }

    @DeleteMapping("/contact/{id}")
    public Contact deleteContact(@PathVariable String id) {
        Contact contact = PersonStorage.getContactById(id);
        if (contact != null) {
            PersonStorage.deleteContact(PersonStorage.getContactById(id));
            return contact;
        }
        else {
            throw new ContactNotFoundException();

        }
    }
}
