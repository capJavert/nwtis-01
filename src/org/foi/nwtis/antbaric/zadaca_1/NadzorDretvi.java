package org.foi.nwtis.antbaric.zadaca_1;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.antbaric.konfiguracije.Konfiguracija;

public class NadzorDretvi extends Thread {

    private final Konfiguracija konfiguracija;

    public NadzorDretvi(final Konfiguracija konfiguracija) {
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

                // maknuti
                System.out.println(getClass().toString());
                //todo dovrsiti
                //provjeriti koliko traje svaka dretva
                //todo obrisati dretvu iz kolekcije ako traje predugo
                long finishTime = System.currentTimeMillis();
                sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(NadzorDretvi.class.getName()).log(Level.SEVERE, null, ex);
            }
            //todo kada izaci van iz petlje
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

}
