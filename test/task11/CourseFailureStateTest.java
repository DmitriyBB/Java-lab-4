package task11;

import coursehub.Course;
import coursehub.CourseCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CourseFailureStateTest {

    @Test
    void completeMoreThanRemaining_throwsAndKeepsState() {
        Course course = new Course(CourseCode.of("JAVA-101"), 10);
        course.completeHours(8);

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> course.completeHours(5));

        assertEquals("Cannot complete 5 hours, remaining 2", e.getMessage());
        assertEquals(8, course.getCompletedHours());
        assertEquals(2, course.remainingHours());
    }

    @Test
    void completeNegativeHours_throwsAndKeepsState() {
        Course course = new Course(CourseCode.of("JAVA-101"), 10);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> course.completeHours(-3));

        assertEquals("Hours must be positive, got -3", e.getMessage());
        assertEquals(0, course.getCompletedHours());
    }
}
