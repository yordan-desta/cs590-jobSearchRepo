package edu.miu.cs.cs590.jobservice.Services;

import edu.miu.cs.cs590.jobservice.Exception.AppException;
import edu.miu.cs.cs590.jobservice.Exception.ResourceNotFoundException;
import edu.miu.cs.cs590.jobservice.Models.Company;
import edu.miu.cs.cs590.jobservice.Models.Vacancy;
import edu.miu.cs.cs590.jobservice.Models.VacancyApplication;
import edu.miu.cs.cs590.jobservice.Models.enums.VacancyStatus;
import edu.miu.cs.cs590.jobservice.Payload.Requests.VacancyRequest;
import edu.miu.cs.cs590.jobservice.Repositories.VacancyApplicationRepository;
import edu.miu.cs.cs590.jobservice.Repositories.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VacancyService {

    VacancyRepository vacancyRepository;
    VacancyApplicationRepository vacancyApplicationRepository;

    @Autowired
    public VacancyService(VacancyRepository vacancyRepository,VacancyApplicationRepository vacancyApplicationRepository) {
        this.vacancyRepository = vacancyRepository;
        this.vacancyApplicationRepository = vacancyApplicationRepository;
    }

    public Vacancy getVacancy(Long id) {
        return vacancyRepository.findById(id).orElseThrow(() -> new AppException("Vacancy with id:"+ id + " doesn't exist"));
    }

    public Vacancy save(Vacancy vacancy) {
        return vacancyRepository.save(vacancy);
    }

    public void delete(Long id,Company company) {
        Optional<Vacancy> vacancyOp = vacancyRepository.findByCompanyAndId(company,id);
        if(vacancyOp.isPresent()){
            Vacancy vacancy = vacancyOp.get();
            Long applicationCant = vacancyApplicationRepository.countByVacancy(vacancy);
            if(applicationCant == 0){
                vacancyRepository.deleteById(id);
            }else{
                throw new AppException("Can not delete Vacancy has applications")  ;
            }
        }else{
            throw new ResourceNotFoundException("Vacancy", "id", id);
        }

    }

    public List<Vacancy> getAllVacancies() {
        return vacancyRepository.findAll();
    }

    public Vacancy update(VacancyRequest vacancyRequest, Long id,Company company) {
        Optional<Vacancy> vacancy = vacancyRepository.findByCompanyAndId(company,id);
        if(vacancy.isPresent()){
            Vacancy vacancy1 = vacancy.get();
            vacancy1.setJobDescription(vacancyRequest.getJobDescription());
            vacancy1.setPostFromDate(vacancyRequest.getPostFromDate());
            vacancy1.setPostToDate(vacancyRequest.getPostToDate());
            vacancy1.setTitle(vacancyRequest.getTitle());
            vacancy1.setSalaryRangFrom(vacancyRequest.getSalaryRangFrom());
            vacancy1.setSalaryRangTo(vacancyRequest.getSalaryRangTo());
            vacancy1.setLocation(vacancyRequest.getLocation());
            return vacancyRepository.save(vacancy1);
        }else{
            throw new ResourceNotFoundException("Vacancy", "id", id);
        }
    }

    public Vacancy findById(Long id) {
        Optional<Vacancy> vacancy = this.vacancyRepository.findById(id);
        if(vacancy.isPresent()){
            return vacancy.get();
        }
        throw new ResourceNotFoundException(Vacancy.class.toString(),"id",id);
    }


    public void apply(Long vacancyId,Long userId){
        Long jobseekerId = userId;
        Vacancy vacancy = this.findById(vacancyId);
        Optional<VacancyApplication> vao = this.vacancyApplicationRepository.findByVacancyAndJobSeekerId(vacancy,userId);
        if(vao.isPresent()){
            throw new AppException("Already applied");
        }else{
            VacancyApplication vacancyApplication = new VacancyApplication();
            vacancyApplication.setJobSeekerId(jobseekerId);
            vacancyApplication.setVacancy(vacancy);
            this.vacancyApplicationRepository.save(vacancyApplication);
        }
    }

    public List<Vacancy> getCompanyVacancies(Company company) {
        return vacancyRepository.findAllByCompany(company);
    }

    public List<Vacancy> getVacanciesByStatus(VacancyStatus vacancyStatus) {
        return vacancyRepository.findAllByVacancyStatus(vacancyStatus);
    }

    public List<VacancyApplication> getVacancyApplications(Long id) {
        Vacancy vacancy = this.findById(id);
        return vacancyApplicationRepository.findAllByVacancy(vacancy);
    }
}
