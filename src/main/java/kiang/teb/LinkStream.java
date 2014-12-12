package kiang.teb;

import java.io.*;

/**
 * @author Boilit
 * @see
 */
public final class LinkStream extends Writer implements TebCounter {
    private long count;
    private Writer writer;
    private ByteStream byteStream = new ByteStream();

    public LinkStream(String encoding) throws UnsupportedEncodingException {
        this.writer = new OutputStreamWriter(this.byteStream, encoding);
    }

    @Override
    public void write(int c) throws IOException {
        this.writer.write(c);
        this.count++;
    }

    @Override
    public void write(char[] source, int offset, int length) throws IOException {
        this.writer.write(source, offset, length);
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
        return byteStream.size();
    }

    @Override
    public long count() {
        return this.count;
    }
}
