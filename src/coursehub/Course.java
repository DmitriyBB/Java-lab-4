package coursehub;

public class Course {

    public static final int MIN_HOURS = 1;
    public static final int MAX_HOURS = 200;

    private final CourseCode code;
    private final int durationHours;
    private int completedHours;

    public Course(CourseCode code, int durationHours) {
        if (durationHours < MIN_HOURS || durationHours > MAX_HOURS) {
            throw new IllegalArgumentException(
                    "Duration must be " + MIN_HOURS + ".." + MAX_HOURS + ", got " + durationHours);
        }
        this.code = code;
        this.durationHours = durationHours;
    }

    public void completeHours(int hours) {
        if (hours <= 0) {
            throw new IllegalArgumentException("Hours must be positive, got " + hours);
        }
        if (completedHours + hours > durationHours) {
            throw new IllegalStateException(
                    "Cannot complete " + hours + " hours, remaining " + remainingHours());
        }
        completedHours += hours;
    }

    public CourseCode getCode() {
        return code;
    }

    public int getDurationHours() {
        return durationHours;
    }

    public int getCompletedHours() {
        return completedHours;
    }

    public int remainingHours() {
        return durationHours - completedHours;
    }

    @Override
    public String toString() {
        return "Course{code=" + code.value() + ", duration=" + durationHours + ", completed=" + completedHours + "}";
    }
}
