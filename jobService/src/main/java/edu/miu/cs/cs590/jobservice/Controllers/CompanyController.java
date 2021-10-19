package edu.miu.cs.cs590.jobservice.Controllers;

import edu.miu.cs.cs590.jobservice.Models.Company;
import edu.miu.cs.cs590.jobservice.Payload.Requests.CompanyRequestModel;
import edu.miu.cs.cs590.jobservice.Payload.Requests.UpdateCompanyRequest;
import edu.miu.cs.cs590.jobservice.Payload.Response.CompanyResponse;
import edu.miu.cs.cs590.jobservice.Security.CurrentUser;
import edu.miu.cs.cs590.jobservice.Security.UserPrincipal;
import edu.miu.cs.cs590.jobservice.Services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/company")
public class CompanyController {

    CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public CompanyResponse addCompany(@RequestBody CompanyRequestModel cm, BindingResult br, @CurrentUser UserPrincipal userPrincipal) {
        CompanyResponse cr = new CompanyResponse();
        System.out.println(cm);
        if (br.hasErrors()) {
            cr.setStatusCode(500);
            cr.setResponseMessage("Wrong information");
            return cr;
        }
        Company comp = new Company();
        comp.setCity(cm.getCity());
        comp.setState(cm.getState());
        comp.setZipcode(cm.getZipcode());
        comp.setStreet(cm.getStreet());

        comp.setUserId(userPrincipal.getId());
        comp = companyService.save(comp);

        cr.setStatusCode(200);
        cr.setResponseMessage("Company with an id " + comp.getId() + " is created succesfully!!");

        return cr;

    }

    @GetMapping("list")
    public List<Company> getAll() {
        return companyService.findAll();
    }

    @GetMapping("/{id}")
    public Company getCompanyInfo(@PathVariable Long id){
        return companyService.getCompanyById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY')")
    public Company updateCompanyInfo(@Valid @RequestBody UpdateCompanyRequest request, @PathVariable Long id){
        Company company =  companyService.getCompanyById(id);
        company.setCity(request.getCity());
        company.setStreet(request.getStreet());
        company.setState(request.getState());
        company.setZipcode(request.getZipcode());
        companyService.save(company);
        return company;
    }

    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable Long id){
        companyService.deleteById(id);
    }


}
