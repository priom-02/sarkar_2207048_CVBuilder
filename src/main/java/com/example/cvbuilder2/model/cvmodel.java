package com.example.cvbuilder2.model;

public class cvmodel {

    public String name;
    public String email;
    public String phone;
    public String address;
    public String education;
    public String skills;
    public String experience;
    public String projects;


    public cvmodel(String name, String email, String phone, String address,
                   String education, String skills, String experience, String projects) {

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.education = education;
        this.skills = skills;
        this.experience = experience;
        this.projects = projects;
    }

    // Optional: default empty constructor
    public cvmodel() {}
}
