package org.foi.nwtis.antbaric.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

public class KorisnikSustava {

    public static void main(String[] args) {
        boolean load = false;
        final String serverName;
        final int port;

        Matcher m = SyntaxValidator.validate(args);

        if (m != null) {
            int poc = 0;
            int kraj = m.groupCount();
            for (int i = poc; i <= kraj; i++) {
                System.out.println(i + ". " + m.group(i));
            }

            serverName = m.group(1);
            port = Integer.parseInt(m.group(2));

            KorisnikSustava serverSustava = new KorisnikSustava();
            serverSustava.startUser(serverName, port);

        } else {
            System.out.println("Ne odgovara!");
        }

    }

    private void startUser(final String serverName, final int port) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        Socket socket = null;
        try {
            socket = new Socket(serverName, port);

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            final String zahtjev = "USER pero; PASSWD 123456; PAUSE;";

            outputStream.write(zahtjev.getBytes());
            outputStream.flush();
            socket.shutdownOutput();

            final StringBuffer buffer = new StringBuffer();

            while (true) {
                int znak = inputStream.read();
                if (znak == -1) {
                    break;
                } else {
                    buffer.append((char) znak);
                }
            }
            System.out.println(buffer);

        } catch (final IOException ex) {
            Logger.getLogger(KorisnikSustava.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                socket.close();
            } catch (final IOException ex) {
                Logger.getLogger(KorisnikSustava.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
