package task13;

public class EnrollmentRules {

    public enum Decision { ACCEPTED, COURSE_CLOSED, ALREADY_APPLIED, PREREQUISITE_MISSING, NO_SEATS }

    public static Decision decide(boolean courseOpen, boolean hasSeats, boolean hasPrerequisite, boolean repeatedApplication) {
        if (!courseOpen) {
            return Decision.COURSE_CLOSED;
        }
        if (repeatedApplication) {
            return Decision.ALREADY_APPLIED;
        }
        if (!hasPrerequisite) {
            return Decision.PREREQUISITE_MISSING;
        }
        if (!hasSeats) {
            return Decision.NO_SEATS;
        }
        return Decision.ACCEPTED;
    }
}
