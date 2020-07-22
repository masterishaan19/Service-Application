package com.example.enrserviceapp;

class UserDefinitionClass{
    String email, type;

    public UserDefinitionClass(String email, String type) {
        this.email = email;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(String type) {
        this.type = type;
    }
}
