package task15;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CourseHubApiTest {

    private CourseHubApi api;

    @BeforeEach
    void setUp() {
        api = new CourseHubApi(new CourseHubService());
        api.importCourses(FakeReader.of("JAVA-101;1\nSQL-200;10"));
    }

    @Test
    void importValid_ok() {
        Response response = api.importCourses(FakeReader.of("GO-300;5"));

        assertEquals(ErrorCode.OK, response.code());
    }

    @Test
    void importMalformed_invalidInput() {
        Response response = api.importCourses(FakeReader.of("GO-300;five"));

        assertEquals(ErrorCode.INVALID_INPUT, response.code());
    }

    @Test
    void importReadFailure_sourceUnavailableWithoutInternalDetails() {
        Response response = api.importCourses(FakeReader.failingOnRead(new IOException("secret path /etc/x")));

        assertEquals(ErrorCode.SOURCE_UNAVAILABLE, response.code());
        assertFalse(response.message().contains("secret"));
    }

    @Test
    void enrollValid_ok() {
        assertEquals(ErrorCode.OK, api.enroll("s1", "sql-200").code());
    }

    @Test
    void enrollFullCourse_courseFull() {
        api.enroll("s1", "JAVA-101");

        assertEquals(ErrorCode.COURSE_FULL, api.enroll("s2", "JAVA-101").code());
    }

    @Test
    void enrollTwice_alreadyEnrolled() {
        api.enroll("s1", "SQL-200");

        assertEquals(ErrorCode.ALREADY_ENROLLED, api.enroll("s1", "SQL-200").code());
    }

    @Test
    void enrollInvalidCode_invalidInput() {
        assertEquals(ErrorCode.INVALID_INPUT, api.enroll("s1", "???").code());
    }

    @Test
    void enrollUnknownCourse_courseNotFound() {
        assertEquals(ErrorCode.COURSE_NOT_FOUND, api.enroll("s1", "GO-300").code());
    }

    @Test
    void programmingError_isNotHiddenByApi() {
        FakeReader buggy = FakeReader.withBug(new IllegalStateException("bug"));

        assertThrows(IllegalStateException.class, () -> api.importCourses(buggy));
    }

    @Test
    void nullService_failsFast() {
        assertThrows(NullPointerException.class, () -> new CourseHubApi(null));
    }
}
