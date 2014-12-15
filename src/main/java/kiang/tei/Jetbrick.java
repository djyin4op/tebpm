package kiang.tei;

import jetbrick.template.JetConfig;
import jetbrick.template.JetEngine;
import kiang.teb.Performer;
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
public final class Jetbrick implements TebEngine {
    private JetEngine engine;

    @Override
    public TebEngine init(Properties properties) throws Exception {
        final Properties ps = new Properties();
        ps.setProperty(JetConfig.INPUT_ENCODING, properties.getProperty("source", "UTF-8"));
        ps.setProperty(JetConfig.OUTPUT_ENCODING, properties.getProperty("target", "UTF-8"));
        boolean debug = Boolean.valueOf(properties.getProperty("debug"));
        if (debug) {
            ps.setProperty(JetConfig.TEMPLATE_LOADERS, "jetbrick.template.loader.ClasspathResourceLoader");
        } else {
            ps.setProperty(JetConfig.TEMPLATE_LOADERS, "jetbrick.template.loader.FileSystemResourceLoader");
        }
        engine = JetEngine.create(ps);
        return this;
    }

    @Override
    public void test(Map arguments, Writer writer) throws Exception {
        engine.getTemplate("kiang/tpl/jetbrick.tpl").render(arguments, writer);
    }

    @Override
    public void test(Map arguments, OutputStream output) throws Exception {
        engine.getTemplate("kiang/tpl/jetbrick.tpl").render(arguments, output);
    }

    @Override
    public boolean isBinarySupport() {
        return true;
    }

    @Override
    public void shut() throws Exception {
    }

    public static void main(String args[]) throws Exception {
        String source="UTF-8", target = "UTF-8";
        OutputStream output = System.out;
        Map data = new HashMap();
        data.put("target", target);
        data.put("models", TebModel.dummyModels(20));
        Properties properties = new Properties();
        properties.setProperty("source", source);
        properties.setProperty("target", target);
        properties.setProperty("binary", String.valueOf(true));
        TebEngine engine = new Jetbrick().init(properties);
        engine.test(data, output);
        output.flush();
        engine.shut();
    }
}
