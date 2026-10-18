package task03;

public class EnrollmentRejectedException extends Exception {

    public enum ReasonCode { COURSE_FULL, COURSE_CLOSED, PREREQUISITE_MISSING, ALREADY_ENROLLED, STORAGE_FAILURE }

    private final String studentId;
    private final String courseId;
    private final ReasonCode reasonCode;

    public EnrollmentRejectedException(String studentId, String courseId, ReasonCode reasonCode) {
        this(studentId, courseId, reasonCode, null);
    }

    public EnrollmentRejectedException(String studentId, String courseId, ReasonCode reasonCode, Throwable cause) {
        super("Enrollment rejected: " + reasonCode, cause);
        this.studentId = studentId;
        this.courseId = courseId;
        this.reasonCode = reasonCode;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public ReasonCode getReasonCode() {
        return reasonCode;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[reason=" + reasonCode + "]";
    }
}
