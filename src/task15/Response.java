package task15;

public record Response(ErrorCode code, String message) {

    public static Response ok(String message) {
        return new Response(ErrorCode.OK, message);
    }

    public static Response error(ErrorCode code, String message) {
        return new Response(code, message);
    }
}
