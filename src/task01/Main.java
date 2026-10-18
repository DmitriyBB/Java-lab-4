package task01;

import java.util.Map;

public class Main {

    static final Map<String, Integer> HOURS = Map.of("JAVA", 40, "SQL", 24);

    static int level1(String code) {
        return level2(code);
    }

    static int level2(String code) {
        return level3(code) * 2;
    }

    static int level3(String code) {
        return level4(code) + 1;
    }

    static int level4(String code) {
        return HOURS.get(code);
    }

    static int level4Fixed(String code) {
        Integer hours = HOURS.get(code);
        if (hours == null) {
            throw new IllegalArgumentException("Unknown course code: " + code);
        }
        return hours;
    }

    public static void main(String[] args) {
        try {
            level1("PYTHON");
        } catch (NullPointerException e) {
            e.printStackTrace(System.out);
            StackTraceElement top = e.getStackTrace()[0];
            System.out.println();
            System.out.println("Type:    " + e.getClass().getName());
            System.out.println("Message: " + e.getMessage());
            System.out.println("Place:   " + top.getMethodName() + " line " + top.getLineNumber());
            System.out.println("Path:    main -> level1 -> level2 -> level3 -> level4");
            System.out.println("Cause:   HOURS.get returns null for unknown code, auto-unboxing null -> NPE");
            System.out.println("Fix:     check the value in level4 where it appears, not in main/level1");
        }

        System.out.println();
        System.out.println("level4Fixed(\"JAVA\") = " + level4Fixed("JAVA"));
        try {
            level4Fixed("PYTHON");
        } catch (IllegalArgumentException e) {
            System.out.println("level4Fixed(\"PYTHON\") -> " + e.getMessage());
        }
    }
}
