package task10;

import coursehub.CourseCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CourseCodeParameterizedTest {

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource({
            "JAVA-101, JAVA-101",
            "java-101, JAVA-101",
            "'  sql-200  ', SQL-200",
            "Db-001, DB-001"
    })
    void normalizesValidCodes(String raw, String expected) {
        assertEquals(expected, CourseCode.of(raw).value());
    }

    @ParameterizedTest(name = "\"{0}\" -> IllegalArgumentException")
    @NullSource
    @ValueSource(strings = {"", "   ", "J-101", "JAVAS-101", "JAVA101", "JAVA-1", "JAVA-1000", "12-345"})
    void rejectsInvalidCodes(String raw) {
        assertThrows(IllegalArgumentException.class, () -> CourseCode.of(raw));
    }
}
