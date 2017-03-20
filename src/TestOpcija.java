
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestOpcija {

    public static void main(String[] args) {

        // -server -konf datoteka(.txt | .xml | .bin) [-load]
        String sintaksa1 = "^-server -konf ([^\\s]+\\.(?i)txt|xml)( +-load)?$";
        String sintaksa = "(^-server.+)|(^-admin.+)|(^-user.+)|(^-show.+)";
        final String sintaksa2 = "^-konf ([^\\s]+\\.)(txt|xml|bin)( +-load)?$";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String p = sb.toString().trim();
        Pattern pattern = Pattern.compile(sintaksa2);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if (status) {
            int poc = 0;
            int kraj = m.groupCount();
            for (int i = poc; i <= kraj; i++) {
                System.out.println(i + ". " + m.group(i));
            }
        } else {
            System.out.println("Ne odgovara!");
        }
    }
}
