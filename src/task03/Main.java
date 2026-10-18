package task03;

import java.io.IOException;

public class Main {

    static void saveEnrollment(String studentId, String courseId) throws IOException {
        throw new IOException("Disk is full");
    }

    static void enroll(String studentId, String courseId) throws EnrollmentRejectedException {
        try {
            saveEnrollment(studentId, courseId);
        } catch (IOException e) {
            throw new EnrollmentRejectedException(studentId, courseId,
                    EnrollmentRejectedException.ReasonCode.STORAGE_FAILURE, e);
        }
    }

    public static void main(String[] args) {
        try {
            enroll("student-42", "JAVA-101");
        } catch (EnrollmentRejectedException e) {
            System.out.println("toString:   " + e);
            System.out.println("message:    " + e.getMessage());
            System.out.println("reason:     " + e.getReasonCode());
            System.out.println("studentId:  " + e.getStudentId());
            System.out.println("courseId:   " + e.getCourseId());
            System.out.println("cause:      " + e.getCause());
        }
    }
}
