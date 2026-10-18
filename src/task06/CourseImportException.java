package task06;

public class CourseImportException extends Exception {

    private final int recordNumber;

    public CourseImportException(int recordNumber, String message, Throwable cause) {
        super("Record " + recordNumber + ": " + message, cause);
        this.recordNumber = recordNumber;
    }

    public int getRecordNumber() {
        return recordNumber;
    }
}
