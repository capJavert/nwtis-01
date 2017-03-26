package org.foi.nwtis.antbaric.zadaca_1;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.antbaric.konfiguracije.Konfiguracija;

public class ProvjeraAdresa extends Thread {

    private final Konfiguracija konfiguracija;
    public Evidencija log;

    public ProvjeraAdresa(final Konfiguracija konfiguracija, Evidencija log) {

        this.konfiguracija = konfiguracija;
        this.log = log;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public synchronized void run() {
        int sleepTime = Integer.parseInt(konfiguracija.dajPostavku("intervaAdresneDretve"));

        while (true) {
            System.out.println("ProvjeraAdresa...");

            try {
                synchronized (this.log) {
                    for(Map.Entry<String, Boolean> item : this.log.getAddresses().entrySet()) {
                        item.setValue(this.testAddress(item.getKey()));
                    }
                }
                sleep(sleepTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProvjeraAdresa.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    private boolean testAddress(String address) {
        try {
            URL u = new URL (address);
            HttpURLConnection huc =  ( HttpURLConnection )  u.openConnection ();
            huc.setRequestMethod ("GET");
            huc.connect();

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
