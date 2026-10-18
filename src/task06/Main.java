package task06;

import java.util.ArrayList;
import java.util.List;

public class Main {

    record Course(String code, int hours) {}

    static Course parseLine(String line) {
        String[] parts = line.split(";");
        return new Course(parts[0].trim(), Integer.parseInt(parts[1].trim()));
    }

    static List<Course> importCourses(List<String> lines) throws CourseImportException {
        List<Course> result = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            try {
                result.add(parseLine(lines.get(i)));
            } catch (NumberFormatException e) {
                throw new CourseImportException(i + 1, "invalid hours", e);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            System.out.println(importCourses(List.of("JAVA;40", "SQL;24")));
            importCourses(List.of("JAVA;40", "SQL;twenty", "GO;30"));
        } catch (CourseImportException e) {
            System.out.println("error:  " + e.getMessage());
            System.out.println("record: " + e.getRecordNumber());
            System.out.println("cause:  " + e.getCause());
        }
    }
}
