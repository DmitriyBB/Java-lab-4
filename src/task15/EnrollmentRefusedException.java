package task15;

public class EnrollmentRefusedException extends Exception {

    private final ErrorCode code;

    public EnrollmentRefusedException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }
}
