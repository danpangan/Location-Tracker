package com.example.p000.tracker;

public class LoginResponse {

    private C_User user;

    public LoginResponse(C_User user) {
        this.user = user;
    }

    public C_User getUser() {
        return user;
    }

}
