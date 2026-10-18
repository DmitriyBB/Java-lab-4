package task05;

public class LoggingResource implements AutoCloseable {

    private final String name;
    private final boolean failOnClose;

    public LoggingResource(String name, boolean failOnClose) {
        this.name = name;
        this.failOnClose = failOnClose;
        System.out.println("  open " + name);
    }

    public void use(boolean fail) {
        System.out.println("  use " + name);
        if (fail) {
            throw new IllegalStateException("use failed: " + name);
        }
    }

    @Override
    public void close() {
        System.out.println("  close " + name);
        if (failOnClose) {
            throw new IllegalStateException("close failed: " + name);
        }
    }
}
