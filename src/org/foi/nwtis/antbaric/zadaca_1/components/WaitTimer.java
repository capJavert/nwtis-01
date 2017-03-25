package org.foi.nwtis.antbaric.zadaca_1.components;

public class WaitTimer {
    private int timeout = 0;

    public int getTimeout() {

        return timeout;
    }

    public void start(int timeout) {
        this.timeout = timeout;
    }

    public void click() throws InterruptedException {
        Thread.sleep(1000);
        this.timeout--;
    }

    private void stop() {

        this.timeout = 0;
    }
}
