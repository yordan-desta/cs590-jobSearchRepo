package edu.miu.cs.cs590.jobservice.Services;

import edu.miu.cs.cs590.jobservice.Exception.AppException;
import edu.miu.cs.cs590.jobservice.Models.Company;
import edu.miu.cs.cs590.jobservice.Repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService{

    CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    public Company update(Company company) {
        return companyRepository.save(company);
    }

    public void deleteById(Long id) {
         companyRepository.deleteById(id);
    }

    public void deleteAll(List<Company> companies) {
         companyRepository.deleteAll(companies);
    }

    public List<Company> findAll() {
        return  companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
      return  companyRepository.findById(id).orElseThrow(() -> new AppException("Company with id:"+ id + " doesn't exist"));
    }

    public Company findByUser(Long id) {
        return companyRepository.findCompanyByUserId(id);
    }
}
