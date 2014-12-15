package kiang.teb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Properties;

/**
 * Copyright (c) 2014 by kiang
 *
 * @author kiang
 * @version 0.1-pre
 */
public final class TebReport {
    private static final String VERSION = "Kiang TEB 1.0.9";
    private static final String[] COLORS = new String[]{
            "#45b97c;", "#ffd400;", "#f47920;", "#b2d235;", "#6a6da9;",
            "#ef5b9c;", "#7fb80e;", "#999d9c;", "#1d953f;", "#d71345;",
            "#843900;", "#78a355;", "#145b7d;", "#c37e00;", "#293047;"
    };
    private static final DecimalFormat NDF = new DecimalFormat("#,###");

    static interface PanelRender {
        public String getTitle();

        public long getValue(TebResult[] results, int index);
    }

    public static BufferedWriter writeHead(File file, Properties properties) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        TebUtilities.println(bw, "<!DOCTYPE html>");
        TebUtilities.println(bw, "<head>");
        TebUtilities.println(bw, "<title>RPT" + properties.getProperty("thread") + "-" + VERSION + "</title>");
        TebUtilities.println(bw, "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>");
        TebUtilities.println(bw, "<style type=\"text/css\">");
        TebUtilities.println(bw, "    html, body, td, div { padding: 0px; margin: 0px; font-size: 9pt;}");
        TebUtilities.println(bw, "    table.panel { width: 200px; height: 120px; border: 1px solid #000000; border-bottom: none; margin-left: 1px; margin-right: 1px; }");
        TebUtilities.println(bw, "    table.legend { width: 806px; margin-left: 1px; margin-right: 1px; border-left: 1px solid #000000; }");
        TebUtilities.println(bw, "    table.info { width: 806px; margin-left: 1px; margin-right: 1px; border-left: 1px solid #000000; border-bottom: 1px solid #000000; }");
        TebUtilities.println(bw, "    tr.head { background-color: #CAFF70; }");
        TebUtilities.println(bw, "    tr.even { background-color: #FFF8DC; }");
        TebUtilities.println(bw, "    tr.odd { background-color: #D1EEEE; }");
        TebUtilities.println(bw, "    td.title { background-color: #CAFF70; text-align: center; border-bottom: 1px solid #000000; }");
        TebUtilities.println(bw, "    td.legend { border-top: 1px solid #000000; border-right: 1px solid #000000; }");
        TebUtilities.println(bw, "    div.panel { width: 10px; margin-left: 1px; margin-right: 1px; }");
        TebUtilities.println(bw, "    div.legend { width: 10px; height: 10px; }");
        TebUtilities.println(bw, "</style>");
        TebUtilities.println(bw, "</head>");
        TebUtilities.println(bw, "<body>");
        return bw;
    }

    public static void writeTail(BufferedWriter bw) throws Exception {
        try {
            TebUtilities.println(bw, "</body>");
            TebUtilities.println(bw, "</html>");
        } finally {
            if (bw != null) {
                bw.close();
            }
        }
    }

    public static void writeBody(BufferedWriter bw, Properties properties, final boolean binary, String target, TebResult[] results, String date) throws Exception {
        final int thread = Integer.parseInt(properties.getProperty("thread"));
        final int record = Integer.parseInt(properties.getProperty("record"));
        final int period = Integer.parseInt(properties.getProperty("period"));
        final int warmed = Integer.parseInt(properties.getProperty("warmed"));
        final int looped = Integer.parseInt(properties.getProperty("looped"));
        final String version = properties.getProperty("version");
        final String option = properties.getProperty("option");

        TebUtilities.println(bw, "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">");
        TebUtilities.println(bw, "<tr>");
        TebUtilities.println(bw, "    <td>");
        writePanel(bw, results, new PanelRender() {
            @Override
            public String getTitle() {
                return "TPS(n/s)";
            }

            @Override
            public long getValue(TebResult[] results, int index) {
                return looped * thread * 1000 / (results[index].getTime() == 0 ? 1 : results[index].getTime());
            }
        });
        TebUtilities.println(bw, "    </td>");
        TebUtilities.println(bw, "    <td>");
        writePanel(bw, results, new PanelRender() {
            @Override
            public String getTitle() {
                return "OnceIo(n)";
            }

            @Override
            public long getValue(TebResult[] results, int index) {
                return results[index].getOnceIo();
            }
        });
        TebUtilities.println(bw, "    </td>");
        TebUtilities.println(bw, "    <td>");
        writePanel(bw, results, new PanelRender() {
            @Override
            public String getTitle() {
                return "OnceOut(" + (binary ? "b" : "c") + ")";
            }

            @Override
            public long getValue(TebResult[] results, int index) {
                return results[index].getOnceOut();
            }
        });
        TebUtilities.println(bw, "    </td>");
        TebUtilities.println(bw, "    <td>");
        writePanel(bw, results, new PanelRender() {
            @Override
            public String getTitle() {
                return "PermMem(b)";
            }

            @Override
            public long getValue(TebResult[] results, int index) {
                return results[index].getPermMem();
            }
        });
        TebUtilities.println(bw, "    </td>");
        TebUtilities.println(bw, "</tr>");
        TebUtilities.println(bw, "<tr>");
        TebUtilities.println(bw, "    <td colspan =\"4\">");
        TebUtilities.println(bw, "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"legend\">");
        TebUtilities.println(bw, "            <tr class=\"head\">");
        TebUtilities.println(bw, "                <td class=\"legend\" width=\"16px\">&nbsp;</td>");
        TebUtilities.println(bw, "                <td class=\"legend\" align=\"center\">Engine</td>");
        TebUtilities.println(bw, "                <td class=\"legend\" align=\"center\" width=\"80px\">TPS(n/s)</td>");
        TebUtilities.println(bw, "                <td class=\"legend\" align=\"center\" width=\"80px\">Time(ms)</td>");
        TebUtilities.println(bw, "                <td class=\"legend\" align=\"center\" width=\"80px\">OnceIo(n)</td>");
        TebUtilities.println(bw, "                <td class=\"legend\" align=\"center\" width=\"80px\">MassIo(n)</td>");
        TebUtilities.println(bw, "                <td class=\"legend\" align=\"center\" width=\"80px\">OnceOut(" + (binary ? "b" : "c") + ")</td>");
        TebUtilities.println(bw, "                <td class=\"legend\" align=\"center\" width=\"80px\">MassOut(" + (binary ? "b" : "c") + ")</td>");
        TebUtilities.println(bw, "                <td class=\"legend\" align=\"center\" width=\"80px\">PermMem(b)</td>");
        TebUtilities.println(bw, "            </tr>");
        long tps;
        String href = "";
        for (int i = 0, n = results.length; i < n; i++) {
            href = results[i].getSite();
            if (href != null && href.trim().length() > 0) {
                href = "<a href=\"" + href + "\">" + results[i].getName() + "</a>";
            } else {
                href = results[i].getName();
            }
            tps = looped * thread * 1000 / (results[i].getTime() == 0 ? 1 : results[i].getTime());
            TebUtilities.println(bw, "        <tr class=\"" + (i % 2 == 0 ? "even" : "odd") + "\">");
            TebUtilities.println(bw, "            <td class=\"legend\" valign=\"center\" align=\"center\" width=\"16px\"><div class=\"legend\" style=\"background-color:" + COLORS[i] + "\"></td>");
            TebUtilities.println(bw, "            <td class=\"legend\">" + href + "</td>");
            TebUtilities.println(bw, "            <td class=\"legend\" align=\"right\">" + NDF.format(tps) + "</td>");
            TebUtilities.println(bw, "            <td class=\"legend\" align=\"right\">" + NDF.format(results[i].getTime()) + "</td>");
            TebUtilities.println(bw, "            <td class=\"legend\" align=\"right\">" + NDF.format(results[i].getOnceIo()) + "</td>");
            TebUtilities.println(bw, "            <td class=\"legend\" align=\"right\">" + NDF.format(results[i].getMassIo()) + "</td>");
            TebUtilities.println(bw, "            <td class=\"legend\" align=\"right\">" + NDF.format(results[i].getOnceOut()) + "</td>");
            TebUtilities.println(bw, "            <td class=\"legend\" align=\"right\">" + NDF.format(results[i].getMassOut()) + "</td>");
            TebUtilities.println(bw, "            <td class=\"legend\" align=\"right\">" + NDF.format(results[i].getPermMem()) + "</td>");
            TebUtilities.println(bw, "        </tr>");
        }
        TebUtilities.println(bw, "        </table>");
        TebUtilities.println(bw, "    </td>");
        TebUtilities.println(bw, "</tr>");
        TebUtilities.println(bw, "<tr>");
        TebUtilities.println(bw, "    <td colspan=\"4\">");
        TebUtilities.println(bw, "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"info\" align=\"center\">");
        TebUtilities.println(bw, "        <tr class=\"head\">");
        TebUtilities.println(bw, "            <td class=\"legend\" align=\"center\">TestDate</td>");
        TebUtilities.println(bw, "            <td class=\"legend\" align=\"center\" width=\"100px\">JavaVM</td>");
        TebUtilities.println(bw, "            <td class=\"legend\" align=\"center\" width=\"80px\">Thread</td>");
        TebUtilities.println(bw, "            <td class=\"legend\" align=\"center\" width=\"80px\">Record</td>");
        TebUtilities.println(bw, "            <td class=\"legend\" align=\"center\" width=\"80px\">Period</td>");
        TebUtilities.println(bw, "            <td class=\"legend\" align=\"center\" width=\"80px\">Warmed</td>");
        TebUtilities.println(bw, "            <td class=\"legend\" align=\"center\" width=\"80px\">Looped</td>");
        TebUtilities.println(bw, "            <td class=\"legend\" align=\"center\" width=\"80px\">Target</td>");
        TebUtilities.println(bw, "            <td class=\"legend\" align=\"center\" width=\"80px\">Stream</td>");
        TebUtilities.println(bw, "        <tr>");
        TebUtilities.println(bw, "        <tr class=\"even\">");
        TebUtilities.println(bw, "            <td class=\"legend\">" + date + "</td>");
        TebUtilities.println(bw, "            <td class=\"legend\">" + version + "</td>");
        TebUtilities.println(bw, "            <td class=\"legend\" align=\"right\">" + NDF.format(thread) + "</td>");
        TebUtilities.println(bw, "            <td class=\"legend\" align=\"right\">" + NDF.format(record) + "</td>");
        TebUtilities.println(bw, "            <td class=\"legend\" align=\"right\">" + NDF.format(period) + "</td>");
        TebUtilities.println(bw, "            <td class=\"legend\" align=\"right\">" + NDF.format(warmed) + "</td>");
        TebUtilities.println(bw, "            <td class=\"legend\" align=\"right\">" + NDF.format(looped) + "</td>");
        TebUtilities.println(bw, "            <td class=\"legend\" align=\"right\">" + target + "</td>");
        TebUtilities.println(bw, "            <td class=\"legend\" align=\"right\">" + (binary ? "byte" : "char") + "</td>");
        TebUtilities.println(bw, "        <tr>");
        TebUtilities.println(bw, "        <tr class=\"odd\">");
        TebUtilities.println(bw, "            <td class=\"legend\" colspan=\"9\"><span style=\"font-weight:600;color:#FF0000;\">Option: </span>" + option + "</td>");
        TebUtilities.println(bw, "        </tr>");
        TebUtilities.println(bw, "        </table>");
        TebUtilities.println(bw, "    </td>");
        TebUtilities.println(bw, "</tr>");

        TebUtilities.println(bw, "</table>");
    }

    private static void writePanel(BufferedWriter bw, TebResult[] results, PanelRender render) throws Exception {
        long max = Long.MIN_VALUE;
        for (int i = 0, n = results.length; i < n; i++) {
            max = Math.max(max, render.getValue(results, i));
        }
        TebUtilities.println(bw, "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"panel\">");
        TebUtilities.println(bw, "        <tr><td colspan=\"" + results.length + "\" class=\"title\">" + VERSION + ": " + render.getTitle() + "</tr>");
        TebUtilities.println(bw, "        <tr>");
        long height, value;
        for (int i = 0, n = results.length; i < n; i++) {
            value = render.getValue(results, i);
            height = value * 100 / (max == 0 ? 100 : max);
            TebUtilities.println(bw, "            <td valign=\"bottom\"><div class=\"panel\" style=\"height:" + height + "px;background-color:" + COLORS[i] + "\" title=\"" + results[i].getName() + "=" + NDF.format(value) + "\"></td>");
        }
        TebUtilities.println(bw, "        </tr>");
        TebUtilities.println(bw, "        </table>");
    }
}
