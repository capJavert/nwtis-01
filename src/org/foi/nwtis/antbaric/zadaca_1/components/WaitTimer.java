package org.foi.nwtis.antbaric.zadaca_1.components;

public class WaitTimer {
    private int timeout = 0;

    public int getTimeout() {
        return timeout;
    }

    public void start(int timeout) {
        this.timeout = timeout;
    }

    public void click() {
        this.timeout--;

        this.stop();
    }

    private void stop() {
        this.timeout = 0;
    }
}
