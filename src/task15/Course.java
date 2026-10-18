package task15;

import java.util.HashSet;
import java.util.Set;

public class Course {

    public static final int MAX_CAPACITY = 500;

    private final String code;
    private final int capacity;
    private final Set<String> students = new HashSet<>();

    public Course(String code, int capacity) {
        if (capacity < 1 || capacity > MAX_CAPACITY) {
            throw new ValidationException("Capacity must be 1.." + MAX_CAPACITY + ", got " + capacity);
        }
        this.code = normalizeCode(code);
        this.capacity = capacity;
    }

    public static String normalizeCode(String raw) {
        if (raw == null) {
            throw new ValidationException("Course code is required");
        }
        String code = raw.trim().toUpperCase();
        if (!code.matches("[A-Z]{2,4}-\\d{3}")) {
            throw new ValidationException("Invalid course code: " + code);
        }
        return code;
    }

    public void enroll(String studentId) throws EnrollmentRefusedException {
        if (students.contains(studentId)) {
            throw new EnrollmentRefusedException(ErrorCode.ALREADY_ENROLLED, "Already enrolled in " + code);
        }
        if (students.size() >= capacity) {
            throw new EnrollmentRefusedException(ErrorCode.COURSE_FULL, "No seats in " + code);
        }
        students.add(studentId);
    }

    public String getCode() {
        return code;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getEnrolledCount() {
        return students.size();
    }
}
