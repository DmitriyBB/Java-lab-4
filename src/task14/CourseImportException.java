package task14;

public class CourseImportException extends Exception {

    private final int recordNumber;
    private final int processedRecords;

    public CourseImportException(int recordNumber, int processedRecords, String message, Throwable cause) {
        super("Record " + recordNumber + ": " + message, cause);
        this.recordNumber = recordNumber;
        this.processedRecords = processedRecords;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public int getProcessedRecords() {
        return processedRecords;
    }
}
