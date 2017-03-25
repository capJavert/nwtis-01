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

    public HashMap<String, Boolean> getAddresses() {

        return addresses;
    }

    public void setAddress(String address) {
        this.addresses.put(address, false);
    }

    public HashMap<String, Integer> getRequestLog() {

        return requestsLog;
    }

    public boolean findAddress(String address) {
        for(Map.Entry<String, Boolean> item : this.addresses.entrySet()) {
            if(item.getKey().equals(address)) {
                return true;
            }
        }

        return false;
    }

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
}
