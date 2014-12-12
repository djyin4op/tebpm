package kiang.tei;

import kiang.teb.TebEngine;
import kiang.teb.TebModel;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright (c) 2014 by kiang
 *
 * @author kiang
 * @version 0.1-pre
 */
public final class MockJsp implements TebEngine {
    @Override
    public TebEngine init(Properties properties) throws Exception {
        return this;
    }

    @Override
    public void test(Map arguments, Writer writer) throws Exception {
        writer.write("<html>\r\n<head>\r\n    <title>JspMock!!!</title>\r\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=");
        writer.write((String) arguments.get("target"));
        writer.write("\"/>\r\n    <style type=\"text/css\">\r\n        body { font-size: 10pt; color: #333333; }\r\n        thead { font-weight: bold; background-color: #C8FBAF; }\r\n        td { font-size: 10pt; text-align: center; }\r\n        .odd { background-color: #F3DEFB; }\r\n        .even { background-color: #EFFFF8; }\r\n    </style>\r\n</head>\r\n<body>\r\n    <h1>Template Engine Benchmark - JspMock!!!</h1>\r\n    <table>\r\n        <thead>\r\n            <tr>\r\n                <th>\u5E8F\u53F7</th>\r\n                <th>\u7F16\u7801</th>\r\n                <th>\u540D\u79F0</th>r\n                <th>\u65E5\u671F</th>\r\n                <th>\u503C</th>\r\n            </tr>\r\n        </thead>\r\n        <tbody>\r\n");
        TebModel model;
        List<TebModel> models = (List<TebModel>) arguments.get("models");
        for (int i = 0, n = models.size(); i < n; i++) {
            model = models.get(i);
            writer.write("            <tr class=\"");
            writer.write(i % 2 == 0 ? "odd" : "even");
            writer.write("\">\r\n                <td>");
            writer.write(String.valueOf(i));
            writer.write("</td>\r\n                <td>");
            writer.write(String.valueOf(model.getCode()));
            writer.write("</td>\r\n                <td>");
            writer.write(model.getName());
            writer.write("</td>\r\n                <td>");
            writer.write(String.valueOf(model.getDate()));
            writer.write("</td>\r\n");
            if (model.getValue() > 105.5) {
                writer.write("                <td style=\"color: red;\">");
                writer.write(String.valueOf(model.getValue()));
                writer.write("</td>\r\n");
            } else {
                writer.write("                <td style=\"color: blue;\">");
                writer.write(String.valueOf(model.getValue()));
                writer.write("</td>\r\n");
            }
            writer.write("            </tr>\r\n");
        }
        writer.write("        </tbody>\r\n    </table>\r\n</body>\r\n</html>");
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
        TebEngine engine = new MockJsp().init(properties);
        engine.test(data, writer);
        writer.flush();
        engine.shut();
    }
}
