package edu.miu.cs.cs590.jobservice.Repositories;

import edu.miu.cs.cs590.jobservice.Models.Vacancy;
import edu.miu.cs.cs590.jobservice.Models.VacancyApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VacancyApplicationRepository extends JpaRepository<VacancyApplication, Long> {
    Optional<VacancyApplication>  findByVacancyAndJobSeekerId(Vacancy vacancy, Long jobSeekerId);
    List<VacancyApplication> findAllByVacancy(Vacancy vacancy);
    Long countByVacancy(Vacancy vacancy);

}
