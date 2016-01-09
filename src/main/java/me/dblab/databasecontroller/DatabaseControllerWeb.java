package me.dblab.databasecontroller;

import me.dblab.exceptions.TableNotExistsException;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.rmi.RemoteException;

@WebService // Endpoint Interface
@SOAPBinding(style = SOAPBinding.Style.RPC) // Needed for the WSDL
public interface DatabaseControllerWeb extends DatabaseController {
    String getDatabase() throws RemoteException, TableNotExistsException;
}
