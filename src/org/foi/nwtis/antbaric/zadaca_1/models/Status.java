package org.foi.nwtis.antbaric.zadaca_1.models;

/**
 * Created by javert on 25/03/2017.
 */
public class Status {
    private String name;
    private Integer logItemsCount;

    public Status() {

        this.name = "IDLE";
        this.logItemsCount = 0;
    }

    public String get() {
        return name;
    }

    public void set(String name) {
        this.name = name;
    }

    public Integer getLogItemsCount() {
        return logItemsCount;
    }

    public void resetLogItemCount() {
        this.logItemsCount = 0;
    }

    public void setLogItemsCount() {
        this.logItemsCount++;
    }
}
