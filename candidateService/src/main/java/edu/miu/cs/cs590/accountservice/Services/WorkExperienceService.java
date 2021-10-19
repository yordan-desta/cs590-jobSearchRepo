package edu.miu.cs.cs590.accountservice.Services;

import edu.miu.cs.cs590.accountservice.Models.WorkExperience;
import edu.miu.cs.cs590.accountservice.Repositories.WorkExperienceRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkExperienceService {
    private WorkExperienceRepository _WorkExperienceRepository;

    @Autowired
    public WorkExperienceService(WorkExperienceRepository workExperienceRepository) {
        this._WorkExperienceRepository = workExperienceRepository;
    }

    public WorkExperienceRepository get_WorkExperienceRepository() {
        return _WorkExperienceRepository;
    }

    public List<WorkExperience> findAll() {
        return _WorkExperienceRepository.findAll();
    }

    public List<WorkExperience> findAllByUserId(Long userId) {
        return _WorkExperienceRepository.findAllByUserId(userId);
    }

    public WorkExperience findById(long id) throws NotFoundException {
        return _WorkExperienceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Work Experience with id: " + id + " not found"));
    }

    public WorkExperience replaceWorkExperience(WorkExperience newWorkExperience, long id) {
        return _WorkExperienceRepository
                .findById(id)
                .map(workExperience -> {
                    workExperience.setCompanyName(newWorkExperience.getCompanyName());
                    workExperience.setPosition(newWorkExperience.getPosition());
                    workExperience.setFromDate(newWorkExperience.getFromDate());
                    workExperience.setLastSalary(newWorkExperience.getLastSalary());
                    workExperience.setToDate(newWorkExperience.getToDate());
                    return this._WorkExperienceRepository.save(workExperience);
                })
                .orElseGet(() -> {
                    newWorkExperience.setId(id);
                    return this._WorkExperienceRepository.save(newWorkExperience);
                });
    }

    public WorkExperience save(WorkExperience workExperience) {
        _WorkExperienceRepository.save(workExperience);
        return workExperience;
    }

    public void delete(long id) {
        _WorkExperienceRepository.deleteById(id);
    }
}
