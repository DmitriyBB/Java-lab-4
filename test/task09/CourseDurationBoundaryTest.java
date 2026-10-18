package task09;

import coursehub.Course;
import coursehub.CourseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Duration boundaries: one typical value (e.g. 40) passes even if MIN/MAX checks are wrong or missing")
class CourseDurationBoundaryTest {

    private static final CourseCode CODE = CourseCode.of("JAVA-101");

    @Test
    void belowMinimum_isRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Course(CODE, Course.MIN_HOURS - 1));
    }

    @Test
    void minimum_isAccepted() {
        Course course = new Course(CODE, Course.MIN_HOURS);

        assertEquals(Course.MIN_HOURS, course.getDurationHours());
    }

    @Test
    void maximum_isAccepted() {
        Course course = new Course(CODE, Course.MAX_HOURS);

        assertEquals(Course.MAX_HOURS, course.getDurationHours());
    }

    @Test
    void aboveMaximum_isRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Course(CODE, Course.MAX_HOURS + 1));
    }
}
