package kiang.teb;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * @author Boilit
 * @see
 */
public final class CharStream extends Writer implements TebCounter {
    private long size;
    private long count;
    private Writer writer;

    public CharStream(String encoding) throws UnsupportedEncodingException {
        this.writer = new OutputStreamWriter(new NoneStream(), encoding);
    }

    @Override
    public void write(int c) throws IOException {
        this.writer.write(c);
        this.size++;
        this.count++;
    }

    @Override
    public void write(char[] source, int offset, int length) throws IOException {
        this.writer.write(source, offset, length);
        this.size += length;
        this.count++;
    }

    @Override
    public void flush() throws IOException {
        this.writer.flush();
    }

    @Override
    public void close() throws IOException {
        this.writer.close();
    }

    @Override
    public final long size() {
        return this.size;
    }

    @Override
    public long count() {
        return this.count;
    }
}
