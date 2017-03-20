package org.foi.nwtis.antbaric.zadaca_1;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.antbaric.konfiguracije.Konfiguracija;

public class RezervnaDretva extends Thread {

    private final Konfiguracija konfiguracija;

    public RezervnaDretva(final Konfiguracija konfiguracija) {
        this.konfiguracija = konfiguracija;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void run() {
        int sleepTime = Integer.parseInt(konfiguracija.dajPostavku("intervalNadzorneDretve"));

        while (true) {
            try {
                long currentTime = System.currentTimeMillis();
                //todo dovrsiti

                long finishTime = System.currentTimeMillis();
                sleep(sleepTime - (finishTime - currentTime));
            } catch (InterruptedException ex) {
                Logger.getLogger(RezervnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            }
            //todo kada izaci van iz petlje
            //todo kada izaci van iz petlje
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

}
