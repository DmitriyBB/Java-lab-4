package task15;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseHubServiceTest {

    private final CourseHubService service = new CourseHubService();

    @Test
    void importValidSource_publishesCourses() throws Exception {
        int count = service.importCourses(FakeReader.of("JAVA-101;30\nSQL-200;20"));

        assertEquals(2, count);
        assertTrue(service.findCourse("JAVA-101").isPresent());
        assertTrue(service.findCourse("SQL-200").isPresent());
    }

    @Test
    void importValidSource_closesReader() throws Exception {
        FakeReader reader = FakeReader.of("JAVA-101;30");

        service.importCourses(reader);

        assertTrue(reader.closed);
    }

    @Test
    void importBadNumber_validationErrorWithRecordAndCause() {
        FakeReader reader = FakeReader.of("JAVA-101;30\nSQL-200;many");

        ValidationException e = assertThrows(ValidationException.class, () -> service.importCourses(reader));

        assertTrue(e.getMessage().startsWith("Record 2:"));
        assertInstanceOf(NumberFormatException.class, e.getCause());
        assertTrue(reader.closed);
    }

    @Test
    void importWrongFieldCount_validationError() {
        ValidationException e = assertThrows(ValidationException.class,
                () -> service.importCourses(FakeReader.of("JAVA-101")));

        assertEquals("Record 1: expected 2 fields", e.getMessage());
    }

    @Test
    void importInvalidCode_validationErrorKeepsDomainCause() {
        ValidationException e = assertThrows(ValidationException.class,
                () -> service.importCourses(FakeReader.of("bad;30")));

        assertInstanceOf(ValidationException.class, e.getCause());
    }

    @Test
    void failedImport_keepsPreviousCatalog() throws Exception {
        service.importCourses(FakeReader.of("JAVA-101;30"));

        assertThrows(ValidationException.class, () -> service.importCourses(FakeReader.of("SQL-200;x")));

        assertTrue(service.findCourse("JAVA-101").isPresent());
        assertTrue(service.findCourse("SQL-200").isEmpty());
    }

    @Test
    void readFailure_wrappedAsInfrastructureWithCause() {
        IOException io = new IOException("disk error");

        InfrastructureException e = assertThrows(InfrastructureException.class,
                () -> service.importCourses(FakeReader.failingOnRead(io)));

        assertSame(io, e.getCause());
    }

    @Test
    void closeFailure_infrastructureErrorAndNothingPublished() {
        FakeReader reader = FakeReader.failingOnClose("JAVA-101;30");

        assertThrows(InfrastructureException.class, () -> service.importCourses(reader));

        assertTrue(service.findCourse("JAVA-101").isEmpty());
    }

    @Test
    void badRecordAndCloseFailure_closeErrorSuppressed() {
        FakeReader reader = FakeReader.failingOnClose("JAVA-101;x");

        ValidationException e = assertThrows(ValidationException.class, () -> service.importCourses(reader));

        assertEquals(1, e.getSuppressed().length);
        assertEquals("close failed", e.getSuppressed()[0].getMessage());
    }

    @Test
    void enrollUnknownCourse_refusedWithNotFound() {
        EnrollmentRefusedException e = assertThrows(EnrollmentRefusedException.class,
                () -> service.enroll("s1", "JAVA-101"));

        assertEquals(ErrorCode.COURSE_NOT_FOUND, e.getCode());
    }

    @ParameterizedTest(name = "studentId \"{0}\" -> ValidationException")
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void enrollWithoutStudentId_validationError(String studentId) {
        assertThrows(ValidationException.class, () -> service.enroll(studentId, "JAVA-101"));
    }
}
