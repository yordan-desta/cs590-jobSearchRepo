package edu.miu.cs.cs590.accountservice.Controllers;

import edu.miu.cs.cs590.accountservice.Exception.ResourceNotFoundException;
import edu.miu.cs.cs590.accountservice.Models.JobSeeker;
import edu.miu.cs.cs590.accountservice.Payload.Requests.CreateJSRequest;
import edu.miu.cs.cs590.accountservice.Payload.Requests.UpdateJobseekerRequest;
import edu.miu.cs.cs590.accountservice.Services.JobSeekerService;
import edu.miu.cs.cs590.accountservice.Services.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/job-seeker")
public class JobSeekerController {

	private final ProducerService producerService;

	@Autowired
	private JobSeekerService jobSeekerService;

	public JobSeekerController(ProducerService producerService) {
		this.producerService = producerService;
	}


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
	JobSeeker newJobSeeker(@RequestBody CreateJSRequest jsRequest) {
		JobSeeker newJobSeeker = new JobSeeker();
		newJobSeeker.setBio(jsRequest.getBio());
		newJobSeeker.setCurrentPosition(jsRequest.getCurrentPosition());
		newJobSeeker = this.jobSeekerService.save(newJobSeeker);
		producerService.sendMessage(newJobSeeker.toString());
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

