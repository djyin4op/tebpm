package kiang.tei;

import kiang.teb.TebEngine;
import kiang.teb.TebModel;
import org.rythmengine.RythmEngine;

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
public final class Rythm implements TebEngine {
    private RythmEngine engine;

    @Override
    public TebEngine init(Properties properties) throws Exception {
        final Properties ps = new Properties();
        ps.put("log.enabled", false);
        ps.put("feature.smart_escape.enabled", false);
        ps.put("feature.transform.enabled", false);
        boolean debug = Boolean.valueOf(properties.getProperty("debug"));
        if (debug) {
            ps.put("home.template", Rythm.class.getResource("/kiang/tpl").toURI().toURL().getFile());
        } else {
            ps.put("home.template", new File("kiang/tpl"));
        }

        //ps.put("codegen.dynamic_exp.enabled", true);
        //ps.put("built_in.code_type", "false");
        //ps.put("built_in.transformer", "false");
        //ps.put("engine.file_write", "false");
        //ps.put("codegen.compact.enabled", "false");
        //ps.put("home.tmp", "c:\\tmp");
        //ps.put("engine.mode", Rythm.Mode.dev);
        engine = new RythmEngine(ps);
        return this;
    }

    @Override
    public void test(Map arguments, Writer writer) throws Exception {
        engine.render(writer, "rythm.tpl", arguments.get("source"), arguments.get("models"));
    }

    @Override
    public void test(Map arguments, OutputStream output) throws Exception {
        engine.render(output, "rythm.tpl", arguments.get("source"), arguments.get("models"));
    }

    @Override
    public boolean isBinarySupport() {
        return true;
    }

    @Override
    public void shut() throws Exception {
        engine.shutdown();
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
        TebEngine engine = new Rythm().init(properties);
        engine.test(data, output);
        output.flush();
        engine.shut();
    }
}
