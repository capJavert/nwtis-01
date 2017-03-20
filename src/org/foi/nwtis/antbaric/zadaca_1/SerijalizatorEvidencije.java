package org.foi.nwtis.antbaric.zadaca_1;

import org.foi.nwtis.antbaric.konfiguracije.Konfiguracija;

public class SerijalizatorEvidencije extends Thread {

    private final Konfiguracija konfiguracija;

    public SerijalizatorEvidencije(final Konfiguracija konfiguracija) {
        this.konfiguracija = konfiguracija;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void run() {
        //dovrsiti
    }

    @Override
    public synchronized void start() {
        super.start();
    }

}
