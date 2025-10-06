package at.technikum;

import at.technikum.application.mrp.MrpApplication;
import at.technikum.server.Server;

public class Main {
    public static void main(String[] args) {
        new Server(8080, new MrpApplication()).start();
        System.out.println("MRP server running at http://localhost:8080");
    }
}
