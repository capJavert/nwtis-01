package org.foi.nwtis.antbaric.zadaca_1.components;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SyntaxValidator {

    /**
     * Return rules
     *
     * @return Returns list of rules
     */
    private static List<String> rules() {
        return Stream.of(
                "^-konf ([^\\s]+\\.)(txt|xml|bin)( +-load)?$",
                //"(^-server.+)|(^-admin.+)|(^-user.+)|(^-show.+)",
                "^-admin -server ([^\\s]+) -port ([0-9]{4})",
                "^USER ([^\\s]+); PASSWD ([^\\s]+); ((PAUSE)|(STOP)|(START)|(STAT));$",
                "^USER ([^\\s]+); ADD ([^\\s]+);$",
                "^USER ([^\\s]+); TEST ([^\\s]+);$",
                "^USER ([^\\s]+); WAIT ([^\\d]+);$"
                //"^-user -s (([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]) -port ([0-9]{4}) -u korisnik [[-a | -t] URL] | [-w nnn]?$",
                //"serialization", "^-prikaz -s datoteka?$"
        ).collect(Collectors.toList());
    }

    /**
     *
     * @param match String to match
     * @return Matcher
     */
    public static Matcher validate(Object match) {
        for(String rule : rules()) {
            Pattern pattern = Pattern.compile(rule);
            Matcher m = pattern.matcher(match.toString());

            if(m.matches()) {
                return m;
            }
        }

        return null;
    }

    /**
     * Check if rule is valid
     *
     * @param args Command
     * @return Returns true if valid
     */
    public static Matcher validateArguments(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }
        String p = sb.toString().trim();

        return validate(p);
    }
}
