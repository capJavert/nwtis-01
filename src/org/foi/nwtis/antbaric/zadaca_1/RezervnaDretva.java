package org.foi.nwtis.antbaric.zadaca_1;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.antbaric.konfiguracije.Konfiguracija;
import org.foi.nwtis.antbaric.zadaca_1.components.ErrorNwtis;

public class RezervnaDretva extends Thread {

    private final Konfiguracija konfiguracija;
    private Socket socket;

    public RezervnaDretva(final Konfiguracija konfiguracija) {
        this.konfiguracija = konfiguracija;
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
            } catch (InterruptedException ex) {
                Logger.getLogger(RezervnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                socket.getOutputStream().write(ErrorNwtis.getMessage("20").getBytes());
                socket.getOutputStream().flush();
                socket.shutdownOutput();
            } catch (final IOException ex) {
                Logger.getLogger(RezervnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    public synchronized void alert(Socket socket) {
        this.socket = socket;
        notify();
    }

}
