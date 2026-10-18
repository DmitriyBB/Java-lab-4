package task14;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseImporterTest {

    static class FakeReader extends Reader {
        private final StringReader content;
        private final boolean failOnClose;
        boolean closed;

        FakeReader(String content, boolean failOnClose) {
            this.content = new StringReader(content);
            this.failOnClose = failOnClose;
        }

        @Override
        public int read(char[] buffer, int offset, int length) throws IOException {
            return content.read(buffer, offset, length);
        }

        @Override
        public void close() throws IOException {
            closed = true;
            if (failOnClose) {
                throw new IOException("close failed");
            }
        }
    }

    private final CourseImporter importer = new CourseImporter();

    @Test
    void validInput_publishesAllRecordsAndClosesReader() throws Exception {
        FakeReader reader = new FakeReader("JAVA;40\nSQL;24", false);

        importer.importFrom(reader);

        assertEquals(List.of(new Course("JAVA", 40), new Course("SQL", 24)), importer.getPublished());
        assertTrue(reader.closed);
    }

    @Test
    void malformedRecord_reportsPositionAndPublishesNothing() {
        FakeReader reader = new FakeReader("JAVA;40\nSQL;abc\nGO;30", false);

        CourseImportException e = assertThrows(CourseImportException.class, () -> importer.importFrom(reader));

        assertEquals(2, e.getRecordNumber());
        assertEquals(1, e.getProcessedRecords());
        assertInstanceOf(NumberFormatException.class, e.getCause());
        assertTrue(importer.getPublished().isEmpty());
        assertTrue(reader.closed);
    }

    @Test
    void malformedRecordAndCloseFailure_closeErrorIsSuppressed() {
        FakeReader reader = new FakeReader("JAVA;40\nbroken", true);

        CourseImportException e = assertThrows(CourseImportException.class, () -> importer.importFrom(reader));

        assertEquals(2, e.getRecordNumber());
        assertEquals(1, e.getSuppressed().length);
        assertEquals("close failed", e.getSuppressed()[0].getMessage());
        assertTrue(importer.getPublished().isEmpty());
    }

    @Test
    void onlyCloseFailure_isMainExceptionAndNothingPublished() {
        FakeReader reader = new FakeReader("JAVA;40\nSQL;24", true);

        IOException e = assertThrows(IOException.class, () -> importer.importFrom(reader));

        assertEquals("close failed", e.getMessage());
        assertTrue(importer.getPublished().isEmpty());
    }

    @Test
    void failedImport_keepsPreviouslyPublishedResult() throws Exception {
        importer.importFrom(new StringReader("JAVA;40"));

        assertThrows(CourseImportException.class, () -> importer.importFrom(new StringReader("SQL;x")));

        assertEquals(List.of(new Course("JAVA", 40)), importer.getPublished());
    }
}
