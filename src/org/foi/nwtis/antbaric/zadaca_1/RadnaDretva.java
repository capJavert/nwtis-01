package org.foi.nwtis.antbaric.zadaca_1;

import org.foi.nwtis.antbaric.konfiguracije.Konfiguracija;
import org.foi.nwtis.antbaric.zadaca_1.components.ErrorNwtis;
import org.foi.nwtis.antbaric.zadaca_1.components.SyntaxValidator;
import org.foi.nwtis.antbaric.zadaca_1.components.UserManager;
import org.foi.nwtis.antbaric.zadaca_1.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

public class RadnaDretva extends Thread {

    private final Socket socket;
    private final Konfiguracija config;
    private String state;
    private UserManager userManager;
    private OutputStream outputStream;
    private InputStream inputStream;
    //todo varijabla koja pamti kad je pocela dretva

    public RadnaDretva(Socket socket, Konfiguracija config) throws IOException {

        this.socket = socket;
        this.config = config;
        this.state = "IDLE";
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void run() {
        //todo puniti varijablu za trenutno vrijeme
        try {
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();

            try {
                this.userManager = new UserManager(this.config.dajPostavku("adminDatoteka"));
            } catch (IOException exception){
                System.out.println(exception.getMessage());
                outputStream.write(ErrorNwtis.getMessage("-1").getBytes());
            }

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

            Matcher m = SyntaxValidator.validate(buffer);

            if(m != null){
                String commandLine = buffer.toString();
                commandLine = commandLine.replace("USER ", "");
                commandLine = commandLine.replace("PASSWD ", "");
                String[] params = commandLine.split("; ");
                String command = params[params.length-1];

                try {
                    switch (command.replace(" ", "")) {
                        case "PAUSE;":
                            outputStream.write(execPause(params).getBytes());
                            outputStream.flush();
                            break;
                        case "START;":
                            outputStream.write(execStart(params).getBytes());
                            outputStream.flush();
                            break;
                        case "STOP;":
                            outputStream.write(execStop(params).getBytes());
                            outputStream.flush();
                            break;
                        case "STAT;":
                            outputStream.write(execStat(params).getBytes());
                            outputStream.flush();
                            break;
                        default:
                            outputStream.write(("ERROR: Nepoznata komanda " + command).getBytes());
                            outputStream.flush();
                    }
                } catch (InterruptedException exception) {
                    System.out.println("Pogreška kod rada s dretvom");
                }
            } else {
                System.out.println("NULL");
            }
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

    private String execPause(String[] params) throws InterruptedException {
        User user = this.userManager.findOne(params[0], params[1]);

        if(user != null) {
            if(this.state.equals("PAUSE")) {
                return ErrorNwtis.getMessage("01");
            } else {
                this.wait();
            }
        } else {
            return ErrorNwtis.getMessage("00");
        }

        return "OK";
    }

    private String execStart(String[] params) throws InterruptedException {
        User user = this.userManager.findOne(params[0], params[1]);

        if(user != null) {
            if(this.state.equals("PAUSE")) {
                this.notify();
            } else {
                return ErrorNwtis.getMessage("02");
            }
        } else {
            return ErrorNwtis.getMessage("00");
        }

        return "OK";
    }

    private String execStop(String[] params) {
        User user = this.userManager.findOne(params[0], params[1]);

        if(user != null) {
            if(this.state.equals("IDLE")) {
                //stop and log
            } else {
                return ErrorNwtis.getMessage("03");
            }
        } else {
            return ErrorNwtis.getMessage("00");
        }

        return "OK";
    }

    private String execStat(String[] params) {
        User user = this.userManager.findOne(params[0], params[1]);

        if(user != null) {
            if(!this.state.equals("IDLE")) {
                //return log
            } else {
                return ErrorNwtis.getMessage("04");
            }
        } else {
            return ErrorNwtis.getMessage("00");
        }

        return "OK";
    }

    @Override
    public synchronized void start() {
        super.start();
    }

}
