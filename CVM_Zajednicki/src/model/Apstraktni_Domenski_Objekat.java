/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.List;
import java.sql.*;

/**
 *
 * @author sreck
 */
public interface Apstraktni_Domenski_Objekat extends Serializable {
    
    // Vrati ime tabele u bazi
    public abstract String vratiNazivTabele();

    // Vrati nazive kolona za INSERT
    public abstract String vratiKoloneZaInsert();

    // Vrati vrednosti za INSERT
    public abstract String vratiVrednostiZaInsert();

    // Vrati primarni ključ (uslov WHERE)
    public abstract String vratiUslov();

    // Napravi listu objekata iz ResultSet-a
    public abstract List<Apstraktni_Domenski_Objekat> vratiListu(ResultSet rs) throws SQLException;
    
    public abstract String vratiVrednostiZaUpdate();
    
}
