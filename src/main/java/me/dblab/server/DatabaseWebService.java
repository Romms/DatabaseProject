package me.dblab.server;

import me.dblab.databasecontroller.DatabaseControllerWeb;
import me.dblab.databasecontroller.DatabaseControllerWebAdapter;
import me.dblab.databasecontroller.DatabaseControllerDirect;

import javax.xml.ws.Endpoint;
import java.io.IOException;

public class DatabaseWebService {
    public static void main(String[] args) {
    	int port = 7777;
    	if (args.length > 0) {
    		port = Integer.parseInt(args[0]);
    	}

        DatabaseControllerWeb controller = new DatabaseControllerWebAdapter(new DatabaseControllerDirect());
        try {
            controller.loadDatabaseFromFile("sample.db");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = "http://0.0.0.0:" + port + "/ws_db";
        Endpoint.publish(address, controller);
        System.out.println("Web server running at " + address);
    }
}
