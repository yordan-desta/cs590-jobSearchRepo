package edu.miu.cs.cs590.jobservice.Models;

import edu.miu.cs.cs590.jobservice.Models.enums.Category;
import edu.miu.cs.cs590.jobservice.Models.enums.VacancyStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.javafaker.Faker;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "vacancy")
@Data
@JsonIgnoreProperties("vacancyApplications")
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String location;
    @Lob
    private String jobDescription;
    private LocalDate postFromDate;
    private LocalDate postToDate;
    private double salaryRangFrom;
    private double salaryRangTo;
    private VacancyStatus vacancyStatus;

    public Vacancy() {
        setVacancyStatus(VacancyStatus.Draft);
    }

    public Vacancy(VacancyStatus vacancyStatus, edu.miu.cs.cs590.jobservice.Models.Company company, int year, int month) {
        this.vacancyStatus = vacancyStatus;
        this.company = company;
        Faker faker = new Faker();
        this.title = faker.job().title();
        this.jobDescription = faker.ancient().titan();
        this.postFromDate = LocalDateTime.now().withYear(year).toLocalDate();
        this.postToDate = LocalDateTime.now().withMonth(month).toLocalDate();
    }

    @OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "vacancy")
    private List<edu.miu.cs.cs590.jobservice.Models.VacancyApplication> vacancyApplications;


    @ManyToOne
    private edu.miu.cs.cs590.jobservice.Models.Company company;


}
