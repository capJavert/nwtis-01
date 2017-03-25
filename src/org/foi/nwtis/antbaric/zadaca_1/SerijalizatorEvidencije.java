package org.foi.nwtis.antbaric.zadaca_1;

import org.foi.nwtis.antbaric.konfiguracije.Konfiguracija;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerijalizatorEvidencije extends Thread {

    private final Konfiguracija konfiguracija;
    public Evidencija log;

    public SerijalizatorEvidencije(final Konfiguracija konfiguracija, Evidencija log) {

        this.konfiguracija = konfiguracija;
        this.log = log;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public synchronized void run() {
        while (true) {
            try {
                wait();

                this.serializeThisShit();

            } catch (InterruptedException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    private void serializeThisShit() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(this.log);
            out.flush();
            byte[] data = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(this.konfiguracija.dajPostavku("evidDatoteka"));
            fos.write(data);
            fos.close();

        } catch (IOException exception){
            Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
}
