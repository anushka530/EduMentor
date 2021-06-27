package com.edumentor.edumentor;

public class User {
    String  username, mail, password, tech,  userId ;

    public User(String username, String mail, String password, String tech, String userId) {
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.tech = tech;
        this.userId = userId;
    }
    public  User(String Name, String Tech, String email, String pass){
        this.username = Name;
        this.mail = email;
        this.password = pass;
        this.tech = Tech;


    }
    public  User(){

    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTech() {
        return tech;
    }

    public void setTech(String tech) {
        this.tech = tech;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
