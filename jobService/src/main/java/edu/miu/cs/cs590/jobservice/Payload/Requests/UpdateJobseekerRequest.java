package edu.miu.cs.cs590.jobservice.Payload.Requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdateJobseekerRequest {

    @NotBlank
    @Size(min = 2, max = 40)
    private String bio;

    @NotBlank
    @Size(min = 2, max = 40)
    private String current_position;


    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCurrent_position() {
        return current_position;
    }

    public void setCurrent_position(String current_position) {
        this.current_position = current_position;
    }
}
