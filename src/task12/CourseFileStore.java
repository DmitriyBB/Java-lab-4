package task12;

import coursehub.Course;
import coursehub.CourseCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CourseFileStore {

    private final Path directory;

    public CourseFileStore(Path directory) {
        this.directory = directory;
    }

    public Path save(Course course) throws IOException {
        Path file = directory.resolve(course.getCode().value() + ".txt");
        Files.writeString(file, course.getCode().value() + ";" + course.getDurationHours());
        return file;
    }

    public Course load(String code) throws IOException {
        String[] parts = Files.readString(directory.resolve(code + ".txt")).split(";");
        return new Course(CourseCode.of(parts[0]), Integer.parseInt(parts[1]));
    }
}
