package org.foi.nwtis.antbaric.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RadnaDretva extends Thread {

    private final Socket socket;
    //todo varijabla koja pamti kad je pocela dretva

    public RadnaDretva(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void run() {
        //todo puniti varijablu za trenutno vrijeme
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            StringBuffer buffer = new StringBuffer();

            while (true) {
                int znak = inputStream.read();
                if (znak == -1) {
                    break;
                } else {
                    buffer.append((char) znak);
                }
            }
            System.out.println(buffer);
            final String adminRegex = "^USER ([^\\s]+); PASSWD ([^\\s]+); (PAUSE|STOP|START|STAT);$";
            final String korisnikRegex1 = "^USER ([^\\s]+); ADD ([^\\s]+);$";
            final String korisnikRegex2 = "^USER ([^\\s]+); TEST ([^\\s]+);$";
            final String korisnikRegex3 = "^USER ([^\\s]+); WAIT ([^\\d]+);$";
            //todo provjeri ispravnost primljenog zahtjeva
     
            Pattern pattern = Pattern.compile(adminRegex);
            Matcher m = pattern.matcher(buffer);
            boolean status = m.matches();
            
            if(status){
                //admin
            } else {
                //dovrsiti
            }

            outputStream.write("OK;".getBytes());
            outputStream.flush();

        } catch (final IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //todo azuriraj evidenciju rada
        //todo obrisati dretvu iz liste
        //todo smanjiti brojac, medusobno iskljucivanje
    }

    @Override
    public synchronized void start() {
        super.start();
    }

}
