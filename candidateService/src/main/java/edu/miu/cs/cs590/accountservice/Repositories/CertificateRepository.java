package edu.miu.cs.cs590.accountservice.Repositories;


import edu.miu.cs.cs590.accountservice.Models.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
}

