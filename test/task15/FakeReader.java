package task15;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

class FakeReader extends Reader {

    private final StringReader content;
    private final RuntimeException failOnRead;
    private final IOException ioFailOnRead;
    private final boolean failOnClose;
    boolean closed;

    private FakeReader(String content, RuntimeException failOnRead, IOException ioFailOnRead, boolean failOnClose) {
        this.content = new StringReader(content);
        this.failOnRead = failOnRead;
        this.ioFailOnRead = ioFailOnRead;
        this.failOnClose = failOnClose;
    }

    static FakeReader of(String content) {
        return new FakeReader(content, null, null, false);
    }

    static FakeReader failingOnClose(String content) {
        return new FakeReader(content, null, null, true);
    }

    static FakeReader failingOnRead(IOException e) {
        return new FakeReader("", null, e, false);
    }

    static FakeReader withBug(RuntimeException e) {
        return new FakeReader("", e, null, false);
    }

    @Override
    public int read(char[] buffer, int offset, int length) throws IOException {
        if (failOnRead != null) {
            throw failOnRead;
        }
        if (ioFailOnRead != null) {
            throw ioFailOnRead;
        }
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
