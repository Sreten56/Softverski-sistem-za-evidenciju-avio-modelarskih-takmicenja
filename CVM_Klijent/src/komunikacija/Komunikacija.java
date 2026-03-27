/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package komunikacija;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import transfer.Klijentski_Zahtev;
import transfer.Serverski_Odgovor;

/**
 *
 * @author Srecko
 */
public class Komunikacija {

    private Socket soket;
    private static Komunikacija instance;

    private Komunikacija() {

        try {
            soket = new Socket("localhost", 9000);

        } catch (IOException ex) {
            Logger.getLogger(Komunikacija.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Komunikacija getInstance() {
        if (instance == null) {
            instance = new Komunikacija();
        }
        return instance;
    }

//    public Serverski_Odgovor primi_odgovor() {
//
//        try {
//            ObjectInputStream ois = new ObjectInputStream(soket.getInputStream());
//            return (Serverski_Odgovor) ois.readObject();
//
//        } catch (IOException ex) {
//            Logger.getLogger(Komunikacija.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(Komunikacija.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return null;
//    }
    public Serverski_Odgovor primi_odgovor() {
        try {
            ObjectInputStream ois = new ObjectInputStream(soket.getInputStream());
            Serverski_Odgovor so = (Serverski_Odgovor) ois.readObject();

            if (so != null && "GRESKA_BAZA".equals(so.getOdgovor())) {
                javax.swing.JOptionPane.showMessageDialog(null,
                        " Veza sa bazom podataka je prekinuta!\n"
                        + "Aplikacija će se zatvoriti.",
                        "Greška u bazi", javax.swing.JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

            return so;

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Komunikacija.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public void posalji_zahtev(Klijentski_Zahtev kz) {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(soket.getOutputStream());
            oos.writeObject(kz);
            oos.flush();

        } catch (IOException ex) {
            Logger.getLogger(Komunikacija.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean isConnected() {
        try {
            return soket != null && soket.isConnected() && !soket.isClosed();
        } catch (Exception e) {
            return false;
        }
    }

    public Socket getSoket() {
        return soket;
    }

    public void setSoket(Socket soket) {
        this.soket = soket;
    }

}
