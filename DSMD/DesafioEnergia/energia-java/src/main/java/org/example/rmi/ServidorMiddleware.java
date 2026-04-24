package org.example.rmi;

import org.example.baseDados.BaseDados;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServidorMiddleware extends UnicastRemoteObject
    implements IConsumoService {

    public ServidorMiddleware() throws RemoteException {
    }

    @Override
    public double consultarConsumoTotal() throws RemoteException {
        System.out.println("Realizando consulta do consumo total");
        double consumoTotal = 0;
        for (String key : BaseDados.baseDados.keySet()) {
            double consumo = BaseDados.baseDados.get(key);
            System.out.println("Consumo [" + key + "] - " +
                    "" + consumo + "kwh" );
            consumoTotal += consumo;
        }
        return consumoTotal;
    }

    @Override
    public double consultarConsumoCasa(String idCasa) throws RemoteException {
        System.out.println("Obtendo consumo da casa " + idCasa);
        return BaseDados.baseDados.getOrDefault(idCasa, 0.0);
    }
}
