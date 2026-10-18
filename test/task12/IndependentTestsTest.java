package task12;

import coursehub.Course;
import coursehub.CourseCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.Random.class)
class IndependentTestsTest {

    @TempDir
    Path tempDir;

    private Course course;
    private CourseFileStore store;

    @BeforeEach
    void setUp() {
        course = new Course(CourseCode.of("JAVA-101"), 40);
        store = new CourseFileStore(tempDir);
    }

    @Test
    void completeHours_onFreshCourse() {
        course.completeHours(5);

        assertEquals(5, course.getCompletedHours());
    }

    @Test
    void newCourse_hasNoCompletedHours() {
        assertEquals(0, course.getCompletedHours());
    }

    @Test
    void save_createsFileInGivenDirectory() throws IOException {
        Path file = store.save(course);

        assertTrue(Files.exists(file));
        assertEquals(tempDir, file.getParent());
    }

    @Test
    void saveThenLoad_returnsSameData() throws IOException {
        store.save(course);

        Course loaded = store.load("JAVA-101");

        assertEquals(course.getCode(), loaded.getCode());
        assertEquals(course.getDurationHours(), loaded.getDurationHours());
    }

    @Test
    void toString_containsCode() {
        assertTrue(course.toString().contains("JAVA-101"));
    }
}
