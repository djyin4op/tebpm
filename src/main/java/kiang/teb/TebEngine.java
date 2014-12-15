package kiang.teb;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright (c) 2014 by kiang
 *
 * @author kiang
 * @version 0.1-pre
 */
public interface TebEngine {

    public TebEngine init(Properties properties) throws Exception;

    public void test(Map arguments, Writer writer) throws Exception;

    public void test(Map arguments, OutputStream output) throws Exception;

    public void shut() throws Exception;

    public boolean isBinarySupport();
}
