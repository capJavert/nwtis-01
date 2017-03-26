package org.foi.nwtis.antbaric.zadaca_1.components;

public class WaitTimer {
    private int timeout = 0;

    /**
     *
     * @return
     */
    public int getTimeout() {

        return timeout;
    }

    /**
     *
     * @param timeout
     */
    public void start(int timeout) {
        this.timeout = timeout;
    }

    /**
     *
     * @throws InterruptedException
     */
    public void click() throws InterruptedException {
        Thread.sleep(1000);
        this.timeout--;
    }

    private void stop() {

        this.timeout = 0;
    }
}
