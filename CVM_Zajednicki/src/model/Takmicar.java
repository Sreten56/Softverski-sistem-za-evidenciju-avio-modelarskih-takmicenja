/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Srecko
 */
public class Takmicar implements Apstraktni_Domenski_Objekat{
    
    private int idTakmicar;
    private String Ime;
    private String Prezime;
    private Date datumRodjenja;
    private String brojTelefona_takmicar;
    private String obelezjeTakmicara;
    private int KatID;
    private int AMKid;

    public Takmicar() {
    }

    public Takmicar(int idTakmicar, String Ime, String Prezime, Date datumRodjenja, String brojTelefona_takmicar,
            String obelezjeTakmicara, int KatID, int AMKid) {
        this.idTakmicar = idTakmicar;
        this.Ime = Ime;
        this.Prezime = Prezime;
        this.datumRodjenja = datumRodjenja;
        this.brojTelefona_takmicar = brojTelefona_takmicar;
        this.obelezjeTakmicara = obelezjeTakmicara;
        this.KatID = KatID;
        this.AMKid = AMKid;
    }

    
    

    public int getIdTakmicar() {
        return idTakmicar;
    }

    public void setIdTakmicar(int idTakmicar) {
        this.idTakmicar = idTakmicar;
    }

    public String getIme() {
        return Ime;
    }

    public void setIme(String Ime) {
        this.Ime = Ime;
    }

    public String getPrezime() {
        return Prezime;
    }

    public void setPrezime(String Prezime) {
        this.Prezime = Prezime;
    }

    public Date getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(Date datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public String getBrojTelefona_takmicar() {
        return brojTelefona_takmicar;
    }

    public void setBrojTelefona_takmicar(String brojTelefona_takmicar) {
        this.brojTelefona_takmicar = brojTelefona_takmicar;
    }

    public String getObelezjeTakmicara() {
        return obelezjeTakmicara;
    }

    public void setObelezjeTakmicara(String obelezjeTakmicara) {
        this.obelezjeTakmicara = obelezjeTakmicara;
    }

    public int getKatID() {
        return KatID;
    }

    public void setKatID(int KatID) {
        this.KatID = KatID;
    }

    public int getAMKid() {
        return AMKid;
    }

    public void setAMKid(int AMKid) {
        this.AMKid = AMKid;
    }
    
    

    @Override
    public String toString() {
        return "Takmicar{" + "idTakmicar=" + idTakmicar + ", Ime=" + Ime + ", Prezime=" + Prezime + ", datumRodjenja=" + datumRodjenja + ", brojTelefona_takmicar=" + brojTelefona_takmicar + ", obelezjeTakmicara=" + obelezjeTakmicara + ", KatID=" + KatID + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Takmicar other = (Takmicar) obj;
        if (this.idTakmicar != other.idTakmicar) {
            return false;
        }
        if (!Objects.equals(this.Ime, other.Ime)) {
            return false;
        }
        if (!Objects.equals(this.Prezime, other.Prezime)) {
            return false;
        }
        if (!Objects.equals(this.obelezjeTakmicara, other.obelezjeTakmicara)) {
            return false;
        }
        return Objects.equals(this.datumRodjenja, other.datumRodjenja);
    }
    
    

    
    
    
    
        @Override
    public String vratiNazivTabele() {
        return "takmicar";
    }

    @Override
    public String vratiKoloneZaInsert() {
        return "(Ime, Prezime, datumRodjenja, brojTelefona, obelezjeTakmicara, katID, AMKid)";
    }

    @Override
    public String vratiVrednostiZaInsert() {
        // datumRodjenja je java.util.Date, pa ga treba konvertovati u java.sql.Date
        return "'" + Ime + "', '" + Prezime + "', '" + new java.sql.Date(datumRodjenja.getTime()) + "', '" 
                + brojTelefona_takmicar + "', '" + obelezjeTakmicara + "', " + KatID + ", " + AMKid;
    }

    @Override
    public String vratiUslov() {
        return "idTakmicar = " + idTakmicar;
    }

    @Override
    public List<Apstraktni_Domenski_Objekat> vratiListu(ResultSet rs) throws SQLException {
        List<Apstraktni_Domenski_Objekat> lista = new ArrayList<>();
        while (rs.next()) {
            Takmicar takmicar = new Takmicar(
                rs.getInt("idTakmicar"),
                rs.getString("Ime"),
                rs.getString("Prezime"),
                rs.getDate("datumRodjenja"),          // ovde SQL date ide direktno u konstruktor
                rs.getString("brojTelefona"),
                rs.getString("obelezjeTakmicara"),
                rs.getInt("katID"),
                rs.getInt("AMKid")
            );
            lista.add(takmicar);
        }
        return lista;
    }

    
    @Override
    public String vratiVrednostiZaUpdate() {
        return "Ime = '" + Ime + "', "
                + "Prezime = '" + Prezime + "', "
                + "datumRodjenja = '" + new java.sql.Date(datumRodjenja.getTime()) + "', "
                + "brojTelefona = '" + brojTelefona_takmicar + "', "
                + "obelezjeTakmicara = '" + obelezjeTakmicara + "', "
                + "katID = " + KatID + ", "
                + "AMKid = " + AMKid;
    }

    
    
}
