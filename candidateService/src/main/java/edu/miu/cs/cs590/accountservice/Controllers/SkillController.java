package edu.miu.cs.cs590.accountservice.Controllers;

import edu.miu.cs.cs590.accountservice.Exception.ResourceNotFoundException;
import edu.miu.cs.cs590.accountservice.Models.JobSeeker;
import edu.miu.cs.cs590.accountservice.Models.Skill;
import edu.miu.cs.cs590.accountservice.Payload.Response.ApiResponse;
import edu.miu.cs.cs590.accountservice.Security.CurrentUser;
import edu.miu.cs.cs590.accountservice.Security.UserPrincipal;
import edu.miu.cs.cs590.accountservice.Services.JobSeekerService;
import edu.miu.cs.cs590.accountservice.Services.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/skills")
@PreAuthorize("hasRole('JOBSEEKER')")
public class SkillController {

    private final SkillService skillService;
    private final JobSeekerService jobSeekerService;

    @Autowired
    public SkillController(SkillService skillService, JobSeekerService jobSeekerService) {
        this.skillService = skillService;
        this.jobSeekerService = jobSeekerService;
    }

    @GetMapping("/")
    List<Skill> all() {
        return this.skillService.findAll();
    }

    @PostMapping("/")
    Skill newSkill(@RequestBody Skill newSkill,@CurrentUser UserPrincipal currentUser) {

        JobSeeker js = jobSeekerService.findByUserId(currentUser.getId());
        newSkill.setJobSeeker(js);
        return this.skillService.save(newSkill);
    }

    @GetMapping("/{id}")
    Skill getSkill(@PathVariable Long id) throws ResourceNotFoundException {
        return this.skillService.findById(id);
    }

    @PutMapping("/{id}")
    Skill replaceSkill(@RequestBody Skill newSkill, @PathVariable Long id) throws ResourceNotFoundException {
        return this.skillService.replaceSkill(newSkill, id);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse> deleteSkill(@PathVariable Long id) {
        try{
            Skill skill = skillService.findById(id);
        }catch (ResourceNotFoundException e){
            return new ResponseEntity(new ApiResponse(false, "Invalid skill Id"),
                    HttpStatus.BAD_REQUEST);
        }

        //todo check owner user

        this.skillService.delete(id);
        return new ResponseEntity(new ApiResponse(true, "Deleted"),
                HttpStatus.OK);

    }
}
