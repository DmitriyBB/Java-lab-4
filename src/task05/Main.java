package task05;

public class Main {

    static void run(String title, boolean failInUse, boolean failOnClose) {
        System.out.println(title);
        try (LoggingResource r = new LoggingResource("R", failOnClose)) {
            r.use(failInUse);
            System.out.println("  work done");
        } catch (IllegalStateException e) {
            System.out.println("  caught: " + e.getMessage());
            for (Throwable s : e.getSuppressed()) {
                System.out.println("  suppressed: " + s.getMessage());
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        run("1) success", false, false);
        run("2) error in use", true, false);
        run("3) error in use AND in close", true, true);
        run("4) error only in close", false, true);
    }
}
