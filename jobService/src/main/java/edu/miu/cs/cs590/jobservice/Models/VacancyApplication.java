package edu.miu.cs.cs590.jobservice.Models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import edu.miu.cs.cs590.jobservice.Models.enums.Category;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class VacancyApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;


    @JsonSerialize(using= LocalDateSerializer.class)
    @JsonDeserialize(using= LocalDateDeserializer.class)
    private LocalDate applyDate;

    @Max(value = 10)
    private int companyRating;
    private Category category;


    private Long jobSeekerId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Vacancy vacancy;

    public VacancyApplication() {
        Faker faker = new Faker();
        this.companyRating = faker.number().numberBetween(0, 10);
        category = Category.New;
        applyDate = LocalDate.now();
    }

}
