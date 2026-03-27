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
public class CVM implements Apstraktni_Domenski_Objekat {

    private int idCVM;
    private String licenca;
    private String nazivCVM;
    private String br_telefona;
    private String email;
    private String lozinka;
    private String grad;
    private int brojZaposlenih;
    private Date datumOsnivanja;

    public CVM() {
    }

    public CVM(String email, String lozinka) {
        this.email = email;
        this.lozinka = lozinka;
    }

    public CVM(int idCVM, String licenca, String nazivCVM, String br_telefona, String email, String lozinka, String grad,
            int brojZaposlenih, Date datumOsnivanja) {
        this.idCVM = idCVM;
        this.licenca = licenca;
        this.nazivCVM = nazivCVM;
        this.br_telefona = br_telefona;
        this.email = email;
        this.lozinka = lozinka;
        this.grad = grad;
        this.brojZaposlenih = brojZaposlenih;
        this.datumOsnivanja = datumOsnivanja;
    }

    public int getIdCVM() {
        return idCVM;
    }

    public void setIdCVM(int idCVM) {
        this.idCVM = idCVM;
    }

    public String getLicenca() {
        return licenca;
    }

    public void setLicenca(String licenca) {
        this.licenca = licenca;
    }

    public String getNazivCVM() {
        return nazivCVM;
    }

    public void setNazivCVM(String nazivCVM) {
        this.nazivCVM = nazivCVM;
    }

    public String getBr_telefona() {
        return br_telefona;
    }

    public void setBr_telefona(String br_telefona) {
        this.br_telefona = br_telefona;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public int getBrojZaposlenih() {
        return brojZaposlenih;
    }

    public void setBrojZaposlenih(int brojZaposlenih) {
        this.brojZaposlenih = brojZaposlenih;
    }

    public Date getDatumOsnivanja() {
        return datumOsnivanja;
    }

    public void setDatumOsnivanja(Date datumOsnivanja) {
        this.datumOsnivanja = datumOsnivanja;
    }

    @Override
    public String toString() {
        return "CVM{" + "nazivCVM=" + nazivCVM + ", br_telefona=" + br_telefona + '}';
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
        final CVM other = (CVM) obj;
        if (this.idCVM != other.idCVM) {
            return false;
        }
        if (!Objects.equals(this.licenca, other.licenca)) {
            return false;
        }
        return Objects.equals(this.nazivCVM, other.nazivCVM);
    }

    //////////////////////////////////////////////
    @Override
    public String vratiNazivTabele() {
        return "CVM";
    }

    @Override
    public String vratiKoloneZaInsert() {
        return "(licenca, naziv, broj_telefona, email, lozinka, grad, brojZaposlenih, datumOsnivanja)";
    }

//    @Override
//    public String vratiVrednostiZaInsert() {
//        String datum = (datumOsnivanja == null) ? "NULL" : "'" + datumOsnivanja + "'";
//        return "'" + licenca + "', '" + nazivCVM + "', '" + br_telefona + "', '"
//                + email + "', '" + lozinka + "', '" + grad + "', "
//                + brojZaposlenih + ", " + datum;
//    }
    @Override
    public String vratiVrednostiZaInsert() {
        String datum = (datumOsnivanja == null) ? "NULL" : "'" + datumOsnivanja + "'";
        String lic = (licenca == null || licenca.trim().isEmpty()) ? "NULL" : "'" + licenca + "'";
        String naziv = (nazivCVM == null || nazivCVM.trim().isEmpty()) ? "NULL" : "'" + nazivCVM + "'";
        String tel = (br_telefona == null || br_telefona.trim().isEmpty()) ? "NULL" : "'" + br_telefona + "'";
        String mail = (email == null || email.trim().isEmpty()) ? "NULL" : "'" + email + "'";
        String pass = (lozinka == null || lozinka.trim().isEmpty()) ? "NULL" : "'" + lozinka + "'";
        String gradStr = (grad == null || grad.trim().isEmpty()) ? "NULL" : "'" + grad + "'";

        return lic + ", " + naziv + ", " + tel + ", " + mail + ", " + pass + ", " + gradStr + ", " + brojZaposlenih + ", " + datum;
    }

    @Override
    public String vratiUslov() {
        return "CVMid = " + idCVM;
    }

    @Override
    public List<Apstraktni_Domenski_Objekat> vratiListu(ResultSet rs) throws SQLException {
        List<Apstraktni_Domenski_Objekat> lista = new ArrayList<>();
        while (rs.next()) {
            System.out.println(">> CVM id=" + rs.getInt("CVMid")
                    + ", naziv=" + rs.getString("naziv")
                    + ", email=" + rs.getString("email")
                    + ", grad=" + rs.getString("grad"));

            CVM cvm = new CVM(
                    rs.getInt("CVMid"),
                    rs.getString("licenca"),
                    rs.getString("naziv"),
                    rs.getString("broj_telefona"),
                    rs.getString("email"),
                    rs.getString("lozinka"),
                    rs.getString("grad"),
                    rs.getInt("brojZaposlenih"),
                    rs.getDate("datumOsnivanja")
            );

            lista.add(cvm);
        }
        return lista;
    }

    @Override
    public String vratiVrednostiZaUpdate() {
        String datum = (datumOsnivanja == null) ? "NULL" : "'" + datumOsnivanja + "'";
        return "licenca = " + (licenca == null || licenca.trim().isEmpty() ? "NULL" : "'" + licenca + "'") + ", "
                + "naziv = " + (nazivCVM == null || nazivCVM.trim().isEmpty() ? "NULL" : "'" + nazivCVM + "'") + ", "
                + "broj_telefona = " + (br_telefona == null || br_telefona.trim().isEmpty() ? "NULL" : "'" + br_telefona + "'") + ", "
                + "email = " + (email == null || email.trim().isEmpty() ? "NULL" : "'" + email + "'") + ", "
                + "lozinka = " + (lozinka == null || lozinka.trim().isEmpty() ? "NULL" : "'" + lozinka + "'") + ", "
                + "grad = " + (grad == null || grad.trim().isEmpty() ? "NULL" : "'" + grad + "'") + ", "
                + "brojZaposlenih = " + brojZaposlenih + ", "
                + "datumOsnivanja = " + datum;
    }

}
