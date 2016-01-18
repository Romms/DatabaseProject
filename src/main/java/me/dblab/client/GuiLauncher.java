package me.dblab.client;

import me.dblab.client.clientcontroller.ClientController;
import me.dblab.client.layout.MainPanel;
import me.dblab.databasecontroller.DatabaseController;
import me.dblab.databasecontroller.DatabaseControllerNullUnescapeAdapter;
import me.dblab.databasecontroller.DatabaseControllerWeb;
import me.dblab.databasecontroller.DatabaseControllerDirect;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.swing.*;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;

public class GuiLauncher {
    protected static DatabaseController GetLocalController() {
        return new DatabaseControllerDirect();
    }

    protected static DatabaseController GetRmiJrmpController() {
        try {
            return (DatabaseController) Naming.lookup("//localhost/RmiJrmpServer");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected static DatabaseController GetRmiIiopController() {
        Properties p = System.getProperties();
        p.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
        p.put("java.naming.provider.url", "iiop://localhost:1050");

        try {
            Context context = new InitialContext();
            Object reference = context.lookup("RmiIiopServer");
            return (DatabaseController) PortableRemoteObject.narrow(reference, DatabaseController.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

/*    protected static DatabaseController GetRmiIiopToCorbaController() {
        Properties p = System.getProperties();
        p.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
        p.put("java.naming.provider.url", "iiop://localhost:1050");

        try {
            Context context = new InitialContext();
            Object reference = context.lookup("CorbaServer");
            return new DatabaseControllerCorbaAdapter((DatabaseControllerCorba) PortableRemoteObject.narrow(reference, DatabaseControllerCorba.class));
        } catch (NamingException e) {
            System.out.println(e);
            e.printStackTrace();
        }

        return null;
    }

    protected static DatabaseController GetCorbaController(String[] args) {
        try {
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            DatabaseControllerCorba corbaController = DatabaseControllerCorbaHelper.narrow(ncRef.resolve_str("CorbaServer"));
            return new DatabaseControllerCorbaAdapter(corbaController);
        } catch(Exception e) {
            System.out.println("CORBA error: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }*/

    protected static DatabaseController GetWebServiceController() {
        try {
            URL url;
//          url = new URL("http://localhost:7000/ws_db");
            url = new URL("https://enigmatic-garden-2745.herokuapp.com/ws_db");
            QName qname = new QName("http://databasecontroller.dblab.me/", "DatabaseControllerService");
            Service service = Service.create(url, qname);
            return new DatabaseControllerNullUnescapeAdapter(service.getPort(DatabaseControllerWeb.class));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.println("Starting gui");
        DatabaseController controller = null;
        for (String arg : args) {
            switch (arg) {
                case "--use=LocalController":
                    System.out.println("Local controller");
                    controller = GetLocalController();
                    break;
                case "--use=JrmpController":
                    System.out.println("JRMP controller"); // Java Remote Method Protocol

                    controller = GetRmiJrmpController();
                    break;
                case "--use=IiopController":
                    System.out.println("IIOP controller");
                    controller = GetRmiIiopController();
                    break;
                case "--use=IiopToCorbaController":
                    System.out.println("Iiop To Corba controller");
                    //controller = GetRmiIiopToCorbaController();
                    break;
                case "--use=CorbaController":
                    System.out.println("Corba controller");
                    //controller = GetCorbaController(args);
                    break;
                case "--use=WebServiceController":
                    System.out.println("Web service controller");
                    controller = GetWebServiceController();
                    break;
                default:
                    System.out.println("Invalid options.");
                    break;

            }
        }

        if (controller == null) {
            System.out.println("Controller not found.");
            return;
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final DatabaseController finalController = controller;
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Database Management System");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.add(new MainPanel(frame, new ClientController(finalController)), BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        });
    }
}

