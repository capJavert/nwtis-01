package org.foi.nwtis.antbaric.zadaca_1.models;

/**
 * Created by javert on 25/03/2017.
 */
public class Status {
    private String name;

    public Status() {
        this.name = "IDLE";
    }

    public String get() {
        return name;
    }

    public void set(String name) {
        this.name = name;
    }
}
