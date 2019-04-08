package net.bigmir;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class Room {
    private String name;
    private List<Message> list;

    public Room(String name) {
        this.name = name;
        this.list = new LinkedList<>();
    }

    public Room() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void add(Message message){
        list.add(message);
    }

    public List<Message> getList() {
        return list;
    }

    public void setList(List<Message> list) {
        this.list = list;
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
}
