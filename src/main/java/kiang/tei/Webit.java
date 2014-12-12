package kiang.tei;

import kiang.teb.TebEngine;
import kiang.teb.TebModel;
import webit.script.CFG;
import webit.script.Engine;

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
public final class Webit implements TebEngine {
    private Engine engine;

    @Override
    public TebEngine init(Properties properties) throws Exception {
        final Map<String, Object> ps = new HashMap<String, Object>();
        ps.put(CFG.LOADER_ENCODING, properties.getProperty("source", "UTF-8"));
        ps.put(CFG.OUT_ENCODING, properties.getProperty("target", "UTF-8"));
        final boolean binary = Boolean.parseBoolean(properties.getProperty("binary", "true"));
        if (binary) {
            ps.put(CFG.TEXT_FACTORY, CFG.BYTE_ARRAY_TEXT_FACTORY);
        } else {
            ps.put(CFG.TEXT_FACTORY, CFG.CHAR_ARRAY_TEXT_FACTORY);
        }
        engine = Engine.create("", ps);
        return this;
    }

    @Override
    public void test(Map arguments, Writer writer) throws Exception {
        engine.getTemplate("/kiang/tpl/webit.tpl").merge(arguments, writer);
    }

    @Override
    public void test(Map arguments, OutputStream output) throws Exception {
        engine.getTemplate("/kiang/tpl/webit.tpl").merge(arguments, output);
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
        TebEngine engine = new Webit().init(properties);
        engine.test(data, output);
        output.flush();
        engine.shut();
    }
}
