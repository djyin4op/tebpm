package kiang.tei;

import kiang.teb.TebEngine;
import kiang.teb.TebModel;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.FileResourceLoader;

import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Boilit
 * @see
 */
public final class Beetl implements TebEngine {
    private GroupTemplate engine;

    @Override
    public final TebEngine init(Properties properties) throws Exception {
        BeetlFixedClassResourceLoader loader = new BeetlFixedClassResourceLoader("/kiang/tpl/");
        Configuration cfg = Configuration.defaultConfiguration();
        cfg.setCharset(properties.getProperty("source", "UTF-8"));
        cfg.setDirectByteOutput(Boolean.parseBoolean(properties.getProperty("binary", "true")));
        Map<String,String> resources = cfg.getResourceMap();
        resources.put("autoCheck", "false");
        engine = new GroupTemplate(loader, cfg);
        return this;
    }

    @Override
    public void test(final Map arguments, final Writer writer) throws Exception {
        final Template template = engine.getTemplate("beetl.tpl");
        template.binding(arguments);
        template.renderTo(writer);
    }

    @Override
    public void test(Map arguments, final OutputStream output) throws Exception {
        Template template = engine.getTemplate("beetl.tpl");
        template.binding(arguments);
        template.renderTo(output);
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
        TebEngine engine = new Beetl().init(properties);
        engine.test(data, output);
        output.flush();
        engine.shut();
    }
}
