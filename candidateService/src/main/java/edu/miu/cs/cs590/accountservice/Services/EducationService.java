package edu.miu.cs.cs590.accountservice.Services;

import edu.miu.cs.cs590.accountservice.Exception.ResourceNotFoundException;
import edu.miu.cs.cs590.accountservice.Models.Education;
import edu.miu.cs.cs590.accountservice.Repositories.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationService {
    private EducationRepository educationRepository;

    @Autowired
    public EducationService(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    public List<Education> findAll() {
        return educationRepository.findAll();
    }

    public Education findById(long id) throws ResourceNotFoundException {
        return educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education", "ID", id));
    }

    public Education replaceEducation(Education newEducation, long id) {
        return educationRepository
                .findById(id)
                .map(education -> {
                    education.setDegree(newEducation.getDegree());
                    education.setFromDate(newEducation.getFromDate());
                    education.setToDate(newEducation.getToDate());
                    education.setGpa(newEducation.getGpa());
                    return this.educationRepository.save(education);
                })
                .orElseGet(() -> {
                    newEducation.setId(id);
                    return this.educationRepository.save(newEducation);
                });
    }

    public Education save(Education education) {
        educationRepository.save(education);
        return education;
    }

    public void delete(long id) {
        educationRepository.deleteById(id);
    }
    
}
