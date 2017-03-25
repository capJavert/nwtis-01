package org.foi.nwtis.antbaric.zadaca_1;

import org.foi.nwtis.antbaric.zadaca_1.models.Request;

import java.io.Serializable;
import java.util.ArrayList;

public final class Evidencija implements Serializable {
    
    public int requests = 0;
    public int successfulRequests = 0;
    public int terminatedRequests = 0;
    public int addressRequests = 0;
    public int addressCount = 0;
    private ArrayList<String> addresses = new ArrayList<>();
    private ArrayList<Request> requestLog = new ArrayList<>();

    public ArrayList<String> getAddresses() {
        return addresses;
    }

    public void setAddress(String address) {
        this.addressCount++;
        this.addresses.add(address);
    }

    public ArrayList<Request> getRequestLog() {
        return requestLog;
    }

    public void setRequestLog(ArrayList<Request> requestLog) {
        this.requestLog = requestLog;
    }

    public boolean findAddress(String address) {
        for(String item : this.addresses) {
            if(item.equals(address)) {
                return true;
            }
        }

        return false;
    }

    public boolean findRequest(int id) {
        for(Request request : this.requestLog) {
            if(request.getId() == id) {
                return true;
            }
        }

        return false;
    }
}
