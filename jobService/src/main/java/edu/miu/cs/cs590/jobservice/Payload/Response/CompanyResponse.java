package edu.miu.cs.cs590.jobservice.Payload.Response;

public class CompanyResponse {
    private int statusCode;
    private String responseMessage;

    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public String getResponseMessage() {
        return responseMessage;
    }
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
