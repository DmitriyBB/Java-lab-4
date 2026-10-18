package task13;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnrollmentRulesTest {

    @ParameterizedTest(name = "open={0}, seats={1}, prereq={2}, repeat={3} -> {4}")
    @CsvSource({
            "true,  true,  true,  false, ACCEPTED",
            "false, true,  true,  false, COURSE_CLOSED",
            "false, false, false, true,  COURSE_CLOSED",
            "true,  true,  true,  true,  ALREADY_APPLIED",
            "true,  false, false, true,  ALREADY_APPLIED",
            "true,  true,  false, false, PREREQUISITE_MISSING",
            "true,  false, false, false, PREREQUISITE_MISSING",
            "true,  false, true,  false, NO_SEATS"
    })
    void decisionTable(boolean open, boolean seats, boolean prereq, boolean repeat, EnrollmentRules.Decision expected) {
        assertEquals(expected, EnrollmentRules.decide(open, seats, prereq, repeat));
    }
}
