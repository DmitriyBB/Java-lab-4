package task14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CourseImporter {

    private List<Course> published = List.of();

    public List<Course> getPublished() {
        return published;
    }

    public void importFrom(Reader source) throws CourseImportException, IOException {
        List<Course> batch = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(source)) {
            String line;
            int recordNumber = 0;
            while ((line = reader.readLine()) != null) {
                recordNumber++;
                batch.add(parse(recordNumber, line, batch.size()));
            }
        }
        published = List.copyOf(batch);
    }

    private Course parse(int recordNumber, String line, int processed) throws CourseImportException {
        String[] parts = line.split(";");
        if (parts.length != 2) {
            throw new CourseImportException(recordNumber, processed, "expected 2 fields", null);
        }
        try {
            return new Course(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        } catch (NumberFormatException e) {
            throw new CourseImportException(recordNumber, processed, "hours is not a number", e);
        }
    }
}
