package edu.miu.cs.cs590.accountservice.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.miu.cs.cs590.accountservice.Exception.ResourceNotFoundException;
import edu.miu.cs.cs590.accountservice.Models.JobSeeker;
import edu.miu.cs.cs590.accountservice.Payload.Requests.CreateJSRequest;
import edu.miu.cs.cs590.accountservice.Payload.Requests.UpdateJobseekerRequest;
import edu.miu.cs.cs590.accountservice.Security.CurrentUser;
import edu.miu.cs.cs590.accountservice.Security.UserPrincipal;
import edu.miu.cs.cs590.accountservice.Services.JobSeekerService;
import edu.miu.cs.cs590.accountservice.Services.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/ca/job-seeker")
public class JobSeekerController {

	@Autowired
	private  ProducerService producerService;

	@Autowired
	private JobSeekerService jobSeekerService;


	@GetMapping("/{id}")
	JobSeeker getJobSeeker(@PathVariable Long id) throws ResourceNotFoundException {
		return this.jobSeekerService.findById(id);
	}

	@GetMapping("/list")
	List<JobSeeker> all() {
		return this.jobSeekerService.findAll();
	}

	@PostMapping
	@PreAuthorize("hasRole('JOBSEEKER')")
	JobSeeker newJobSeeker(@RequestBody CreateJSRequest jsRequest,@CurrentUser UserPrincipal currentUser) throws JsonProcessingException {
		JobSeeker newJobSeeker = new JobSeeker();
		newJobSeeker.setBio(jsRequest.getBio());
		newJobSeeker.setCurrentPosition(jsRequest.getCurrentPosition());
		newJobSeeker.setUserId(currentUser.getId());
		newJobSeeker.setEmail(currentUser.getEmail());
		newJobSeeker = this.jobSeekerService.save(newJobSeeker);

		ObjectMapper mapper = new ObjectMapper();
		producerService.sendMessage(mapper.writeValueAsString(newJobSeeker));
		return newJobSeeker;
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('JOBSEEKER')")
	public JobSeeker UpdateJobSeekerInfo(@Valid @RequestBody UpdateJobseekerRequest request, @PathVariable Long id){
		JobSeeker js =  this.jobSeekerService.findById(id);
		js.setBio(request.getBio());
		js.setCurrentPosition(request.getCurrent_position());
		jobSeekerService.save(js);
		return js;
	}


	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('JOBSEEKER')")
	void deleteJobSeeker(@PathVariable Long id) {
		this.jobSeekerService.delete(id);
	}
}

