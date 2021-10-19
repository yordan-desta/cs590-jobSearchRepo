package edu.miu.cs.cs590.accountservice.Controllers;

import edu.miu.cs.cs590.accountservice.Exception.ResourceNotFoundException;
import edu.miu.cs.cs590.accountservice.Models.Education;
import edu.miu.cs.cs590.accountservice.Models.JobSeeker;
import edu.miu.cs.cs590.accountservice.Payload.Response.ApiResponse;
import edu.miu.cs.cs590.accountservice.Security.CurrentUser;
import edu.miu.cs.cs590.accountservice.Security.UserPrincipal;
import edu.miu.cs.cs590.accountservice.Services.EducationService;
import edu.miu.cs.cs590.accountservice.Services.JobSeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/educations")
@PreAuthorize("hasRole('JOBSEEKER')")
public class EducationController {

    private final EducationService educationService;
    private final JobSeekerService jobSeekerService;

    @Autowired
    public EducationController(EducationService educationService, JobSeekerService jobSeekerService) {
        this.educationService = educationService;
        this.jobSeekerService = jobSeekerService;
    }

    @GetMapping("/")
    List<Education> all() {
        return this.educationService.findAll();
    }

    @PostMapping("/")
    Education newEducation(@RequestBody Education newEducation,@CurrentUser UserPrincipal currentUser) {

        JobSeeker js = jobSeekerService.findByUserId(currentUser.getId());
        newEducation.setJobSeeker(js);
        return this.educationService.save(newEducation);
    }

    @GetMapping("/{id}")
    Education getEducation(@PathVariable Long id) throws ResourceNotFoundException {
        return this.educationService.findById(id);
    }

    @PutMapping("/{id}")
    Education replaceEducation(@RequestBody Education newEducation, @PathVariable Long id) throws ResourceNotFoundException {
        return this.educationService.replaceEducation(newEducation, id);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse> deleteEducation(@PathVariable Long id) {
        try{
            Education education = educationService.findById(id);
        }catch (ResourceNotFoundException e){
            return new ResponseEntity(new ApiResponse(false, "Invalid education Id"),
                    HttpStatus.BAD_REQUEST);
        }

        this.educationService.delete(id);
        return new ResponseEntity(new ApiResponse(true, "Deleted"),
                HttpStatus.OK);

    }
}
