package kiang.teb;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2014 by kiang
 *
 * @author kiang
 * @version 0.1-pre
 */
public final class Performer implements Runnable {
    private Map data;
    private File file;
    private int period;
    private int looped;
    private String encoding;
    private TebEngine engine;
    private boolean binary;

    @Override
    public void run() {
        try {
            long launch, finish;
            TebEngine engine = this.engine;
            Map data = this.data;
            long[] ms = new long[this.looped / period + 1];
            if (this.binary) {
                if (engine.isBinarySupport()) {
                    ByteStream loopStream = new ByteStream();
                    launch = System.currentTimeMillis();
                    for (int i = 0, n = this.looped; i < n; i++) {
                        engine.test(data, loopStream);
                        if (i % period == 0) {
                            ms[i / period] = getMemory();
                        }
                    }
                    loopStream.flush();
                    finish = System.currentTimeMillis();
                    loopStream.close();
                } else {
                    LinkStream loopStream = new LinkStream(this.encoding);
                    launch = System.currentTimeMillis();
                    for (int i = 0, n = this.looped; i < n; i++) {
                        engine.test(data, loopStream);
                        if (i % period == 0) {
                            ms[i / period] = getMemory();
                        }
                    }
                    loopStream.flush();
                    finish = System.currentTimeMillis();
                    loopStream.close();
                }
            } else {
                CharStream loopStream = new CharStream(this.encoding);
                launch = System.currentTimeMillis();
                for (int i = 0, n = this.looped; i < n; i++) {
                    engine.test(data, loopStream);
                    if (i % period == 0) {
                        ms[i / period] = getMemory();
                    }
                }
                loopStream.flush();
                finish = System.currentTimeMillis();
                loopStream.close();
            }
            TebUtilities.println(this.file, "bot:" + launch, "eot:" + finish);
            for (int i = 0, n = ms.length; i < n; i++) {
                TebUtilities.println(this.file, "mfs:" + ms[i]);
            }
            TebUtilities.println(this.file, "mfs:" + getMemory());
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String args[]) throws Exception {
        test(new String[]{
                System.getenv("binary"),
                System.getenv("thread"),
                System.getenv("record"),
                System.getenv("period"),
                System.getenv("warmed"),
                System.getenv("looped"),
                System.getenv("locate"),
                System.getenv("source"),
                System.getenv("target"),
                System.getenv("tester"),
                System.getenv("simple")
        });
    }

    public static void test(String args[]) throws Exception {
        boolean binary = Boolean.parseBoolean(args[0]);
        int thread = Integer.parseInt(args[1]);
        int record = Integer.parseInt(args[2]);
        int period = Integer.parseInt(args[3]);
        int warmed = Integer.parseInt(args[4]);
        int looped = Integer.parseInt(args[5]);
        String locate = args[6];
        String source = args[7];
        String target = args[8];
        String tester = args[9];
        String simple = args[10];

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        TebEngine engine = (TebEngine) classLoader.loadClass(simple).newInstance();

        Properties properties = new Properties();
        properties.setProperty("source", source);
        properties.setProperty("target", target);
        properties.setProperty("binary", String.valueOf(binary));
        engine.init(properties);

        Map data = new HashMap();
        data.put("target", target);
        data.put("models", TebModel.dummyModels(record));

        long onceIo, onceOut;
        if (binary) {
            if (engine.isBinarySupport()) {
                ByteStream warmStream = new ByteStream();
                for (int i = 0; i < warmed; i++) {
                    engine.test(data, warmStream);
                }
                warmStream.flush();
                warmStream.close();
                onceIo = warmed == 0 ? 0 : warmStream.count() / warmed;
                onceOut = warmed == 0 ? 0 : warmStream.size() / warmed;
            } else {
                LinkStream warmStream = new LinkStream(target);
                for (int i = 0; i < warmed; i++) {
                    engine.test(data, warmStream);
                }
                warmStream.flush();
                warmStream.close();
                onceIo = warmed == 0 ? 0 : warmStream.count() / warmed;
                onceOut = warmed == 0 ? 0 : warmStream.size() / warmed;
            }
        } else {
            CharStream warmStream = new CharStream(target);
            for (int i = 0; i < warmed; i++) {
                engine.test(data, warmStream);
            }
            warmStream.flush();
            warmStream.close();
            onceIo = warmed == 0 ? 0 : warmStream.count() / warmed;
            onceOut = warmed == 0 ? 0 : warmStream.size() / warmed;
        }
        long massIo = onceIo * looped * thread;
        long massOut = onceOut * looped * thread;
        File file = new File(locate, tester + ".txt");
        TebUtilities.println(file, "oio:" + onceIo, "mio:" + massIo);
        TebUtilities.println(file, "obs:" + onceOut, "mbs:" + massOut);
        File tot = new File(locate, "TOT-" + tester + ".html");
        OutputStream output = new FileOutputStream(tot);
        if (binary && engine.isBinarySupport()) {
            engine.test(data, output);
            output.flush();
            output.close();
        } else {
            Writer writer = new OutputStreamWriter(output, target);
            engine.test(data, writer);
            writer.flush();
            writer.close();
        }

        Performer performer;
        if (thread == 1) {
            performer = new Performer();
            performer.data = data;
            performer.file = file;
            performer.looped = looped;
            performer.period = period;
            performer.binary = binary;
            performer.engine = engine;
            performer.encoding = target;
            performer.run();
            engine.shut();
            return;
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(Math.min(thread, 200));
        for (int i = 0; i < thread; i++) {
            performer = new Performer();
            performer.data = data;
            performer.file = file;
            performer.period = period;
            performer.looped = looped;
            performer.binary = binary;
            performer.engine = engine;
            performer.encoding = target;
            threadPool.execute(performer);
        }
        threadPool.shutdown();
        // wait thread pool finish.
        while (!threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS)) ;
        engine.shut();
    }

    private static long getMemory() {
        long used = 0;
        String name;
        MemoryPoolMXBean bean;
        List<MemoryPoolMXBean> mms = ManagementFactory.getMemoryPoolMXBeans();
        for (int i = 0, n = mms.size(); i < n; i++) {
            bean = mms.get(i);
            name = bean.getName();
            if (!name.contains("Eden")) {
                used += bean.getUsage().getUsed();
            }
        }
        return used;
    }
}
