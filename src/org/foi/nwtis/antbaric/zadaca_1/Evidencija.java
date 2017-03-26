package org.foi.nwtis.antbaric.zadaca_1;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class Evidencija implements Serializable {
    
    public int requests = 0;
    public int successfulRequests = 0;
    public int terminatedRequests = 0;
    public long lastWorkingThreadId = 0;
    public int workingThreadsRunningTime = 0;
    private HashMap<String, Boolean> addresses = new HashMap<>();
    private HashMap<String, Integer> requestsLog = new HashMap<>();

    /**
     *
     * @return
     */
    public HashMap<String, Boolean> getAddresses() {

        return addresses;
    }

    /**
     *
     * @param address
     */
    public void setAddress(String address) {
        this.addresses.put(address, false);
    }

    public HashMap<String, Integer> getRequestLog() {

        return requestsLog;
    }

    /**
     *
     * @param address
     * @return
     */
    public boolean findAddress(String address) {
        for(Map.Entry<String, Boolean> item : this.addresses.entrySet()) {
            if(item.getKey().equals(address)) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean findRequest(String id) {
        for(Map.Entry<String, Integer> item : this.requestsLog.entrySet()) {
            if(item.getKey().equals(id)) {
                return true;
            }
        }

        return false;
    }

    public void badRequest() {
        this.requests++;
        this.terminatedRequests++;
    }

    public void okRequest() {
        this.requests++;
        this.successfulRequests++;
    }

    public void addRequest(String address) {
        this.requestsLog.putIfAbsent(address, 0);
        this.requestsLog.put(address, this.requestsLog.get(address)+1);
    }

    public void outputLog() {
        System.out.println("Uspješni zahtjevi: " + this.successfulRequests);
        System.out.println("Ne uspješni zahtjevi: " + this.terminatedRequests);
        System.out.println("Svi zahtjevi: " + this.requests);
        System.out.println("ID zadnje radne dretve: " + this.lastWorkingThreadId);
        System.out.println("Vrijeme rada radnih dretvi: " + this.workingThreadsRunningTime + " ms");
        System.out.println("Adrese");
        for(Map.Entry<String, Boolean> address : this.addresses.entrySet()) {
            System.out.println("    " + address.getKey() + ": " + (address.getValue() ? "YES" : "NO"));
        }
        System.out.println("Zahtjevi");
        for(Map.Entry<String, Integer> address : this.requestsLog.entrySet()) {
            System.out.println("    " + address.getKey() + ": " + address.getValue());
        }
    }
}
