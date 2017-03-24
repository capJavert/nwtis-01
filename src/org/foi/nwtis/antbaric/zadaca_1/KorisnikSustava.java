package org.foi.nwtis.antbaric.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import org.foi.nwtis.antbaric.zadaca_1.components.SyntaxValidator;

public class KorisnikSustava {

    public static void main(String[] args) {
        boolean load = false;
        final String serverName;
        final int port;

        Matcher m = SyntaxValidator.validateArguments(args);

        if (m != null) {
            int poc = 0;
            int kraj = m.groupCount();
            for (int i = poc; i <= kraj; i++) {
                if(m.group(i) != null) {
                    System.out.println(i + ". " + m.group(i));
                }
            }

            serverName = m.group(1);
            port = Integer.parseInt(m.group(2));

            KorisnikSustava serverSustava = new KorisnikSustava();
            serverSustava.startUser(serverName, port, m);

        } else {
            System.out.println("Ne odgovara!");
        }

    }

    private void startUser(final String serverName, final int port, Matcher params) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        Socket socket = null;
        try {
            socket = new Socket(serverName, port);

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            final String command = this.buildCommand(params);

            outputStream.write(command.getBytes());
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

    private String buildCommand(Matcher params) {
        // TODO: Implement command builder with regexp or something
        return "command";
    }
}
