package org.foi.nwtis.antbaric.zadaca_1.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by javert on 26/03/2017.
 */
class ErrorNwtisTest {
    @Test
    void getMessage() {
        assert !ErrorNwtis.getMessage("-1").equals("");
    }

}