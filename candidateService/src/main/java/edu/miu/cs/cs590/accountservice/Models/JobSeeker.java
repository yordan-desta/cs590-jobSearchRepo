package edu.miu.cs.cs590.accountservice.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.javafaker.Faker;
import lombok.Data;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job_seekers")
@Data
public class JobSeeker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Current Position is required")
    private String currentPosition;

    @NotBlank(message = "Bio is required")
    private String bio;

    @OneToMany(mappedBy = "jobSeeker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Skill> jobSeekerSkills;

    @OneToMany(mappedBy = "jobSeeker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<WorkExperience> workExperiences;

    @OneToMany(mappedBy = "jobSeeker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Certificate> certificates;

    @OneToMany(mappedBy = "jobSeeker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Education> educations;


    //private User user;
    private Long userId;


    private String country;
    private boolean isRemotely;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isRemotely() {
        return isRemotely;
    }

    public JobSeeker() {
        Faker faker = new Faker();
        this.currentPosition = faker.job().position();
        this.country = faker.country().name();
        this.bio = faker.food().dish();
        this.workExperiences=new ArrayList<WorkExperience>();
    }


}
