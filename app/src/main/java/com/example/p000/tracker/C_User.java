package com.example.p000.tracker;

public class C_User {

    private int id, empId;
    private String name, email, contact, auth;

    public C_User() {

        this.id = id;
        this.empId = empId;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.auth = auth;
    }

    public int getId() {
        return id;
    }

    public int getEmpId() {
        return empId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public String getAuth() {
        return auth;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
