package kiang.tei;

import httl.Engine;
import kiang.teb.TebEngine;
import kiang.teb.TebModel;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright (c) 2014 by kiang
 *
 * @author kiang
 * @version 0.1-pre
 */
public final class Httl implements TebEngine {
    private Engine engine;

    @Override
    public TebEngine init(Properties properties) throws Exception {
        final Properties ps = new Properties();
        ps.setProperty("import.packages", "java.util");
        ps.setProperty("filter", "null");
        ps.setProperty("logger", "null");
        ps.setProperty("input.encoding", properties.getProperty("source", "UTF-8"));
        ps.setProperty("output.encoding", properties.getProperty("target", "UTF-8"));
        ps.setProperty("template.suffix", "tpl");
        boolean debug = Boolean.valueOf(properties.getProperty("debug"));
        if (debug) {
            ps.setProperty("loaders", "httl.spi.loaders.ClasspathLoader,httl.spi.loaders.JarLoader");
            ps.setProperty("template.directory", "kiang/tpl/");
        } else {
            ps.setProperty("loaders", "httl.spi.loaders.FileLoader");
            ps.setProperty("template.directory", new File("kiang/tpl/").getAbsolutePath());
        }
        engine = Engine.getEngine(ps);
        return this;
    }

    @Override
    public void test(Map arguments, Writer writer) throws Exception {
        engine.getTemplate("/httl.tpl").render(arguments, writer);

    }

    @Override
    public void test(Map arguments, OutputStream output) throws Exception {
        engine.getTemplate("/httl.tpl").render(arguments, output);

    }

    @Override
    public boolean isBinarySupport() {
        return true;
    }

    @Override
    public void shut() throws Exception {
    }

    public static void main(String args[]) throws Exception {
        String source = "UTF-8", target = "UTF-8";
        OutputStream output = System.out;
        Map data = new HashMap();
        data.put("target", target);
        data.put("models", TebModel.dummyModels(20));
        Properties properties = new Properties();
        properties.setProperty("source", source);
        properties.setProperty("target", target);
        properties.setProperty("binary", String.valueOf(true));
        TebEngine engine = new Httl().init(properties);
        engine.test(data, output);
        output.flush();
        engine.shut();
    }
}
