package org.foi.nwtis.antbaric.zadaca_1.components;

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
                "^(-admin)? ?-server (?:(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6})|(localhost)) -port ([8|9][0-9]{3}) -u ([a-zA-Z0-9-_]+) -p ([a-zA-Z0-9-_!#]+) -(pause|stop|start|stat)$",
                "^(-korisnik)? ?-s (?:(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6})|(localhost)) -port ([8|9][0-9]{3}) -u ([a-zA-Z0-9-_]+) ?(-(a|t) (?:(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(((http:\\/\\/)|(https:\\/\\/))([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6})|(localhost)))? ?(-(w) (([1-9]{1})|([1-9]{1}[0-9]{1})|([1-5]{1}[0-9]{0,2})|600))?$",
                "^(-prikaz)? ?-s ((:?([a-zA-Z]:|(http:(?:\\\\|\\/))))?(?:(?:\\\\|\\/)?(\\w[\\w ]*.*))+\\.(?:(?i)bin(?-i)))$"
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
