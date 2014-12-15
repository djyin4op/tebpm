package kiang.teb;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Boilit
 * @see
 */
public final class ByteStream extends OutputStream implements TebCounter {
    private long size = 0;
    private long count = 0;

    @Override
    public final void write(int b) throws IOException {
        this.size++;
        this.count++;
    }

    @Override
    public final void write(byte[] source, int offset, int length) throws IOException {
        this.size += length;
        this.count++;
    }

    @Override
    public final void flush() throws IOException {
    }

    @Override
    public final long size() {
        return this.size;
    }

    public long count() {
        return this.count;
    }
}
