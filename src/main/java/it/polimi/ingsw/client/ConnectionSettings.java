package it.polimi.ingsw.client;

public class ConnectionSettings {
    private static int port;
    private static String address;

    public static int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public static String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}