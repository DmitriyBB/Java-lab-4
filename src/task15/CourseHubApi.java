package task15;

import java.io.Reader;
import java.util.Objects;

public class CourseHubApi {

    private final CourseHubService service;

    public CourseHubApi(CourseHubService service) {
        this.service = Objects.requireNonNull(service, "service");
    }

    public Response importCourses(Reader source) {
        try {
            int count = service.importCourses(source);
            return Response.ok("Imported " + count + " courses");
        } catch (ValidationException e) {
            return Response.error(ErrorCode.INVALID_INPUT, e.getMessage());
        } catch (InfrastructureException e) {
            return Response.error(ErrorCode.SOURCE_UNAVAILABLE, "Course source is unavailable");
        }
    }

    public Response enroll(String studentId, String courseCode) {
        try {
            service.enroll(studentId, courseCode);
            return Response.ok("Enrolled");
        } catch (ValidationException e) {
            return Response.error(ErrorCode.INVALID_INPUT, e.getMessage());
        } catch (EnrollmentRefusedException e) {
            return Response.error(e.getCode(), e.getMessage());
        }
    }
}
