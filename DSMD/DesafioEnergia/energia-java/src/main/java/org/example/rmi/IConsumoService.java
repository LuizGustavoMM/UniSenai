package org.example.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IConsumoService extends Remote {

    double consultarConsumoTotal() throws RemoteException;
    double consultarConsumoCasa(String idCasa) throws RemoteException;

}
