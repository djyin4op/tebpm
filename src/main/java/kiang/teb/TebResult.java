package kiang.teb;

/**
 * Copyright (c) 2014 by kiang
 *
 * @author kiang
 * @version 0.1-pre
 */
public final class TebResult {
    private String jdk;
    private String name;
    private String site;
    private long time;
    private long onceOut;
    private long massOut;
    private long onceIo;
    private long massIo;
    private long permMem;

    public String getJdk() {
        return jdk;
    }

    public void setJdk(String jdk) {
        this.jdk = jdk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public long getOnceOut() {
        return onceOut;
    }

    public void setOnceOut(long onceOut) {
        this.onceOut = onceOut;
    }

    public long getMassOut() {
        return massOut;
    }

    public void setMassOut(long massOut) {
        this.massOut = massOut;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getOnceIo() {
        return onceIo;
    }

    public void setOnceIo(long onceIo) {
        this.onceIo = onceIo;
    }

    public long getMassIo() {
        return massIo;
    }

    public void setMassIo(long massIo) {
        this.massIo = massIo;
    }

    public long getPermMem() {
        return permMem;
    }

    public void setPermMem(long permMem) {
        this.permMem = permMem;
    }
}
