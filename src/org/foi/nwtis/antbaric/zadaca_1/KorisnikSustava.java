package org.foi.nwtis.antbaric.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import org.foi.nwtis.antbaric.zadaca_1.components.AdministratorSustava;
import org.foi.nwtis.antbaric.zadaca_1.components.KlijentSustava;
import org.foi.nwtis.antbaric.zadaca_1.components.SyntaxValidator;

public class KorisnikSustava {

    public static void main(String[] args) {
        boolean load = false;
        final String serverName;
        final int port;

        Matcher m = SyntaxValidator.validateArguments(args);

        if (m != null) {
            System.out.println(m.group(0));

            KorisnikSustava serverSustava = new KorisnikSustava();
            serverSustava.startUser(m);

        } else {
            System.out.println("Ne odgovara!");
        }

    }

    private void startUser(Matcher params) {
        String serverName;
        Integer port;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        Socket socket = null;

        try {
            final ArrayList<String> commandParts = new ArrayList<>();

            switch (params.group(1)) {
                case "-admin":
                    commandParts.add(params.group(8));
                    commandParts.add(params.group(9));
                    commandParts.add(params.group(10).trim());

                    serverName = params.group(6);
                    port = Integer.parseInt(params.group(7));
                    socket = new Socket(serverName, port);

                    AdministratorSustava admin = new AdministratorSustava();
                    admin.connect(socket, this.buildCommand(commandParts));
                    break;
                case "-korisnik":
                    if(params.group(10) != null) {
                        String[] param = params.group(9).split(" ");
                        commandParts.add(params.group(8));
                        commandParts.add(param[1]);
                        commandParts.add(param[0]);
                    } else {
                        String[] param = params.group(19).split(" ");
                        commandParts.add(params.group(8));
                        commandParts.add(param[1]);
                        commandParts.add(param[0]);
                    }

                    serverName = params.group(6);
                    port = Integer.parseInt(params.group(7));
                    socket = new Socket(serverName, port);

                    KlijentSustava client = new KlijentSustava();
                    client.connect(socket, this.buildCommand(commandParts));
                    break;
                case "-prikaz":
                    commandParts.add(params.group(1));
                    commandParts.add(params.group(2));
            }
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

    private String buildCommand(ArrayList<String> params) {
        String command = "";

        for(String p : params) {
            command += (p + ";");
        }

        return command;
    }
}
