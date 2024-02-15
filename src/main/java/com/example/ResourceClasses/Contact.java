package com.example.ResourceClasses;



public class Contact {
    private String id;
    private String contactType;
    private String contact;

    public Contact (String id, String contactType, String contact){
        this.id = id;
        this.contactType = contactType;
        this.contact = contact;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getId() {
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
}
