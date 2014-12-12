package kiang.teb;

import org.apache.commons.exec.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright (c) 2014 by kiang
 *
 * @author kiang
 * @version 0.1-pre
 */
public class Benchmark {
    public static void main(String[] args) throws Exception {
        String date = new SimpleDateFormat("yyy-MM-dd hh:mm:ss").format(new Date());
        Properties properties = TebUtilities.getProperties();
        boolean[] binaries;
        String stream = properties.getProperty("stream");
        if ("byte".equals(stream)) {
            binaries = new boolean[]{true};
        } else if ("char".equals(stream)) {
            binaries = new boolean[]{false};
        } else {
            binaries = new boolean[]{true, false};
        }
        String locate = properties.getProperty("locate");
        String[] targets = TebUtilities.splitToken(properties.getProperty("target"), ";");
        String[] engines = TebUtilities.splitToken(properties.getProperty("engine"), ";");
        File rst;
        String name;
        TebResult[] results = new TebResult[engines.length];
        File rpt = new File(locate, "RPT" + properties.getProperty("thread") + ".html");
        BufferedWriter bw = TebReport.writeHead(rpt, properties);
        try {
            for (int i = 0, r = targets.length; i < r; i++) {
                for (int j = 0, s = binaries.length; j < s; j++) {
                    for (int k = 0, t = engines.length; k < t; k++) {
                        name = properties.getProperty(engines[k] + ".name", "").trim();
                        System.out.println("Test [" + engines[k] + "=" + name + "]@[" + targets[i] + "]@[" + (binaries[j] ? "byte" : "char") + " stream]...");
                        rst = new File(locate, engines[k] + ".txt");
                        rst.delete();
                        // change to common-exec

                        if (!bench(properties, binaries[j], targets[i], engines[k])) {
                            System.err.println("Test [" + engines[k] + "=" + name + "]@[" + targets[i] + "]@[" + (binaries[j] ? "byte" : "char") + " stream] is Failed.");
                            continue;
                        }
                        Benchmark.readResult(rst, results[k] = new TebResult());
                        rst.delete();
                        results[k].setName(name);
                        results[k].setSite(properties.getProperty(engines[k] + ".site", "").trim());
                    }
                    Arrays.sort(results, new Comparator<TebResult>() {
                        @Override
                        public int compare(TebResult o1, TebResult o2) {
                            if (o1.getTime() < o2.getTime()) {
                                return -1;
                            } else if (o1.getTime() > o2.getTime()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
                    TebReport.writeBody(bw, properties, binaries[j], targets[i], results, date);
                }
            }
        } finally {
            TebReport.writeTail(bw);
        }
    }


    private static void readResult(File rst, TebResult result) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(rst));
        List<Long> launch = new ArrayList<Long>();
        List<Long> finish = new ArrayList<Long>();
        List<Long> mfs = new ArrayList<Long>();
        try {
            String line;
            result.setOnceIo(Long.parseLong(br.readLine().substring(4)));
            result.setMassIo(Long.parseLong(br.readLine().substring(4)));
            result.setOnceOut(Long.parseLong(br.readLine().substring(4)));
            result.setMassOut(Long.parseLong(br.readLine().substring(4)));
            while ((line = br.readLine()) != null) {
                if (line.startsWith("bot:")) {
                    launch.add(Long.parseLong(line.substring(4)));
                } else if (line.startsWith("eot:")) {
                    finish.add(Long.parseLong(line.substring(4)));
                } else if (line.startsWith("mfs:")) {
                    mfs.add(Long.parseLong(line.substring(4)));
                }
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        Collections.sort(mfs);
        long mx = 0;
        int size = mfs.size();
        for (int i = 0; i < size; i++) {
            mx += mfs.get(i);
        }
        result.setPermMem(size == 0 ? 0 : mx / size);
        result.setTime(Collections.max(finish) - Collections.min(launch));
    }

    static final String argsTemplate = "-classpath \"%s\" %s";

    public static boolean bench(Properties properties, boolean binary, String target, String engine) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
        final ByteArrayOutputStream errsStream = new ByteArrayOutputStream(2048);
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errsStream);
        final CommandLine commandLine = new CommandLine(TebUtilities.JDK_HOME + File.separator + "bin/java");
        final String option = properties.getProperty("option");
        commandLine.addArguments(option);
        String arguments = String.format(argsTemplate, new String[]{TebUtilities.JDKCLASSPATH, Performer.class.getName()});
        commandLine.addArguments(arguments);
        Map<String, String> environments = new HashMap<String, String>();
        environments.put("binary", Boolean.toString(binary));
        environments.put("thread", properties.getProperty("thread", "1"));
        environments.put("record", properties.getProperty("record", "20"));
        environments.put("period", properties.getProperty("period", "10"));
        environments.put("warmed", properties.getProperty("warmed", "500"));
        environments.put("looped", properties.getProperty("looped", "10000"));
        environments.put("locate", properties.getProperty("locate", ""));
        environments.put("source", properties.getProperty("source", "UTF-8"));
        environments.put("target", target);
        environments.put("tester", engine);
        environments.put("simple", properties.getProperty(engine + ".test"));

        // create the executor and consider the exitValue '1' as success
        final Executor executor = new DefaultExecutor();
        executor.setExitValue(0);
        executor.setStreamHandler(streamHandler);
        ResultHandler handler = new ResultHandler(executor, outputStream, errsStream);
        //Infinite timeout
        ExecuteWatchdog watchDog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
        executor.setWatchdog(watchDog);

        try {
            executor.execute(commandLine, environments, handler);
            handler.waitFor();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static class ResultHandler extends DefaultExecuteResultHandler {

        final ByteArrayOutputStream outputStream;
        final ByteArrayOutputStream errsStream;
        final Executor executor;

        public ResultHandler(final Executor executor,
                             final ByteArrayOutputStream outputStream,
                             final ByteArrayOutputStream errsStream) {
            super();
            this.executor = executor;
            this.outputStream = outputStream;
            this.errsStream = errsStream;

        }

        @Override
        public void onProcessComplete(final int exitValue) {
            super.onProcessComplete(exitValue);
            ExecuteWatchdog dog =
                    executor.getWatchdog();
            if (dog != null) {
                dog.destroyProcess();
            }
            String encoding = System.getProperty("sun.jnu.encoding");
            try {
                String error = errsStream.toString(encoding);
                String out = outputStream.toString(encoding);
                System.out.println(out);
                System.err.println(error);
            } catch (UnsupportedEncodingException e) {
                // ignored
            } finally {
                IOUtils.closeQuietly(outputStream);
                IOUtils.closeQuietly(errsStream);
            }

        }

        @Override
        public void onProcessFailed(final ExecuteException e) {
            super.onProcessFailed(e);
            ExecuteWatchdog dog =
                    executor.getWatchdog();
            if (dog != null) {
                dog.destroyProcess();
            }
            String encoding = System.getProperty("sun.jnu.encoding");
            try {
                String error = errsStream.toString(encoding);
                String out = outputStream.toString(encoding);
                System.out.println(out);
                System.err.println(error);
            } catch (UnsupportedEncodingException ue) {
                // ignored
            } finally {
                IOUtils.closeQuietly(outputStream);
                IOUtils.closeQuietly(errsStream);
            }
        }
    }
}
