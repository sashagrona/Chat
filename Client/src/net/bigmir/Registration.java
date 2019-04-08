package net.bigmir;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Registration {
    private String login;
    private String password;

    public void signIn(User user){
        Scanner sc = new Scanner(System.in);
        System.out.println("Input you login");
        login = sc.nextLine();
        System.out.println("Input your password");
        password = sc.nextLine();
        user.setLogin(login);
        user.setPassword(password);

    }
    public int registrate(String url){
        try {
            URL u = new URL(url);
            HttpURLConnection hc = (HttpURLConnection) u.openConnection();
            hc.setRequestMethod("GET");
            hc.setDoOutput(true);
            return hc.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
