package org.foi.nwtis.antbaric.zadaca_1.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by javert on 26/03/2017.
 */
class WaitTimerTest {
    private WaitTimer timer;

    @BeforeEach
    void setUp() {
        this.timer = new WaitTimer();
    }

    /**
     * Test if timeout is initiated
     */
    @Test
    void getTimeout() {
        assert this.timer.getTimeout() == 0;
    }

    /**
     * Test what happens if timeout is se as negative value
     */
    @Test
    void start() {
        this.timer.start(-1);
        assert this.timer.getTimeout() > 0;
    }

    /**
     * Test stop method
     */
    @Test
    void click() {
        this.timer.stop();

        assert this.timer.getTimeout() == 0;
    }

}