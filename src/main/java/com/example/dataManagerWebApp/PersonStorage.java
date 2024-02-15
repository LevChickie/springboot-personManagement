package com.example.dataManagerWebApp;

import com.example.ResourceClasses.Adress;
import com.example.ResourceClasses.Contact;
import com.example.ResourceClasses.Person;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PersonStorage {
    private static Map<String, Person> people;
    private static Map<String, Adress> adresses;
    private static Map<String, Contact> contacts;

    public static List<Person> getAllPeople() {
        return new ArrayList<>(people.values());
    }

    public static Person getPersonById(String id) {
        return people.get(id);
    }

    public static void savePerson(Person person) {
        people.put(person.getId(), person);
    }

    public static void deletePerson(Person person) {
        people.remove(person.getId());
    }

    public static List<Adress> getAllAdresses() {
        return new ArrayList<>(adresses.values());
    }

    public static Adress getAdressById(String id) {
        return adresses.get(id);
    }

    public static void saveAdress(Adress adress) {
        adresses.put(adress.getId(), adress);
    }

    public static void deleteAdress(Adress adress) {
        adresses.remove(adress.getId());
    }

    public static List<Contact> getAllContacts() {
        return new ArrayList<>(contacts.values());
    }

    public static Contact getContactById(String id) {
        return contacts.get(id);
    }

    public static void saveContact(Contact contact) {
        contacts.put(contact.getId(), contact);
    }

    public static void deleteContact(Contact contact) {
        contacts.remove(contact.getId());
    }

    public static void createSampleStorage() {
        people = new HashMap<>();
        adresses = new HashMap<>();
        contacts = new HashMap<>();
        Person person1 = new Person("p1","D. David");
        Person person2 = new Person("p2","M. Mark");
        Person person3 = new Person("p3","K. Krisztian");

        Adress adress1 = new Adress("a1","Budapest", "Csango utca", "20/B", true);
        Adress adress2 = new Adress("a2", "Debrecen", "Kossuth utca", "20", false);
        Adress adress3 = new Adress("a3","Mezokovesd", "Szechenyi utca", "11", true);
        Adress adress4 = new Adress("a4","Gyor", "Szechenyi utca", "1", false);

        Contact contact1 = new Contact("c1","telephone","+36201111111");
        Contact contact2 = new Contact("c2","email","xy@exampleemail.com");
        Contact contact3 = new Contact("c3","telephone","+36202222222");
        Contact contact4 = new Contact("c4","email","yz@exampleemail.com");
        Contact contact5 = new Contact("c5","fax","fax number #1");
        Contact contact6 = new Contact("c6","fax","fax number #2");

        adress1.addContact(contact1);
        adress1.addContact(contact2);
        adress2.addContact(contact3);
        adress3.addContact(contact4);
        adress3.addContact(contact5);
        adress4.addContact(contact6);

        person1.setAdress(adress1);
        person1.setAdress(adress2);
        person2.setAdress(adress3);
        person3.setAdress(adress4);

        person1.addContact(contact1);
        person1.addContact(contact2);
        person1.addContact(contact3);
        person2.addContact(contact4);
        person2.addContact(contact5);
        person3.addContact(contact6);

        adresses.put(adress1.getId(),adress1);
        adresses.put(adress2.getId(),adress2);
        adresses.put(adress3.getId(),adress3);
        adresses.put(adress4.getId(),adress4);

        contacts.put(contact1.getId(), contact1);
        contacts.put(contact2.getId(), contact2);
        contacts.put(contact3.getId(), contact3);
        contacts.put(contact4.getId(), contact4);
        contacts.put(contact5.getId(), contact5);
        contacts.put(contact6.getId(), contact6);

        people.put(person1.getId(), person1);
        people.put(person2.getId(), person2);
        people.put(person3.getId(), person3);
    }
}
