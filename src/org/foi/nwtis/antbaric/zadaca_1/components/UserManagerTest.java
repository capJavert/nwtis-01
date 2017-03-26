package org.foi.nwtis.antbaric.zadaca_1.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by javert on 26/03/2017.
 */
class UserManagerTest {
    private UserManager userManager;

    @BeforeEach
    void setUp() throws IOException{
        this.userManager = new UserManager("DZ_1_administratori.txt");
    }

    /**
     * Test admin users file
     */
    @Test
    void getUsers() {
        assert this.userManager.getUsers() != null;
    }

    /**
     * Test finding a User
     */
    @Test
    void findOne() {
        assert this.userManager.findOne("pero", "123456") != null;
    }

}