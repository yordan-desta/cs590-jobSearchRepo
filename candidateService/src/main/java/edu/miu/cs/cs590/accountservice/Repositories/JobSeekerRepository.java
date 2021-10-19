package edu.miu.cs.cs590.accountservice.Repositories;

import edu.miu.cs.cs590.accountservice.Models.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long>  {
    Optional<JobSeeker> findJobSeekerByUserId(Long userId);
}
