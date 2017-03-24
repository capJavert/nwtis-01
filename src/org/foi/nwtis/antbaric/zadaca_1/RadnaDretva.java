package org.foi.nwtis.antbaric.zadaca_1;

import org.foi.nwtis.antbaric.konfiguracije.Konfiguracija;
import org.foi.nwtis.antbaric.zadaca_1.components.ErrorNwtis;
import org.foi.nwtis.antbaric.zadaca_1.components.SyntaxValidator;
import org.foi.nwtis.antbaric.zadaca_1.components.UserManager;
import org.foi.nwtis.antbaric.zadaca_1.components.WaitTimer;
import org.foi.nwtis.antbaric.zadaca_1.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
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
    private Evidencija log;
    private WaitTimer waitTimer;
    //todo varijabla koja pamti kad je pocela dretva

    public RadnaDretva(Socket socket, Konfiguracija config, Evidencija log) throws IOException {
        this.log = log;
        this.socket = socket;
        this.config = config;
        this.state = "IDLE";
        this.waitTimer = new WaitTimer();
    }

    @Override
    public void interrupt() {

        super.interrupt();
    }

    @Override
    public synchronized void start() {

        super.start();
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
                        case "ADD;":
                            outputStream.write(execAdd(params).getBytes());
                            outputStream.flush();
                            break;
                        case "TEST;":
                            outputStream.write(execTest(params).getBytes());
                            outputStream.flush();
                            break;
                        case "WAIT":
                            outputStream.write(execWait(params).getBytes());
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
                this.state = "PAUSE";
                //this.wait();
            }
        } else {
            return ErrorNwtis.getMessage("00");
        }

        return "OK;";
    }

    private String execStart(String[] params) throws InterruptedException {
        User user = this.userManager.findOne(params[0], params[1]);

        if(user != null) {
            if(this.state.equals("PAUSE")) {
                this.state = "IDLE";
            } else {
                return ErrorNwtis.getMessage("02");
            }
        } else {
            return ErrorNwtis.getMessage("00");
        }

        return "OK;";
    }

    private String execStop(String[] params) {
        User user = this.userManager.findOne(params[0], params[1]);

        if(user != null) {
            if(this.state.equals("IDLE")) {
                this.interrupt();
            } else {
                return ErrorNwtis.getMessage("03");
            }
        } else {
            return ErrorNwtis.getMessage("00");
        }

        return "OK;";
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

        return "OK;";
    }
    
    private String execAdd(String[] params) {
        if(!this.log.findAddress(params[1])) {
            if(Integer.parseInt(this.config.dajPostavku("maksAdresa")) < this.log.addressCount) {
                this.log.setAddress(params[1]);
            } else {
                return ErrorNwtis.getMessage("10");
            }
        } else {
            return ErrorNwtis.getMessage("11");
        }

        return "OK;";
    }

    private String execTest(String[] params) {
        if(!this.log.findAddress(params[1])) {
            if(this.testAddress(params[1])) {
                return "OK; YES";
            } else {
                return "OK; NO";
            }
        } else {
            return ErrorNwtis.getMessage("12");
        }
    }

    private String execWait(String[] params) {
        if(this.state.equals("IDLE")) {
            this.waitTimer.start(Integer.parseInt(params[1]));

            while (this.waitTimer.getTimeout() > 0) {
                this.waitTimer.click();
            }
        } else {
            return ErrorNwtis.getMessage("13");
        }

        return "OK;";
    }

    private boolean testAddress(String address) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(address, 80), 3);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
