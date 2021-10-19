package edu.miu.cs.cs590.jobservice.Payload.Requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdateCompanyRequest {

    @NotBlank
    @Size(min = 4, max = 40)
    private String street;

    @NotBlank
    @Size(min = 2, max = 40)
    private String city;

    @NotBlank
    @Size(min = 2, max = 40)
    private String state;

    private Long zipcode;


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getZipcode() {
        return zipcode;
    }

    public void setZipcode(Long zipcode) {
        this.zipcode = zipcode;
    }





}
