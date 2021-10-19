package edu.miu.cs.cs590.accountservice.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.util.Date;

@Entity
@Table(name = "work_experiences")
@JsonIgnoreProperties("jobSeeker")
public class WorkExperience {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	public JobSeeker getJobSeeker() {
		return jobSeeker;
	}

	public void setJobSeeker(JobSeeker jobSeeker) {
		this.jobSeeker = jobSeeker;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_seeker_id", referencedColumnName = "id", nullable = false)
	private JobSeeker jobSeeker;
	public WorkExperience(){
		super();
	}
	public WorkExperience(String companyName,  Date fromDate,  Date toDate,String position) {
		super();
		this.companyName = companyName;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.position = position;
	}

	@NotBlank(message = "Company Name is required")
	private String companyName;

	@Past
	private Date fromDate;

	@FutureOrPresent
	private Date toDate;

	@NotBlank(message = "Position is required")
	private String position;

	@Positive
	private double lastSalary;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public double getLastSalary() {
		return lastSalary;
	}
	public void setLastSalary(double lastSalary) {
		this.lastSalary = lastSalary;
	}
}
