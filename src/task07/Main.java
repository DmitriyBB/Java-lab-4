package task07;

import java.util.Map;
import java.util.Optional;

public class Main {

    record Course(String code, int pricePerHour, int hours) {}

    static final Map<String, Course> CATALOG = Map.of(
            "JAVA", new Course("JAVA", 10, 40),
            "SQL", new Course("SQL", 8, 24));

    static Optional<Course> findCourse(String code) {
        return Optional.ofNullable(CATALOG.get(code));
    }

    static int calculatePrice(Course course, int discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("discountPercent must be 0..100, got " + discountPercent);
        }
        int full = course.pricePerHour() * course.hours();
        return full - full * discountPercent / 100;
    }

    public static void main(String[] args) {
        System.out.println("findCourse      -> Optional: absence is a normal answer, caller decides");
        System.out.println("parseCourse     -> typed Result: bad input from user is expected, need error details");
        System.out.println("enroll          -> checked exception: business refusal caller must handle");
        System.out.println("calculatePrice  -> unchecked exception: wrong argument is a programming error");
        System.out.println();

        System.out.println(findCourse("JAVA").map(c -> "found " + c.code()).orElse("not found"));
        System.out.println(findCourse("GO").map(c -> "found " + c.code()).orElse("not found"));

        Course java = findCourse("JAVA").orElseThrow();
        System.out.println("price with 25% = " + calculatePrice(java, 25));
        try {
            calculatePrice(java, 150);
        } catch (IllegalArgumentException e) {
            System.out.println("bug detected: " + e.getMessage());
        }
    }
}
