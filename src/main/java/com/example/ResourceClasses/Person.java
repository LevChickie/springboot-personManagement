package com.example.ResourceClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Person {
    private String id;
    private String name;
    //private int age;
    //private String nationality;
    private Adress permanentAdress;
    private Adress temporaryAdress;
    private Map<String, Contact> contacts;

    public Person(String id, String name){
        this.id = id;
        this.name = name;
        this.contacts = new HashMap<>();
    }
    public String getName() {
        return name;
    }
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Adress getPermanentAdress() {
        return permanentAdress;
    }

    public void setAdress(Adress adress) {
        if(adress.isPermanentAdress()){
            permanentAdress = adress;
        }
        else {
            temporaryAdress = adress;
        }
        this.permanentAdress = permanentAdress;
    }

    public Adress getTemporaryAdress() {
        return temporaryAdress;
    }


    public void addContact(Contact contact) {
        this.contacts.put(contact.getContactType(), contact);
    }

    public String writePerson(){
        return "Name: "+name+", ID: "+id+", Permament Adress: "+permanentAdress;
    }
}
