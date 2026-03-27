/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.sql.*;

/**
 *
 * @author Srecko
 */
public class AMKlub implements Apstraktni_Domenski_Objekat {

    private int idAMK;
    private String nazivAMKluba;
    private String brojTelefona;
    private String emailAMK;
    private int mestoID;

    public AMKlub() {
    }

    public AMKlub(int idAMK, String nazivAMKluba, String brojTelefona, String emailAMK, int mestoID) {
        this.idAMK = idAMK;
        this.nazivAMKluba = nazivAMKluba;
        this.brojTelefona = brojTelefona;
        this.emailAMK = emailAMK;
        this.mestoID = mestoID;
    }

    public int getIdAMK() {
        return idAMK;
    }

    public void setIdAMK(int idAMK) {
        this.idAMK = idAMK;
    }

    public String getNazivAMKluba() {
        return nazivAMKluba;
    }

    public void setNazivAMKluba(String nazivAMKluba) {
        this.nazivAMKluba = nazivAMKluba;
    }

    public String getBrojTelefona() {
        return brojTelefona;
    }

    public void setBrojTelefona(String brojTelefona) {
        this.brojTelefona = brojTelefona;
    }

    public String getEmailAMK() {
        return emailAMK;
    }

    public void setEmailAMK(String emailAMK) {
        this.emailAMK = emailAMK;
    }

    public int getMestoID() {
        return mestoID;
    }

    public void setMestoID(int mestoID) {
        this.mestoID = mestoID;
    }

    @Override
    public String toString() {
        return "AMKlub{" + "idAMK=" + idAMK + ", nazivAMKluba=" + nazivAMKluba + ", brojTelefona=" + brojTelefona + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAMK, nazivAMKluba, brojTelefona);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AMKlub other = (AMKlub) obj;
        return idAMK == other.idAMK
                && Objects.equals(nazivAMKluba, other.nazivAMKluba)
                && Objects.equals(brojTelefona, other.brojTelefona);
    }

    /* ===== IMPLEMENTACIJA ADO METODA ===== */
    @Override
    public String vratiNazivTabele() {
        return "amklub";
    }

    @Override
    public String vratiKoloneZaInsert() {
        return "(nazivAMK, brojTelefona, email, mestoID)";
    }

    @Override
    public String vratiVrednostiZaInsert() {
        return "'" + nazivAMKluba + "', '" + brojTelefona + "', '" + emailAMK + "', " + mestoID;
    }

    @Override
    public String vratiUslov() {
        return "idAMK = " + idAMK;
    }

    @Override
    public List<Apstraktni_Domenski_Objekat> vratiListu(ResultSet rs) throws SQLException {
        List<Apstraktni_Domenski_Objekat> lista = new ArrayList<>();
        while (rs.next()) {
            AMKlub amk = new AMKlub(
                    rs.getInt("idAMK"),
                    rs.getString("nazivAMK"),
                    rs.getString("brojTelefona"),
                    rs.getString("email"),
                    rs.getInt("mestoID")
            );
            lista.add(amk);
        }
        return lista;
    }

    @Override
    public String vratiVrednostiZaUpdate() {
        return "nazivAMK = '" + nazivAMKluba + "', "
                + "brojTelefona = '" + brojTelefona + "', "
                + "email = '" + emailAMK + "', "
                + "mestoID = " + mestoID;
    }

}
