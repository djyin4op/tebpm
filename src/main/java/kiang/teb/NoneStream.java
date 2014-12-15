package kiang.teb;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Copyright (c) 2014 by kiang
 *
 * @author kiang
 * @version 0.1-pre
 */
public final class NoneStream extends OutputStream {

    @Override
    public final void write(int b) throws IOException {
    }

    @Override
    public final void write(byte[] source, int offset, int length) throws IOException {
    }

    @Override
    public final void flush() throws IOException {
    }
}
