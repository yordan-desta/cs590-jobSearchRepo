package edu.miu.cs.cs590.jobservice.Controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.miu.cs.cs590.jobservice.Exception.AppException;
import edu.miu.cs.cs590.jobservice.Models.Company;
import edu.miu.cs.cs590.jobservice.Models.Vacancy;
import edu.miu.cs.cs590.jobservice.Models.VacancyApplication;
import edu.miu.cs.cs590.jobservice.Models.enums.VacancyStatus;
import edu.miu.cs.cs590.jobservice.Payload.Requests.ChangeVacancyStatusRequest;
import edu.miu.cs.cs590.jobservice.Payload.Requests.VacancyRequest;
import edu.miu.cs.cs590.jobservice.Payload.Response.ApiResponse;
import edu.miu.cs.cs590.jobservice.Security.CurrentUser;
import edu.miu.cs.cs590.jobservice.Security.UserPrincipal;
import edu.miu.cs.cs590.jobservice.Services.CompanyService;
import edu.miu.cs.cs590.jobservice.Services.ProducerService;
import edu.miu.cs.cs590.jobservice.Services.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/js/vacancy")
public class VacancyController {

    private final ProducerService producerService;

    @Autowired
    private  VacancyService vacancyService;
    @Autowired
    private  CompanyService companyService;

    public VacancyController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping("/{id}")
    public Vacancy getVacancy(@PathVariable Long id){
        return vacancyService.getVacancy(id);
    }


    @GetMapping("/applications/{id}")
    @PreAuthorize("hasRole('COMPANY')")
    public List<VacancyApplication> getVacancyApplications(@PathVariable Long id){

        List<VacancyApplication> r = new ArrayList<>();
        for(VacancyApplication model :vacancyService.getVacancyApplications(id)){
            r.add(model);
                System.out.println(model.getJobSeekerId());
        }

        return r;


        //return new ArrayList<VacancyApplication>();
       //return vacancyService.getVacancyApplications(id);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('COMPANY')")
    public List<Vacancy> getVacancies(@CurrentUser UserPrincipal currentUse){
        Company company = this.companyService.findByUser(currentUse.getId());
        if (company == null){
            throw new AppException("Do not have a company");
        }
        return vacancyService.getCompanyVacancies(company);
    }


    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public Vacancy saveVacancy(@RequestBody VacancyRequest vacancyRequest, @CurrentUser UserPrincipal currentUser) throws JsonProcessingException {
        Company company = this.companyService.findByUser(currentUser.getId());
        if (company == null){
            throw new AppException("Cannot post vaccancy against");
        }
        Vacancy vacancy = new Vacancy();
        vacancy.setJobDescription(vacancyRequest.getJobDescription());
        vacancy.setPostFromDate(vacancyRequest.getPostFromDate());
        vacancy.setPostToDate(vacancyRequest.getPostToDate());
        vacancy.setTitle(vacancyRequest.getTitle());
        vacancy.setSalaryRangFrom(vacancyRequest.getSalaryRangFrom());
        vacancy.setSalaryRangTo(vacancyRequest.getSalaryRangTo());
        vacancy.setLocation(vacancyRequest.getLocation());

        //Company company = this.companyService.findById(vacancyRequest.getCompanyId().longValue());

        vacancy.setCompany(company);


        vacancy = vacancyService.save(vacancy);
        ObjectMapper mapper = new ObjectMapper();
        producerService.sendMessage(mapper.writeValueAsString(vacancy));

        return vacancy;
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY')")
    public Vacancy updateVacancy(@PathVariable Long id,@RequestBody VacancyRequest vacancyRequest, @CurrentUser UserPrincipal currentUser){

        Company company = this.companyService.findByUser(currentUser.getId());
        if (company == null){
            throw new AppException("Cannot post vaccancy against");
        }
        return vacancyService.update(vacancyRequest,id,company);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<ApiResponse> deleteVacancy(@PathVariable Long id, @CurrentUser UserPrincipal currentUser){
        try{

            Company company = this.companyService.findByUser(currentUser.getId());
            if (company == null){
                throw new AppException("Cannot post vacancy against");
            }

            vacancyService.delete(id,company);
            return new ResponseEntity<>(
                    new ApiResponse(true, "Vacancy Deleted successfully"),
                    HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/approved")
    public List<Vacancy> getApprovedVacancies(){
        return vacancyService.getVacanciesByStatus(VacancyStatus.Published);
    }

    @PostMapping("/apply/{id}")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<ApiResponse> apply(@PathVariable Long id, @CurrentUser UserPrincipal currentUser){
        try{
            this.vacancyService.apply(id,currentUser.getId());

            return new ResponseEntity<>(
                    new ApiResponse(true, "Application Submitted successfully"),
                    HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/change-status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> approve(@PathVariable Long id, @RequestBody ChangeVacancyStatusRequest changeStatusRequest, @CurrentUser UserPrincipal currentUser){
        try{
            Vacancy vacancy = this.vacancyService.findById(id);
            vacancy.setVacancyStatus(changeStatusRequest.getStatus().toLowerCase().equals("published")?VacancyStatus.Published:VacancyStatus.Canceled);
            this.vacancyService.save(vacancy);
            return new ResponseEntity<>(
                    new ApiResponse(true, "Vacancy Status Updates successfully"),
                    HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/pending-approval/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Vacancy>  pendingApproval(){
        return vacancyService.getVacanciesByStatus(VacancyStatus.Draft);
    }
}
