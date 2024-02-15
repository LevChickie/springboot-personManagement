package com.example.ResourceClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adress {
    private String id;
    private boolean permanentAdress;
    private String city;
    private String street;
    //Streetnumber choosen as a String while it works still with numbers, but sometimes houses have an additional
    private String streetNumber;

    private Map<String,Contact> contacts;

    public Adress(String id, String city, String street, String streetNumber,  boolean permanentAdress){
        this.id = id;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.permanentAdress = permanentAdress;
        this.contacts = new HashMap<>();
    }

    public boolean isPermanentAdress() {
        return permanentAdress;
    }

    public void setPermanentAdress(boolean permanentAdress) {
        this.permanentAdress = permanentAdress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public void addContact(Contact contact){
        contacts.put(contact.getContactType(),contact);
    }

    public void setContacts(Map<String,Contact> contacts){
        this.contacts = contacts;
    }
    public Map<String,Contact> getContacts(){
        return contacts;
    }

    public String getId() {
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }
}
