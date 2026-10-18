package task04;

public class Main {

    static int returnInTry() {
        try {
            System.out.println("  try: return 1");
            return 1;
        } finally {
            System.out.println("  finally");
        }
    }

    static int throwInTryReturnInCatch() {
        try {
            System.out.println("  try: throw");
            throw new IllegalStateException("from try");
        } catch (IllegalStateException e) {
            System.out.println("  catch: return 2");
            return 2;
        } finally {
            System.out.println("  finally");
        }
    }

    static int throwInCatch() {
        try {
            System.out.println("  try: throw");
            throw new IllegalStateException("from try");
        } catch (IllegalStateException e) {
            System.out.println("  catch: throw new");
            throw new RuntimeException("from catch", e);
        } finally {
            System.out.println("  finally");
        }
    }

    static int valueChangedInFinally() {
        int x = 10;
        try {
            System.out.println("  try: return x (x=10)");
            return x;
        } finally {
            x = 99;
            System.out.println("  finally: x=99, but returned value already saved");
        }
    }

    @SuppressWarnings("finally")
    static int badReturnInFinally() {
        try {
            System.out.println("  try: throw");
            throw new IllegalStateException("important error");
        } finally {
            System.out.println("  finally: return -1  (BAD)");
            return -1;
        }
    }

    public static void main(String[] args) {
        System.out.println("1) return in try:");
        System.out.println("  result = " + returnInTry());

        System.out.println("2) throw in try, return in catch:");
        System.out.println("  result = " + throwInTryReturnInCatch());

        System.out.println("3) throw in catch:");
        try {
            throwInCatch();
        } catch (RuntimeException e) {
            System.out.println("  caller got: " + e.getMessage() + ", cause: " + e.getCause().getMessage());
        }

        System.out.println("4) change variable in finally:");
        System.out.println("  result = " + valueChangedInFinally());

        System.out.println("5) return in finally hides exception:");
        System.out.println("  result = " + badReturnInFinally() + "  <- IllegalStateException is lost");
        System.out.println();
        System.out.println("Rule: finally always runs after try/catch, before the method leaves.");
        System.out.println("A return in finally replaces any pending return or exception, so errors disappear.");
        System.out.println("Use finally only for cleanup, never for return.");
    }
}
