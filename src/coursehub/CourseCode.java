package coursehub;

public record CourseCode(String value) {

    public CourseCode {
        if (value == null) {
            throw new IllegalArgumentException("Course code is null");
        }
        value = value.trim().toUpperCase();
        if (!value.matches("[A-Z]{2,4}-\\d{3}")) {
            throw new IllegalArgumentException("Invalid course code: " + value);
        }
    }

    public static CourseCode of(String raw) {
        return new CourseCode(raw);
    }
}
