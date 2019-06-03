package com.example.wikunamu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "app_user")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;
    @Column(nullable = false)
    private String user_name;
    @Column(nullable = false)
    private String user_email;
    @Column(nullable = false)
    private String user_password;
    @Column
    private String contact_no;
    @Column(columnDefinition = "Varchar(100) default 'ADMIN'")
    private String user_role = "ADMIN";
    @JsonIgnore
    private String refresh_token;

    public AppUser() {
    }


    @Override
    public String toString() {
        return "AppUser{" + "\n" +
                "user_id=" + getUser_id() + "\n" +
                ", user_name='" + getUser_name() + '\'' + "\n" +
                ", user_email='" + getUser_email() + '\'' + "\n" +
                ", user_password='" + getUser_password() + '\'' + "\n" +
                ", contact_no='" + getContact_no() + '\'' + "\n" +
                ", user_role='" + getUser_role() + '\'' + "\n" +
                ", refresh_token='" + getRefresh_token() + '\'' + "\n" +
                '}';
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }
}
