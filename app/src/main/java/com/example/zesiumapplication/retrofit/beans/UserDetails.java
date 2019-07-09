package com.example.zesiumapplication.retrofit.beans;

import com.google.gson.annotations.SerializedName;

public class UserDetails {

    @SerializedName("user")
    private User user;

    @SerializedName("company")
    private Company company;

    public UserDetails() {

    }

    public UserDetails(User user, Company company) {
        this.user = user;
        this.company = company;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
