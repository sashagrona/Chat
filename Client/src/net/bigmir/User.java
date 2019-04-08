package net.bigmir;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class User {
    private String login;
    private transient String password;
    private boolean status;
    private boolean userInPrivate;

    public User(String login, String password,boolean status) {
        this.login = login;
        this.password = password;
        this.status=status;
    }

    public User() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String toJSON() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public static User fromJSON(String s) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(s, User.class);
    }

    public int send(String url) {
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

    @Override
    public String toString() {
        String line="";
        if(status==true){
            line="online";
        }else{
            line="offline";
        }
        return "User[" +
                "Login:'" + login + '\'' +
                "; status: " + line +
                ']';
    }

    public boolean isUserInPrivate() {
        return userInPrivate;
    }

    public void setUserInPrivate(boolean userInPrivate) {
        this.userInPrivate = userInPrivate;
    }
}
