package org.foi.nwtis.antbaric.zadaca_1.components;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by javert on 25/03/2017.
 */
public class KlijentSustava {
    public void connect(Socket socket, String command) throws IOException {
        System.out.println(command);

        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

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
    }
}
