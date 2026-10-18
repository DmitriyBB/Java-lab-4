package task15;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CourseTest {

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource({"java-101, JAVA-101", "' SQL-200 ', SQL-200", "Db-001, DB-001"})
    void normalizesValidCode(String raw, String expected) {
        assertEquals(expected, Course.normalizeCode(raw));
    }

    @ParameterizedTest(name = "\"{0}\" -> ValidationException")
    @NullSource
    @ValueSource(strings = {"", "J-101", "JAVA101", "JAVA-12"})
    void rejectsInvalidCode(String raw) {
        assertThrows(ValidationException.class, () -> Course.normalizeCode(raw));
    }

    @ParameterizedTest(name = "capacity {0} -> ValidationException")
    @ValueSource(ints = {-1, 0, Course.MAX_CAPACITY + 1})
    void rejectsInvalidCapacity(int capacity) {
        assertThrows(ValidationException.class, () -> new Course("JAVA-101", capacity));
    }

    @ParameterizedTest(name = "capacity {0} is accepted")
    @ValueSource(ints = {1, Course.MAX_CAPACITY})
    void acceptsBoundaryCapacity(int capacity) {
        assertEquals(capacity, new Course("JAVA-101", capacity).getCapacity());
    }

    @Test
    void enrollSameStudentTwice_refusedAndCountUnchanged() throws Exception {
        Course course = new Course("JAVA-101", 5);
        course.enroll("s1");

        EnrollmentRefusedException e = assertThrows(EnrollmentRefusedException.class, () -> course.enroll("s1"));

        assertEquals(ErrorCode.ALREADY_ENROLLED, e.getCode());
        assertEquals(1, course.getEnrolledCount());
    }

    @Test
    void enrollIntoFullCourse_refusedAndCountUnchanged() throws Exception {
        Course course = new Course("JAVA-101", 1);
        course.enroll("s1");

        EnrollmentRefusedException e = assertThrows(EnrollmentRefusedException.class, () -> course.enroll("s2"));

        assertEquals(ErrorCode.COURSE_FULL, e.getCode());
        assertEquals(1, course.getEnrolledCount());
    }
}
