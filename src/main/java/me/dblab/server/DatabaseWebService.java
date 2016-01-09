package me.dblab.server;

import me.dblab.databasecontroller.DatabaseControllerWeb;
import me.dblab.databasecontroller.DatabaseControllerWebAdapter;
import me.dblab.databasecontroller.DatabaseControllerDirect;
import org.apache.commons.cli.*;

import javax.xml.ws.Endpoint;
import java.io.IOException;

public class DatabaseWebService {
    public static void main(String[] args) {
        int port = 7777;
        String address = "0.0.0.0";
        String database_path = "sample.db";

        Option option_port = new Option("p", "port", true, "Port");
        Option option_address = new Option("a", "address", true, "IP or domain address");
        Option option_database = new Option("d", "database", true, "Path to the database file");

        Options posixOptions = new Options();
        posixOptions.addOption(option_port);
        posixOptions.addOption(option_address);
        posixOptions.addOption(option_database);

        try {
            CommandLineParser cmdLinePosixParser = new PosixParser();
            CommandLine commandLine = cmdLinePosixParser.parse(posixOptions, args);

            if(commandLine.hasOption(option_port.getOpt())){
                port = Integer.parseInt(commandLine.getOptionValue(option_port.getOpt()));
            }

            if(commandLine.hasOption(option_address.getOpt())){
                address = commandLine.getOptionValue(option_address.getOpt());
            }

            if(commandLine.hasOption(option_database.getOpt())){
                database_path = commandLine.getOptionValue(option_database.getOpt());
            }
        }
        catch (ParseException parseException)
        {
            System.err.println(
                    "Encountered exception while parsing args:\n"
                            + parseException.getMessage() );
        }

        DatabaseControllerWeb controller = new DatabaseControllerWebAdapter(new DatabaseControllerDirect());
        try {
            controller.loadDatabaseFromFile(database_path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String httpAddress = "http://" + address + ":" + port + "/ws_db";
        Endpoint.publish(httpAddress, controller);
        System.out.println("Web server running at " + httpAddress);
    }
}
