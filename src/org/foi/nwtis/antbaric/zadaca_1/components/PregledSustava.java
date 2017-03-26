package org.foi.nwtis.antbaric.zadaca_1.components;

import org.foi.nwtis.antbaric.konfiguracije.KonfiguracijaBin;
import org.foi.nwtis.antbaric.zadaca_1.Evidencija;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by javert on 25/03/2017.
 */
public class PregledSustava {
    /**
     *
     * @param command
     * @throws IOException
     */
    public void connect(String command) throws IOException {
        String[] commandLine = command.split(";");
        String filePath;
        Evidencija log = null;

        if(commandLine[0].equals("REMOTE")) {
            URL url = new URL(commandLine[2]);
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("temp.bin");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            filePath = "temp.bin";
        } else {
            filePath = commandLine[2];
        }

        File file = new File(filePath);

        if(file.exists() && file.isFile()) {
            try {
                ObjectInputStream ex = new ObjectInputStream(new FileInputStream(file));
                log = (Evidencija) ex.readObject();
            } catch (Exception e) {
                Logger.getLogger(KonfiguracijaBin.class.getName()).log(Level.SEVERE, "Datoteka nije ispravna", (Exception)null);
            }
        } else {
            Logger.getLogger(KonfiguracijaBin.class.getName()).log(Level.SEVERE, "Ne postoji datoteka", (Exception)null);
        }

        if(log != null) {
            log.outputLog();
        } else {
            Logger.getLogger(KonfiguracijaBin.class.getName()).log(Level.SEVERE, "Evidencija nije ispravna", (Exception)null);
        }

    }
}
