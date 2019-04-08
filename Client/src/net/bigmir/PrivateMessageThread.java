package net.bigmir;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PrivateMessageThread implements Runnable {
    private final Gson gson;
    private String to;
    private String ufrom;
    private int n;

    public PrivateMessageThread(String to, String ufrom) {
        this.to = to;
        this.ufrom=ufrom;
        gson = new GsonBuilder().create();
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                URL url = new URL(Utils.getURL() + "/get?from=" + n + "&to=" + to + "&ufrom=" + ufrom);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                InputStream is = http.getInputStream();
                if (http.getResponseCode() == 400) {
                    System.out.println("None");
                } else {
                    try {
                        byte[] buf = requestBodyToArray(is);
                        String strBuf = new String(buf, StandardCharsets.UTF_8);
                        JsonMessages list = gson.fromJson(strBuf, JsonMessages.class);
                        if (list!=null) {
                            for(Message message : list.getList()){
                                if(!message.getTo().equals("all")) {

                                    System.out.println(message);
                                    n++;
                                }
                            }
                        }

                    } finally {
                        is.close();
                    }
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("Exit from the private chat");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private byte[] requestBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }
}
