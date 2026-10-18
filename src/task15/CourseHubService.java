package task15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class CourseHubService {

    private Map<String, Course> catalog = Map.of();

    public int importCourses(Reader source) throws InfrastructureException {
        Map<String, Course> loaded = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(source)) {
            String line;
            int recordNumber = 0;
            while ((line = reader.readLine()) != null) {
                recordNumber++;
                Course course = parse(recordNumber, line);
                loaded.put(course.getCode(), course);
            }
        } catch (IOException e) {
            throw new InfrastructureException("Cannot read course source", e);
        }
        catalog = loaded;
        return loaded.size();
    }

    private Course parse(int recordNumber, String line) {
        String[] parts = line.split(";");
        if (parts.length != 2) {
            throw new ValidationException("Record " + recordNumber + ": expected 2 fields");
        }
        try {
            return new Course(parts[0], Integer.parseInt(parts[1].trim()));
        } catch (NumberFormatException e) {
            throw new ValidationException("Record " + recordNumber + ": capacity is not a number", e);
        } catch (ValidationException e) {
            throw new ValidationException("Record " + recordNumber + ": " + e.getMessage(), e);
        }
    }

    public void enroll(String studentId, String rawCode) throws EnrollmentRefusedException {
        if (studentId == null || studentId.isBlank()) {
            throw new ValidationException("Student id is required");
        }
        String code = Course.normalizeCode(rawCode);
        Course course = catalog.get(code);
        if (course == null) {
            throw new EnrollmentRefusedException(ErrorCode.COURSE_NOT_FOUND, "Course not found: " + code);
        }
        course.enroll(studentId);
    }

    public Optional<Course> findCourse(String code) {
        return Optional.ofNullable(catalog.get(code));
    }
}
