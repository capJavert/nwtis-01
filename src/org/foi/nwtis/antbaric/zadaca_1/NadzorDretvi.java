package org.foi.nwtis.antbaric.zadaca_1;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.antbaric.konfiguracije.Konfiguracija;

public class NadzorDretvi extends Thread {

    private final Konfiguracija konfiguracija;
    private List<RadnaDretva> threads;

    public NadzorDretvi(final Konfiguracija konfiguracija, List<RadnaDretva> threads) {

        this.konfiguracija = konfiguracija;
        this.threads = threads;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public synchronized void run() {
        int sleepTime = Integer.parseInt(konfiguracija.dajPostavku("intervalNadzorneDretve"));
        int threadRunningLimit = Integer.parseInt(konfiguracija.dajPostavku("maksVrijemeRadneDretve"));

        while (true) {
            try {
                long currentTime = System.currentTimeMillis();

                // maknuti
                System.out.println(getClass().toString());

                synchronized (this.threads) {
                    Iterator<RadnaDretva> iter = this.threads.iterator();

                    while (iter.hasNext()) {
                        RadnaDretva thread = iter.next();

                        if (System.currentTimeMillis() - thread.startTime > threadRunningLimit) {
                            thread.kill();
                            iter.remove();
                        }
                    }
                }

                sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(NadzorDretvi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

}
