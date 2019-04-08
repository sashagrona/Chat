package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedList;
import java.util.List;

public class User {
    private String login;
    private transient String password;
    private boolean status;
    private final List<Message> list = new LinkedList<>();
    private Gson gson;

    public User(String login, String password,boolean status) {
        this.login = login;
        this.password = password;
        this.status=status;
    }
    public User(String login){
        this.login=login;
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

    public List<Message> getList() {
        return list;
    }
    public synchronized String toJSON(int n) {
        if (n >= list.size()) return null;
        return gson.toJson(new JsonMessages(list, n));
    }

    public void addMessage(Message m){
        list.add(m);
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
