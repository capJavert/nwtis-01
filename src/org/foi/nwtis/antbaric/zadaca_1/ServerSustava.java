package org.foi.nwtis.antbaric.zadaca_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.antbaric.konfiguracije.Konfiguracija;
import org.foi.nwtis.antbaric.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.antbaric.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.antbaric.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.antbaric.zadaca_1.components.SyntaxValidator;

public class ServerSustava {

    public static void main(String[] args) {
        boolean load = false;
        final String fileName;

        Matcher m = SyntaxValidator.validate(args);

        if (m != null) {
            int poc = 0;
            int kraj = m.groupCount();
            for (int i = poc; i <= kraj; i++) {
                System.out.println(i + ". " + m.group(i));
            }

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

            final NadzorDretvi nadzorDretvi = new NadzorDretvi(konfiguracija);
            nadzorDretvi.start();

            final RezervnaDretva rezervnaDretva = new RezervnaDretva(konfiguracija);
            rezervnaDretva.start();

            final ProvjeraAdresa provjeraAdresa = new ProvjeraAdresa(konfiguracija);
            provjeraAdresa.start();

            final SerijalizatorEvidencije serijalizatorEvidencije = new SerijalizatorEvidencije(konfiguracija);
            serijalizatorEvidencije.start();

            final ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                Socket socket = serverSocket.accept();
                RadnaDretva radnaDretva = new RadnaDretva(socket);
                //todo dodaj dretvu u listu aktivnih dretvi
                radnaDretva.start();
                //provjeriti ima li mjesta za thread
            }
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija | IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
