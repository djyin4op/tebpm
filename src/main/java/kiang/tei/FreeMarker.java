package kiang.tei;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateModel;
import kiang.teb.Performer;
import kiang.teb.TebEngine;
import kiang.teb.TebModel;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
public final class FreeMarker implements TebEngine {
    private Configuration engine;
    private TemplateModel templateModel;

    @Override
    public TebEngine init(Properties properties) throws Exception {
        engine = new Configuration();
        engine.setDefaultEncoding(properties.getProperty("target", "UTF-8"));
        engine.setDirectoryForTemplateLoading(new File(FreeMarker.class.getResource("/kiang/tpl").getPath()));
        engine.setObjectWrapper(new BeansWrapper());
        templateModel = BeansWrapper.getDefaultInstance().getStaticModels().get(String.class.getName());
        return this;
    }

    @Override
    public void test(Map arguments, Writer writer) throws Exception {
        arguments.put("String", this.templateModel);
        this.engine.getTemplate("/freemarker.tpl").process(arguments, writer);
    }

    @Override
    public void test(Map arguments, OutputStream output) throws Exception {
    }

    @Override
    public boolean isBinarySupport() {
        return false;
    }

    @Override
    public void shut() throws Exception {
    }

    public static void main(String args[]) throws Exception {
        String source="UTF-8", target = "UTF-8";
        Writer writer = new OutputStreamWriter(System.out, target);
        Map data = new HashMap();
        data.put("target", target);
        data.put("models", TebModel.dummyModels(20));
        Properties properties = new Properties();
        properties.setProperty("source", source);
        properties.setProperty("target", target);
        properties.setProperty("binary", String.valueOf(true));
        TebEngine engine = new FreeMarker().init(properties);
        engine.test(data, writer);
        writer.flush();
        engine.shut();
    }
}
