package me.dblab.server;

import me.dblab.databasecontroller.DatabaseController;
import me.dblab.databasecontroller.DatabaseControllerDirect;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;

public class RmiIiopServer {
    public static void main(String[] args) {
        System.out.println("RMI IIOP server starting");

        Properties p = System.getProperties();
        p.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
        p.put("java.naming.provider.url", "iiop://localhost:1050");

        try {
            LocateRegistry.createRegistry(1099);
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            System.out.println("java RMI registry already exists.");
        }

        try {
            DatabaseController controller = new DatabaseControllerDirect();
            PortableRemoteObject.exportObject(controller);
            Context context = new InitialContext();
            context.rebind("RmiIiopServer", controller);
        } catch (RemoteException | NamingException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("Started");
    }
}
