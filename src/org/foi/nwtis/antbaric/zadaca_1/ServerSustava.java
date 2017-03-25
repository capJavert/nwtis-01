package org.foi.nwtis.antbaric.zadaca_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import org.foi.nwtis.antbaric.konfiguracije.Konfiguracija;
import org.foi.nwtis.antbaric.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.antbaric.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.antbaric.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.antbaric.zadaca_1.components.SyntaxValidator;
import org.foi.nwtis.antbaric.zadaca_1.models.Status;

public class ServerSustava {
    public static Evidencija log;
    public static List<RadnaDretva> threads;
    public static Status state;

    public static void main(String[] args) {
        boolean load = false;
        final String fileName;

        Matcher m = SyntaxValidator.validateArguments(args);

        if (m != null) {
            System.out.println(m.group(0));

            fileName = m.group(1) + m.group(2);

            if (m.group(3) != null) {
                load = true;
            }
            ServerSustava serverSustava = new ServerSustava();
            serverSustava.startServer(fileName, load);

        } else {
            System.out.println("Ne odgovara!");
        }

    }

    private void startServer(final String fileName, final boolean load) {
        //todo kreirati listu dretvi

        try {
            final Konfiguracija konfiguracija = KonfiguracijaApstraktna.preuzmiKonfiguraciju(fileName);
            final int port = Integer.parseInt(konfiguracija.dajPostavku("port"));

            log = new Evidencija();
            threads = new ArrayList<>();
            state = new Status();

            final NadzorDretvi nadzorDretvi = new NadzorDretvi(konfiguracija, threads);
            nadzorDretvi.start();

            final RezervnaDretva rezervnaDretva = new RezervnaDretva(konfiguracija);
            rezervnaDretva.start();

            final ProvjeraAdresa provjeraAdresa = new ProvjeraAdresa(konfiguracija, log);
            provjeraAdresa.start();

            final SerijalizatorEvidencije serijalizatorEvidencije = new SerijalizatorEvidencije(konfiguracija);
            serijalizatorEvidencije.start();

            final ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                Socket socket = serverSocket.accept();

                synchronized (log) {
                    this.log.addRequest(socket.getInetAddress().toString());
                }

                if(threads.size() < Integer.parseInt(konfiguracija.dajPostavku("maksBrojRadnihDretvi"))) {
                    RadnaDretva radnaDretva = new RadnaDretva(socket, konfiguracija, state, log);

                    synchronized (threads) {
                        threads.add(radnaDretva);
                    }
                    radnaDretva.start();
                } else {
                    rezervnaDretva.alert(socket);
                }

                synchronized (threads) {
                    Iterator<RadnaDretva> iter = threads.iterator();

                    while (iter.hasNext()) {
                        RadnaDretva thread = iter.next();

                        if (thread.isInterupted) {
                            iter.remove();
                        }
                    }
                }
            }
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija | IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
