package edu.miu.cs.cs590.accountservice.Services;


import edu.miu.cs.cs590.accountservice.Exception.ResourceNotFoundException;
import edu.miu.cs.cs590.accountservice.Models.Certificate;
import edu.miu.cs.cs590.accountservice.Repositories.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateService {
    private CertificateRepository certificateRepository;

    @Autowired
    public CertificateService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public List<Certificate> findAll() {
        return certificateRepository.findAll();
    }

    public Certificate findById(long id) throws ResourceNotFoundException {
        return certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "ID", id));
    }

    public Certificate replaceCertificate(Certificate newCertificate, long id) {
        return certificateRepository
                .findById(id)
                .map(certificate -> {
                    certificate.setExpirationDate(newCertificate.getExpirationDate());
                    certificate.setIssueDate(newCertificate.getIssueDate());
                    certificate.setIssuedBy(newCertificate.getIssuedBy());
                    certificate.setName(newCertificate.getName());
                    return this.certificateRepository.save(certificate);
                })
                .orElseGet(() -> {
                    newCertificate.setId(id);
                    return this.certificateRepository.save(newCertificate);
                });
    }

    public Certificate save(Certificate certificate) {
        certificateRepository.save(certificate);
        return certificate;
    }

    public void delete(long id) {
        certificateRepository.deleteById(id);
    }

}
