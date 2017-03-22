package org.foi.nwtis.antbaric.zadaca_1.components;

import org.foi.nwtis.antbaric.zadaca_1.models.User;

import java.io.*;
import java.util.ArrayList;

public class UserManager {
    private ArrayList<User> users;

    public UserManager(final String fileName) throws IOException {
        users = new ArrayList<>();

        File file = new File(fileName);
        if(file.exists() && file.isFile()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    line = line.replace("USER ", "");
                    line = line.replace("PASSWD ", "");
                    String[] rawUser = line.split(";");
                    users.add(new User(rawUser[0], rawUser[1]));
                }
            } catch (IOException exception) {
                throw new IOException("Greška u sadržaju administratorske datoteke");
            }

        } else {
            throw new FileNotFoundException("Ne postoji administratorska datoteka " + fileName);
        }
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public User findOne(String username, String password) {
        for(User user : users) {
            if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }
}
