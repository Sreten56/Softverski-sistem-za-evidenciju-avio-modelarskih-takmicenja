/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author sreck
 */
public class Takmicenje implements Apstraktni_Domenski_Objekat {

    private int idTakmicenje;
    private String nazivTakmicenja;
    private Date datum;
    private CVM cvm;
    private Kategorija kat;
    private String lokacija;
    private List<Takmicar> prijavljeniTakmicari;

    public Takmicenje() {
    }

    public Takmicenje(int idTakmicenje, String nazivTakmicenja, Date datum, CVM cvm, Kategorija kat, String lokacija,
            List<Takmicar> prijavljeniTakmicari) {
        this.idTakmicenje = idTakmicenje;
        this.nazivTakmicenja = nazivTakmicenja;
        this.datum = datum;
        this.cvm = cvm;
        this.kat = kat;
        this.lokacija = lokacija;
        this.prijavljeniTakmicari = prijavljeniTakmicari;
    }

    public Takmicenje(int idTakmicenje, String nazivTakmicenja, Date datum, CVM cvm, Kategorija kat, String lokacija) {
        this.idTakmicenje = idTakmicenje;
        this.nazivTakmicenja = nazivTakmicenja;
        this.datum = datum;
        this.cvm = cvm;
        this.kat = kat;
        this.lokacija = lokacija;
        this.prijavljeniTakmicari = new ArrayList<>();
    }

    public int getIdTakmicenje() {
        return idTakmicenje;
    }

    public void setIdTakmicenje(int idTakmicenje) {
        this.idTakmicenje = idTakmicenje;
    }

    public String getNazivTakmicenja() {
        return nazivTakmicenja;
    }

    public void setNazivTakmicenja(String nazivTakmicenja) {
        this.nazivTakmicenja = nazivTakmicenja;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public CVM getCvm() {
        return cvm;
    }

    public void setCvm(CVM cvm) {
        this.cvm = cvm;
    }

    public Kategorija getKat() {
        return kat;
    }

    public void setKat(Kategorija kat) {
        this.kat = kat;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public List<Takmicar> getPrijavljeniTakmicari() {
        return prijavljeniTakmicari;
    }

    public void setPrijavljeniTakmicari(List<Takmicar> prijavljeniTakmicari) {
        this.prijavljeniTakmicari = prijavljeniTakmicari;
    }

    ///////////////////////////
    @Override
    public String vratiNazivTabele() {
        return "takmicenje";
    }

    @Override
    public String vratiKoloneZaInsert() {
        return "(nazivTakmicenja, datum, cvmID, katID, lokacija)";
    }

    @Override
    public String vratiVrednostiZaInsert() {
        return "'" + nazivTakmicenja + "', '"
                + new java.sql.Date(datum.getTime()) + "', "
                + cvm.getIdCVM() + ", "
                + kat.getIdKategorija() + ", '"
                + lokacija + "'";
    }

    @Override
    public String vratiUslov() {
        return "idTakmicenje = " + idTakmicenje;
    }

    @Override
    public List<Apstraktni_Domenski_Objekat> vratiListu(ResultSet rs) throws SQLException {
        List<Apstraktni_Domenski_Objekat> lista = new ArrayList<>();
        while (rs.next()) {
            // samo ID-ovi, naziv i datum iz tabele takmicenje
            CVM cvm = new CVM();
            cvm.setIdCVM(rs.getInt("cvmID"));   // kolona u tabeli je cvmID

            Kategorija kat = new Kategorija();
            kat.setIdKategorija(rs.getInt("katID")); // kolona u tabeli je katID

            Takmicenje t = new Takmicenje(
                    rs.getInt("idTakmicenje"), // idTakmicenje
                    rs.getString("nazivTakmicenja"), // nazivTakmicenja
                    rs.getDate("datum"), // datum
                    cvm, // samo ID, naziv će Controller kasnije dopuniti
                    kat, // isto
                    rs.getString("lokacija") // lokacija
            );
            lista.add(t);
        }
        return lista;
    }

    @Override
    public String vratiVrednostiZaUpdate() {
        return "nazivTakmicenja = '" + nazivTakmicenja + "', "
                + "datum = '" + new java.sql.Date(datum.getTime()) + "', "
                + "cvmID = " + cvm.getIdCVM() + ", "
                + "katID = " + kat.getIdKategorija() + ", "
                + "lokacija = '" + lokacija + "'";
    }

}
