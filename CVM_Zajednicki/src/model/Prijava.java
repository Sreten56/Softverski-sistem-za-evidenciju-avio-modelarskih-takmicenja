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
public class Prijava implements Apstraktni_Domenski_Objekat {

    private int idPrijava;
    private Takmicenje takmicenje;
    private Takmicar takmicar;

    public Prijava() {
    }

    public Prijava(int idPrijava, Takmicenje takmicenje, Takmicar takmicar) {
        this.idPrijava = idPrijava;
        this.takmicenje = takmicenje;
        this.takmicar = takmicar;
    }

    public int getIdPrijava() {
        return idPrijava;
    }

    public void setIdPrijava(int idPrijava) {
        this.idPrijava = idPrijava;
    }

    public Takmicenje getTakmicenje() {
        return takmicenje;
    }

    public void setTakmicenje(Takmicenje takmicenje) {
        this.takmicenje = takmicenje;
    }

    public Takmicar getTakmicar() {
        return takmicar;
    }

    public void setTakmicar(Takmicar takmicar) {
        this.takmicar = takmicar;
    }

    // ==== Apstraktne metode ====
    @Override
    public String vratiNazivTabele() {
        return "prijava";
    }

    @Override
    public String vratiKoloneZaInsert() {
        return "(idTakmicenje, idTakmicar)";
    }

    @Override
    public String vratiVrednostiZaInsert() {
        return takmicenje.getIdTakmicenje() + ", " + takmicar.getIdTakmicar();
    }

//    @Override
//    public String vratiUslov() {
//        return "idPrijava = " + idPrijava;
//    }
    @Override
    public String vratiUslov() {
        if (takmicenje != null) {
            // Brišemo sve prijave za dato takmičenje
            return "idTakmicenje = " + takmicenje.getIdTakmicenje();
        }
        // Ako je setovan samo idPrijava, brišemo tu jednu prijavu
        return "idPrijava = " + idPrijava;
    }

    @Override
    public String vratiVrednostiZaUpdate() {
        return "idTakmicenje = " + takmicenje.getIdTakmicenje()
                + ", idTakmicar = " + takmicar.getIdTakmicar();
    }

//    @Override
//    public List<Apstraktni_Domenski_Objekat> vratiListu(ResultSet rs) throws SQLException {
//        List<Apstraktni_Domenski_Objekat> lista = new ArrayList<>();
//        while (rs.next()) {
//            Takmicenje t = new Takmicenje();
//            t.setIdTakmicenje(rs.getInt("idTakmicenje"));
//            t.setNazivTakmicenja(rs.getString("nazivTakmicenja"));
//
//            Kategorija kat = new Kategorija();
//            kat.setIdKategorija(rs.getInt("katID"));
//            kat.setNazivKategorija(rs.getString("nazivKategorija"));
//            t.setKat(kat);
//
//            CVM cvm = new CVM();
//            cvm.setIdCVM(rs.getInt("cvmID"));
//            cvm.setNazivCVM(rs.getString("nazivCVM"));
//            t.setCvm(cvm);
//
//            Takmicar tak = new Takmicar();
//            tak.setIdTakmicar(rs.getInt("idTakmicar"));
//            tak.setIme(rs.getString("ime"));
//            tak.setPrezime(rs.getString("prezime"));
//            tak.setObelezjeTakmicara(rs.getString("obelezjeTakmicara"));
//            tak.setDatumRodjenja(rs.getDate("datumRodjenja"));
//
//            AMKlub amk = new AMKlub();
//            amk.setIdAMK(rs.getInt("AMKid"));
//            amk.setNazivAMKluba(rs.getString("nazivAMK"));
//            // ako treba i tel/email
//
//            Prijava p = new Prijava(
//                    rs.getInt("idPrijava"),
//                    t,
//                    tak
//            );
//            lista.add(p);
//        }
//        return lista;
//
//    }
    @Override
    public List<Apstraktni_Domenski_Objekat> vratiListu(ResultSet rs) throws SQLException {
        List<Apstraktni_Domenski_Objekat> lista = new ArrayList<>();
        while (rs.next()) {
            Takmicenje t = new Takmicenje();
            t.setIdTakmicenje(rs.getInt("idTakmicenje")); // samo ID

            Takmicar tak = new Takmicar();
            tak.setIdTakmicar(rs.getInt("idTakmicar")); // samo ID

            Prijava p = new Prijava(
                    rs.getInt("idPrijava"),
                    t,
                    tak
            );
            lista.add(p);
        }
        return lista;
    }

}
