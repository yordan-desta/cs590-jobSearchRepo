package edu.miu.cs.cs590.accountservice.Repositories;

import edu.miu.cs.cs590.accountservice.Models.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
}
