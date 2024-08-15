package exception;

public class InvalidParameterException extends IllegalArgumentException {
    private String parameter;
    private String reason;

    public InvalidParameterException(String parameter, String reason) {
        this.parameter = parameter;
        this.reason = reason;
    }


    public String getParameter() {
        return parameter;
    }

    public String getReason() {
        return reason;
    }
}
