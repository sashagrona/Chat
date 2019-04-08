package net.bigmir;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Registration rg = new Registration();
        boolean status = true;

        try {
            System.out.println("Enter your login: ");
            String login = scanner.nextLine();
            System.out.println("Enter your password");
            String password = scanner.nextLine();
            User user = new User(login, password, status);
            if (!enterChat(user, scanner, login, password, rg)) {
                System.exit(0);
            }
                user.setUserInPrivate(true);
                Thread th = new Thread(new GetThread(user));
                th.setDaemon(true);
                th.start();
                System.out.println("For private chat put chatTo_userlogin");
                System.out.println("Enter your message: ");

                while (true) {
                    String regex = "chatTo_[A-Za-z0-9]+";
                    Pattern pat = Pattern.compile(regex);
                    String text = scanner.nextLine();
                    if ("exit".equals(text)) {
                        exit(user);
                        break;
                    }
                    Matcher mat = pat.matcher(text);
                    if (text.equals("getUsers")) {
                        getUsers();
                    }
                    if (text.isEmpty()) break;
                    Room room = new Room();
                    if (text.equals("/createRoom")) {
                        createRoom(room);
                    }
                    if (mat.find()) {
                        user.setUserInPrivate(false);
                        isFind(login, text);
                        user.setUserInPrivate(true);
                        text = "";
                    }
                    if(text.equals("/joinRoom")){
                        user.setUserInPrivate(false);
                        enterToRoom(user,room,login);
                        user.setUserInPrivate(true);
                    }
                    Message m = new Message(login, text, "all");
                    int res = m.send(Utils.getURL() + "/add");

                    if (res != 200) { // 200 OK
                        System.out.println("HTTP error occured: " + res);
                        return;
                    }

                }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    public static void getUsers() throws IOException {
        URL url = new URL(Utils.getURL() + "/getAll");
        HttpURLConnection hc = (HttpURLConnection) url.openConnection();
        hc.setRequestMethod("GET");
        hc.setDoOutput(true);
        InputStream is = hc.getInputStream();

        byte[] buf = requestBodyToArray(is);
        String strBuf = new String(buf, StandardCharsets.UTF_8);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        User[] users = gson.fromJson(strBuf, User[].class);
        for (User u : users) {
            System.out.println(u);
        }
    }


    private static byte[] requestBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }

    public static void exit(User user) throws IOException {
        user.setStatus(false);
        System.out.println(user);
        URL url = new URL(Utils.getURL() + "/logout?login=" + user.getLogin());
        HttpURLConnection hc = (HttpURLConnection) url.openConnection();
        hc.setRequestMethod("GET");
        hc.setDoOutput(true);
        int resp = hc.getResponseCode();
        if (resp != 200) {
            System.out.println("You logged out from chat");
        }
    }

    public static void isFind(String login, String text) throws IOException {
        System.out.println("You are in a private chat.To exit input '/mainChat'");
        Scanner sc = new Scanner(System.in);

        String[] arr = text.split("_");
        String log = arr[1];
        Thread t = new Thread(new PrivateMessageThread(log, login));
        t.setDaemon(true);
        t.start();
        while (true) {
            String msg = sc.nextLine();
            if ("/mainChat".equals(msg)) {
                try {
                    t.interrupt();
                } catch (Exception e) {
                   e.getMessage();
                }

                break;
            }
            Message m = new Message(login, msg, log);
            int res = m.send(Utils.getURL() + "/add");

            if (res != 200) { // 200 OK
                System.out.println("HTTP error occured: " + res);
                return;
            }
        }
    }

    public static boolean enterChat(User user, Scanner scanner, String login, String password, Registration rg) {
        String answer = "";
        int code = 0;
        int r = user.send(Utils.getURL() + "/login?login=" + login + "&password=" + password);
        if (r != 200) { // 200 OK
            System.out.println("HTTP error occured: " + r);
            System.out.println("Do you want to register?(Yes/No)");
            answer = scanner.nextLine();
            if (answer.equals("yes")) {
                boolean f = true;
                while (f) {
                    rg.signIn(user);
                    login = rg.getLogin();
                    password = rg.getPassword();
                    code = rg.registrate(Utils.getURL() + "/signin?login=" + login + "&password=" + password);

                    if (code != 200) {
                        System.out.println("Please try another nickname " + code);
                    } else {
                        f = false;
                    }
                }

            } else {
                System.out.println("Goodbye!)");
                return false;
            }
        }
        return true;
    }

    public static void createRoom(Room room) {

        Scanner sc = new Scanner(System.in);
        System.out.println("What's the name of your room");
        String roomName = sc.nextLine();
        room.setName(roomName);
        System.out.println("You have created a room: " + roomName);
        int res = new Room(roomName).send(Utils.getURL()+"/room?name="+roomName);
        if (res != 200) {
            System.out.println("Http error" + res);
        }
    }
    public static void addUserToRoom(User user,Room room) {
        Scanner sc = new Scanner(System.in);
        System.out.println("To which room you want to join?");
        room.setName(sc.nextLine());
        System.out.println(user.getLogin() + " has been added to the Room: " + room.getName());
        int res = new Room(room.getName()).send(Utils.getURL() + "/room?name=" + room.getName() + "&user=" + user.getLogin());
        if (res != 200) {
            System.out.println("Http error" + res);
        }
    }
    public static void exitFromTheRoom(User user,String roomName){
        int res = new Room().send(Utils.getURL() + "/room?name=" + roomName + "&user=" + user.getLogin() + "&exit=exit");
        if (res != 200) {
            System.out.println("Http error" + res);
        }
    }
    public static void enterToRoom(User user,Room room,String login) throws IOException{
        addUserToRoom(user,room);
        Scanner sc = new Scanner(System.in);
        System.out.println("You are in a room " + room.getName() + ".To exit input '/mainChat'");
        Thread t = new Thread(new PrivateMessageThread(room.getName(), login));
        t.setDaemon(true);
        t.start();
        while (true) {
            String msg = sc.nextLine();
            if ("/mainChat".equals(msg)) {
                exitFromTheRoom(user,room.getName());
                try {
                    t.interrupt();
                } catch (Exception e) {
                    e.getMessage();
                }

                break;
            }
            Message m = new Message(login, msg, room.getName());
            int res = m.send(Utils.getURL() + "/add");

            if (res != 200) { // 200 OK
                System.out.println("HTTP error occured: " + res);
                return;
            }
        }

    }
}
