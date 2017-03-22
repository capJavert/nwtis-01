package org.foi.nwtis.antbaric.zadaca_1.components;

public class ErrorNwtis {

    public static String getMessage(String code) {
        switch (code) {
            case "90":
                return "ERROR" + code + "; Sintaksa nije ispravna ili komanda nije dozvoljena.";
            case "00":
                return "ERROR" + code + "; Korisnik nije valjani administrator sustava.";
            case "01":
                return "ERROR" + code + "; Dretva je vec u stanju PAUSE";
            case "02":
                return "ERROR" + code + "; Dretva nije u stanju PAUSE";
            case "03":
                return "ERROR" + code + "; Dretva ne moze biti prekinuta ili postoji problem sa serijalizacijom";
            case "04":
                return "ERROR" + code + "; Evidencija ne moze biti prikazana";
            case "10":
                return "ERROR" + code + "; Adresa ne postoji ili nema slobodnog mjesta za inicijalizaciju";
            case "11":
                return "ERROR" + code + "; Adresa vec postoji";
            case "12":
                return "ERROR" + code + "; Adresa ne postoji";
            case "13":
                return "ERROR" + code + "; Dretva nije uspjela odraditi cekanje";
            default:
                return "ERROR; Doslo je do nepoznate pogreske u sustavu";
        }
    }
}
