package task08;

import coursehub.Course;
import coursehub.CourseCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseCompleteHoursTest {

    @Test
    void completeHours_addsHoursToCompleted() {
        Course course = new Course(CourseCode.of("JAVA-101"), 40);

        course.completeHours(10);

        assertEquals(10, course.getCompletedHours());
    }
}
