package com.example.brawlheroes;

import java.io.*;

public class Settings implements Serializable {
    private String ip;
    private String port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
    public void loadDefaultSettings() {
        ip = "localhost";
        port = "1234";
        File file = new File("settings.txt");
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeObject(this);
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadSettings() {
        File file = new File("settings.txt");
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
            Settings settings = (Settings) stream.readObject();
            this.setIp(settings.ip);
            this.setPort(settings.port);
        } catch (IOException e) {
            loadDefaultSettings();
        } catch (ClassNotFoundException e) {
            loadDefaultSettings();
        }
    }
    public void save() {
        File file = new File("settings.txt");
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeObject(this);
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
