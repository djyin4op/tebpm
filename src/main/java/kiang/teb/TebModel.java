package kiang.teb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2014 by kiang
 *
 * @author kiang
 * @version 0.1-pre
 */
public final class TebModel {
    private int code;
    private String name;
    private Date date;
    private double value;

    private TebModel() {
    }

    public static List<TebModel> dummyModels(int count) {
        long step = 24 * 60 * 60 * 1000;
        long time = new Date().getTime() - count * step;
        final List<TebModel> models = new ArrayList<TebModel>(count);
        TebModel model;
        for (int i = 0; i < count; i++) {
            model = new TebModel();
            model.code = 1000 + i;
            model.name = "\u6D4B\u8BD5\u6A21\u578B-" + i;
            model.date = new Date(time + i * step);
            model.value = 100d + ((double) i) / 2;
            models.add(model);
        }
        return models;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
