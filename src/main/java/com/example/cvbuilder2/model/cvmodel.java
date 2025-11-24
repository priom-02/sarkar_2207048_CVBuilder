package com.example.cvbuilder2.model;

public class cvmodel {

    private long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String education;
    private String skills;
    private String experience;
    private String projects;

    public cvmodel() {}

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



    public long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getEducation() { return education; }
    public String getSkills() { return skills; }
    public String getExperience() { return experience; }
    public String getProjects() { return projects; }



    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setEducation(String education) { this.education = education; }
    public void setSkills(String skills) { this.skills = skills; }
    public void setExperience(String experience) { this.experience = experience; }
    public void setProjects(String projects) { this.projects = projects; }
}
