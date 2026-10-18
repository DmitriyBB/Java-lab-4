package task02;

public class Main {

    enum Kind { CHECKED, UNCHECKED, OTHER_RESULT, UNRECOVERABLE }

    record Situation(String name, Kind kind, String example, String why) {}

    public static void main(String[] args) {
        Situation[] table = {
            new Situation("Invalid method argument", Kind.UNCHECKED, "IllegalArgumentException",
                    "caller bug, fix the call instead of catching"),
            new Situation("Null passed where forbidden", Kind.UNCHECKED, "NullPointerException (Objects.requireNonNull)",
                    "programming error"),
            new Situation("File not found", Kind.CHECKED, "FileNotFoundException / NoSuchFileException",
                    "external, caller can ask another path or retry"),
            new Situation("Illegal object state", Kind.UNCHECKED, "IllegalStateException",
                    "method called at the wrong time - code bug"),
            new Situation("Network failure", Kind.CHECKED, "IOException",
                    "expected in real world, caller can retry or degrade"),
            new Situation("Configuration error at startup", Kind.UNCHECKED, "IllegalStateException on start",
                    "app cannot work, fail fast"),
            new Situation("Out of memory", Kind.UNRECOVERABLE, "OutOfMemoryError",
                    "Error, application should not catch it"),
            new Situation("Course not found by id", Kind.OTHER_RESULT, "Optional.empty()",
                    "absence is a normal outcome, not an error"),
            new Situation("User input fails validation", Kind.OTHER_RESULT, "Result / list of errors",
                    "expected, need to show all problems to user"),
            new Situation("Stack overflow (infinite recursion)", Kind.UNRECOVERABLE, "StackOverflowError",
                    "bug in code, JVM state is unreliable"),
        };

        for (Situation s : table) {
            System.out.printf("%-38s %-14s %-46s %s%n", s.name(), s.kind(), s.example(), s.why());
        }
    }
}
