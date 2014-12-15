package kiang.tei;

import kiang.teb.TebEngine;
import kiang.teb.TebModel;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

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
public final class Velocity implements TebEngine {
    private VelocityEngine engine;

    @Override
    public TebEngine init(Properties properties) throws Exception {
        engine = new VelocityEngine();
        final Properties ps = new Properties();
        ps.setProperty(";runtime.log", "velocity.log");
        ps.setProperty(";runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
        ps.setProperty("resource.loader", "file");
        ps.setProperty("file.resource.loader.cache", "true");
        ps.setProperty("file.resource.loader.class ", "Velocity.Runtime.Resource.Loader.FileResourceLoader");
        ps.setProperty(";resource.loader", "webapp");
        ps.setProperty(";webapp.resource.loader.class", "org.apache.velocity.tools.view.servlet.WebappLoader");
        ps.setProperty(";webapp.resource.loader.cache", "true");
        ps.setProperty(";webapp.resource.loader.modificationCheckInterval", "3");
        ps.setProperty(";directive.foreach.counter.name", "velocityCount");
        ps.setProperty(";directive.foreach.counter.initial.value", "1");

        ps.setProperty("file.resource.loader.path", Velocity.class.getResource("/kiang/tpl").getPath());
        ps.setProperty("input.encoding", properties.getProperty("source", "UTF-8"));
        ps.setProperty("output.encoding", properties.getProperty("target", "UTF-8"));
        engine.init(ps);
        return this;
    }

    @Override
    public void test(Map arguments, Writer writer) throws Exception {
        engine.getTemplate("velocity.tpl").merge(new VelocityContext(arguments), writer);
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
        TebEngine engine = new Velocity().init(properties);
        engine.test(data, writer);
        writer.flush();
        engine.shut();
    }
}
