/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import baza.DB_Broker;
import baza.Konekcija;
import java.sql.*;
import model.Apstraktni_Domenski_Objekat;

/**
 *
 * @author sreck
 */
public abstract class AbstractSystemOperation<T extends Apstraktni_Domenski_Objekat> {

    protected DB_Broker broker;

    public AbstractSystemOperation() {
        try {
            broker = new DB_Broker();
            System.out.println("Povezan DB_Broker.");
        } catch (Exception e) {
            System.out.println("Greška prilikom kreiranja DB_Broker-a: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public final Object izvrsi(T object) throws Exception {
        try {
            System.out.println("Pokreće se operacija: " + this.getClass().getSimpleName());
            validacija(object);
            System.out.println("Validacija uspešno završena.");
            
            Object rezultat = izvrsiOperaciju(object);
            Konekcija.getInstance().getConnection().commit();
            System.out.println("Commit izvršen.");
            return rezultat;
        } catch (SQLException ex) {
            Konekcija.getInstance().getConnection().rollback();
            System.out.println("SQL greška — rollback izvršen!");
            throw ex;
        } catch (Exception ex) {
            Konekcija.getInstance().getConnection().rollback();
            System.out.println("Greška u sistemskoj operaciji - rollback izvršen!");
            throw ex;
        }
    }

    protected abstract void validacija(T object) throws Exception;

    protected abstract Object izvrsiOperaciju(T object) throws Exception;
}

//public abstract class AbstractSystemOperation<T extends Apstraktni_Domenski_Objekat> {
//
//    protected DB_Broker broker;
//
//    public AbstractSystemOperation() {
//        try {
//            broker = new DB_Broker();
//            System.out.println("Povezan DB_Broker.");
//        } catch (Exception e) {
//            System.out.println("Greška prilikom kreiranja DB_Broker-a: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    public final Object izvrsi(T object) throws Exception {
//        try {
//            System.out.println("Pokreće se operacija: " + this.getClass().getSimpleName());
//
//            // ✅ 1. Provera da li je konekcija aktivna
//            if (broker == null || broker.getConnection() == null || broker.getConnection().isClosed()) {
//                throw new java.sql.SQLNonTransientConnectionException("Konekcija sa bazom nije aktivna!");
//            }
//
//            if (!broker.isConnectionAlive()) {
//                throw new java.sql.SQLNonTransientConnectionException("Veza sa bazom je izgubljena!");
//            }
//
//            // ✅ 2. Validacija i izvršavanje operacije
//            validacija(object);
//            System.out.println("Validacija uspešno završena.");
//            Object rezultat = izvrsiOperaciju(object);
//
//            // ✅ 3. Commit ako je sve prošlo
//            broker.getConnection().commit();
//            System.out.println("Commit izvršen.");
//            return rezultat;
//
//        } catch (java.sql.SQLNonTransientConnectionException ex) {
//            // ✅ Specijalni slučaj – veza sa bazom pukla
//            System.out.println("⚠️ Veza sa bazom izgubljena tokom izvršenja operacije!");
//            throw ex; // šalje se dalje Obrada_Klijentskih_Zahteva da klijentu javi "GRESKA_BAZA"
//
//        } catch (SQLException ex) {
//            // ✅ Rollback u slučaju SQL greške
//            try {
//                if (broker != null && broker.getConnection() != null && !broker.getConnection().isClosed()) {
//                    broker.getConnection().rollback();
//                    System.out.println("SQL greška — rollback izvršen!");
//                } else {
//                    System.out.println("⚠️ Rollback nije moguć jer je konekcija već zatvorena.");
//                }
//            } catch (Exception e) {
//                System.out.println("⚠️ Greška prilikom rollback-a: " + e.getMessage());
//            }
//            throw ex;
//
//        } catch (Exception ex) {
//            // ✅ Rollback za sve ostale greške
//            try {
//                if (broker != null && broker.getConnection() != null && !broker.getConnection().isClosed()) {
//                    broker.getConnection().rollback();
//                    System.out.println("Greška u sistemskoj operaciji — rollback izvršen!");
//                } else {
//                    System.out.println("⚠️ Rollback nije moguć jer je konekcija već zatvorena.");
//                }
//            } catch (Exception e) {
//                System.out.println("⚠️ Greška prilikom rollback-a: " + e.getMessage());
//            }
//            throw ex;
//        }
//    }
//
//    protected abstract void validacija(T object) throws Exception;
//
//    protected abstract Object izvrsiOperaciju(T object) throws Exception;
//}

