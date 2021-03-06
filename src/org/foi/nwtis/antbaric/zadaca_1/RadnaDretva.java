package org.foi.nwtis.antbaric.zadaca_1;

import org.foi.nwtis.antbaric.konfiguracije.Konfiguracija;
import org.foi.nwtis.antbaric.zadaca_1.components.ErrorNwtis;
import org.foi.nwtis.antbaric.zadaca_1.components.UserManager;
import org.foi.nwtis.antbaric.zadaca_1.components.WaitTimer;
import org.foi.nwtis.antbaric.zadaca_1.models.Status;
import org.foi.nwtis.antbaric.zadaca_1.models.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RadnaDretva extends Thread {
    private final Socket socket;
    private final Konfiguracija config;
    private Status state;
    private UserManager userManager;
    private OutputStream outputStream;
    private InputStream inputStream;
    private Evidencija log;
    private WaitTimer waitTimer;
    public volatile boolean isInterupted;
    public volatile long startTime;

    public RadnaDretva(Socket socket, Konfiguracija config, Status state, Evidencija log) throws IOException {
        this.log = log;
        this.socket = socket;
        this.config = config;
        this.state = state;
        this.waitTimer = new WaitTimer();
        this.isInterupted = false;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void interrupt() {

        super.interrupt();
    }

    @Override
    public synchronized void start() {

        super.start();
    }

    public void kill() {
        this.stop();
    }

    @Override
    public void run() {
        String name = "antbaric-"+this.getId();

        try {
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();

            try {
                this.userManager = new UserManager(this.config.dajPostavku("adminDatoteka"));
            } catch (IOException exception){
                System.out.println(exception.getMessage());
                this.badRequest();
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

            //System.out.println(buffer);

            String[] params = this.readCommand(buffer.toString());
            String command = params[params.length-1];

            try {
                switch (command.replace(" ", "")) {
                    case "pause":
                        System.out.println(name + " -> USER " + params[0] + "; PASSWD " + params[1] + "; PAUSE;");
                        outputStream.write(execPause(params).getBytes());
                        outputStream.flush();
                        break;
                    case "start":
                        System.out.println(name + " -> USER " + params[0] + "; PASSWD " + params[1] + "; START;");
                        outputStream.write(execStart(params).getBytes());
                        outputStream.flush();
                        break;
                    case "stop":
                        System.out.println(name + " -> USER " + params[0] + "; PASSWD " + params[1] + "; STOP;");
                        outputStream.write(execStop(params).getBytes());
                        outputStream.flush();
                        break;
                    case "stat":
                        System.out.println(name + " -> USER " + params[0] + "; PASSWD " + params[1] + "; STAT;");
                        outputStream.write(execStat(params).getBytes());
                        outputStream.flush();
                        break;
                    case "-a":
                        if(!this.state.get().equals("IDLE")) break;

                        System.out.println(name + " -> USER " + params[0] + "; ADD " + params[1] + ";");
                        outputStream.write(execAdd(params).getBytes());
                        outputStream.flush();
                        break;
                    case "-t":
                        if(!this.state.get().equals("IDLE")) break;

                        System.out.println(name + " -> USER " + params[0] + "; TEST " + params[1] + ";");
                        outputStream.write(execTest(params).getBytes());
                        outputStream.flush();
                        break;
                    case "-w":
                        if(!this.state.get().equals("IDLE")) break;

                        System.out.println(name + " -> USER " + params[0] + "; WAIT " + params[1] + ";");
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

        this.isInterupted = true;

        synchronized (this.log) {
            this.log.workingThreadsRunningTime += (System.currentTimeMillis() - startTime);
            this.log.lastWorkingThreadId = this.getId();
        }
    }

    /**
     *
     * @param params
     * @return
     * @throws InterruptedException
     */
    private String execPause(String[] params) throws InterruptedException {
        User user = this.userManager.findOne(params[0], params[1]);

        if(user != null) {
            if(this.state.get().equals("PAUSE")) {
                this.badRequest();
                return ErrorNwtis.getMessage("01");
            } else {
                synchronized (this.state) {
                    this.state.set("PAUSE");
                }
            }
        } else {
            this.badRequest();
            return ErrorNwtis.getMessage("00");
        }

        this.okRequest();
        return "OK;";
    }

    /**
     *
     * @param params
     * @return
     * @throws InterruptedException
     */
    private String execStart(String[] params) throws InterruptedException {
        User user = this.userManager.findOne(params[0], params[1]);

        if(user != null) {
            if(this.state.get().equals("PAUSE")) {
                synchronized (this.state) {
                    this.state.set("IDLE");
                }
            } else {
                this.badRequest();
                return ErrorNwtis.getMessage("02");
            }
        } else {
            this.badRequest();
            return ErrorNwtis.getMessage("00");
        }

        this.okRequest();
        return "OK;";
    }

    /**
     *
     * @param params
     * @return
     */
    private String execStop(String[] params) {
        User user = this.userManager.findOne(params[0], params[1]);

        if(user != null) {
            try {
                this.okRequest();
                this.serializeThisShit();

                System.exit(0);

                return "OK;";
            } catch (IOException e) {
                this.badRequest();
                return ErrorNwtis.getMessage("03");
            }
        } else {
            this.badRequest();
            return ErrorNwtis.getMessage("00");
        }
    }

    /**
     *
     * @param params
     * @return
     */
    private String execStat(String[] params) {
        User user = this.userManager.findOne(params[0], params[1]);

        if(user != null) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = new ObjectOutputStream(bos);
                out.writeObject(this.log);
                out.flush();

                this.okRequest();
                return "OK; LENGTH " + bos.toByteArray().length + System.lineSeparator() + bos.toString();
            } catch (IOException e) {
                this.badRequest();
                return ErrorNwtis.getMessage("04");
            }
        } else {
            this.badRequest();
            return ErrorNwtis.getMessage("00");
        }
    }

    /**
     *
     * @param params
     * @return
     */
    private String execAdd(String[] params) {
        if(!this.log.findAddress(params[1])) {
            if(Integer.parseInt(this.config.dajPostavku("maksAdresa")) > this.log.getAddresses().size()) {
                synchronized (this.log) {
                    this.log.setAddress(params[1]);
                }
            } else {
                this.badRequest();
                return ErrorNwtis.getMessage("10");
            }
        } else {
            this.badRequest();
            return ErrorNwtis.getMessage("11");
        }

        this.okRequest();
        return "OK;";
    }

    /**
     *
     * @param params
     * @return
     */
    private String execTest(String[] params) {
        if(this.log.findAddress(params[1])) {
            this.okRequest();

            if(this.testAddress(params[1])) {
                return "OK; YES";
            } else {
                return "OK; NO";
            }
        } else {
            this.badRequest();
            return ErrorNwtis.getMessage("12");
        }
    }

    /**
     *
     * @param params
     * @return
     */
    private String execWait(String[] params) {
        try {
            this.waitTimer.start(Integer.parseInt(params[1]));

            outputStream.write("OK;".getBytes());
            outputStream.flush();
            while (this.waitTimer.getTimeout() > 0) {
                this.waitTimer.click();
            }
        } catch (Exception exception){
            this.badRequest();
            return ErrorNwtis.getMessage("13");
        }

        this.okRequest();
        return "OK;";
    }

    /**
     *
     * @param address
     * @return
     */
    private boolean testAddress(String address) {
        try {
            URL u = new URL (address);
            HttpURLConnection huc =  ( HttpURLConnection )  u.openConnection ();
            huc.setRequestMethod ("GET");
            huc.connect();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     *
     * @param command
     * @return
     */
    private String[] readCommand(String command) {


        return command.split(";");
    }

    public void badRequest() {
        synchronized (this.log) {
            this.log.badRequest();
        }

        synchronized (this.state) {
            this.state.setLogItemsCount();
        }
    }

    public void okRequest() {
        synchronized (this.log) {
            this.log.okRequest();
        }

        synchronized (this.state) {
            this.state.setLogItemsCount();
        }
    }

    /**
     *
     * @throws IOException
     */
    private void serializeThisShit() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(this.log);
        out.flush();
        byte[] data = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(this.config.dajPostavku("evidDatoteka"));
        fos.write(data);
        fos.close();
    }
}
