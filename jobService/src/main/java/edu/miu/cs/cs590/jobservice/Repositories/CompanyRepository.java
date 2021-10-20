package edu.miu.cs.cs590.jobservice.Repositories;

import edu.miu.cs.cs590.jobservice.Models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Company findCompanyByUserId(Long userId);
}
