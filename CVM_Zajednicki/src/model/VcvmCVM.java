/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.Objects;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Srecko
 */
public class VcvmCVM implements Apstraktni_Domenski_Objekat {

    private CVM cvm;
    private VrstaCVM vrsta;
    private Date licencaVaziOd;

    public VcvmCVM() {
    }

    public VcvmCVM(CVM cvm, VrstaCVM vrsta, Date licencaVaziOd) {
        this.cvm = cvm;
        this.vrsta = vrsta;
        this.licencaVaziOd = licencaVaziOd;
    }

    public CVM getCvm() {
        return cvm;
    }

    public void setCvm(CVM cvm) {
        this.cvm = cvm;
    }

    public VrstaCVM getVrsta() {
        return vrsta;
    }

    public void setVrsta(VrstaCVM vrsta) {
        this.vrsta = vrsta;
    }

    public Date getLicencaVaziOd() {
        return licencaVaziOd;
    }

    public void setLicencaVaziOd(Date licencaVaziOd) {
        this.licencaVaziOd = licencaVaziOd;
    }

    @Override
    public String toString() {
        return "VcvmCVM{" + "cvm=" + cvm
                + ", vrsta=" + vrsta
                + ", licencaVaziOd=" + licencaVaziOd + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(cvm, vrsta, licencaVaziOd);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final VcvmCVM other = (VcvmCVM) obj;
        return Objects.equals(this.cvm, other.cvm)
                && Objects.equals(this.vrsta, other.vrsta)
                && Objects.equals(this.licencaVaziOd, other.licencaVaziOd);
    }

    // ========================
    // Implementacija interfejsa
    // ========================
    @Override
    public String vratiNazivTabele() {
        // koristi se za INSERT/UPDATE/DELETE
        return "vcvm_cvm";
    }

// poseban metod SAMO za SELECT
    public String vratiNazivTabeleJoin() {
        return "(SELECT "
                + "C.CVMid, C.licenca, C.naziv AS nazivCVM, C.broj_telefona, "
                + "C.email, C.lozinka, C.grad, C.brojZaposlenih, C.datumOsnivanja, "
                + "V.idVrstaCVM, V.naziv AS nazivVrste, V.kratkiOpis, "
                + "VC.licencaVaziOd "
                + "FROM vcvm_cvm VC "
                + "JOIN cvm C ON VC.CVMid = C.CVMid "
                + "JOIN vrsta_cvm V ON VC.idVrstaCVM = V.idVrstaCVM) AS podaci";
    }

    @Override
    public String vratiKoloneZaInsert() {
        return "(CVMid, idVrstaCVM, licencaVaziOd)";
    }

    @Override
    public String vratiVrednostiZaInsert() {
        String datum = (licencaVaziOd == null) ? "NULL" : "'" + licencaVaziOd + "'";
        return cvm.getIdCVM() + ", "
                + vrsta.getIdVrstaCVM() + ", "
                + datum;
    }

    @Override
    public String vratiUslov() {
        return "CVMid = " + cvm.getIdCVM()
                + " AND idVrstaCVM = " + vrsta.getIdVrstaCVM();
    }

    @Override
    public List<Apstraktni_Domenski_Objekat> vratiListu(ResultSet rs) throws SQLException {
        List<Apstraktni_Domenski_Objekat> lista = new ArrayList<>();
        while (rs.next()) {
            CVM cvm = new CVM(
                    rs.getInt("CVMid"),
                    rs.getString("licenca"),
                    rs.getString("nazivCVM"), // alias iz JOIN-a
                    rs.getString("broj_telefona"),
                    rs.getString("email"),
                    rs.getString("lozinka"),
                    rs.getString("grad"),
                    rs.getInt("brojZaposlenih"),
                    rs.getDate("datumOsnivanja")
            );

            VrstaCVM vrsta = new VrstaCVM(
                    rs.getInt("idVrstaCVM"),
                    rs.getString("nazivVrste"), // alias iz JOIN-a
                    rs.getString("kratkiOpis")
            );

            VcvmCVM veza = new VcvmCVM(
                    cvm,
                    vrsta,
                    rs.getDate("licencaVaziOd")
            );
            lista.add(veza);
        }
        return lista;
    }

//    @Override
//    public String vratiVrednostiZaUpdate() {
//        String datum = (licencaVaziOd == null) ? "NULL" : "'" + licencaVaziOd + "'";
//        return "idVrstaCVM = " + vrsta.getIdVrstaCVM() + ", "
//                + "licencaVaziOd = " + datum;
//    }
    @Override
    public String vratiVrednostiZaUpdate() {
        String datum = (licencaVaziOd == null) ? "NULL" : "'" + new java.sql.Date(licencaVaziOd.getTime()) + "'";
        return "idVrstaCVM = " + vrsta.getIdVrstaCVM() + ", " +
                "licencaVaziOd = " + datum;
    }

    ///////////////////////////////////////
    ///////////////////////////////////////
    ///////////////////////////////////////
    ///////////////////////////////////////
    ///////////////////////////////////////
    ///////////////////////////////////////
    ///////////////////////////////////////
    ///////////////////////////////////////
//    @Override
//    public String vratiNazivTabele() {
//        return "(SELECT "
//                + "C.CVMid, C.licenca, C.naziv AS nazivCVM, C.broj_telefona, "
//                + "C.email, C.lozinka, C.grad, C.brojZaposlenih, C.datumOsnivanja, "
//                + "V.idVrstaCVM, V.naziv AS nazivVrste, V.kratkiOpis, "
//                + "VC.licencaVaziOd "
//                + "FROM vcvm_cvm VC "
//                + "JOIN cvm C ON VC.CVMid = C.CVMid "
//                + "JOIN vrsta_cvm V ON VC.idVrstaCVM = V.idVrstaCVM) AS podaci";
//    }
//
//    @Override
//    public String vratiKoloneZaInsert() {
//        return "(CVMid, idVrstaCVM, licencaVaziOd)";
//    }
//
//    @Override
//    public String vratiVrednostiZaInsert() {
//        return cvm.getIdCVM() + ", "
//                + vrsta.getIdVrstaCVM() + ", '"
//                + licencaVaziOd + "'";
//    }
//
//    @Override
//    public String vratiUslov() {
//        return "CVMid = " + cvm.getIdCVM()
//                + " AND idVrstaCVM = " + vrsta.getIdVrstaCVM();
//    }
//
//    @Override
//    public List<Apstraktni_Domenski_Objekat> vratiListu(ResultSet rs) throws SQLException {
//        List<Apstraktni_Domenski_Objekat> lista = new ArrayList<>();
//        while (rs.next()) {
//            CVM cvm = new CVM(
//                    rs.getInt("CVMid"),
//                    rs.getString("licenca"),
//                    rs.getString("nazivCVM"), // iz CVM
//                    rs.getString("broj_telefona"),
//                    rs.getString("email"),
//                    rs.getString("lozinka"),
//                    rs.getString("grad"),
//                    rs.getInt("brojZaposlenih"),
//                    rs.getDate("datumOsnivanja")
//            );
//
//            VrstaCVM vrsta = new VrstaCVM(
//                    rs.getInt("idVrstaCVM"),
//                    rs.getString("nazivVrste"), // iz VrstaCVM
//                    rs.getString("kratkiOpis")
//            );
//
//            VcvmCVM vcvm = new VcvmCVM(
//                    cvm,
//                    vrsta,
//                    rs.getDate("licencaVaziOd")
//            );
//
//            lista.add(vcvm);
//        }
//        return lista;
//    }
//
//    @Override
//    public String vratiVrednostiZaUpdate() {
//        String datum = (licencaVaziOd == null) ? "NULL" : "'" + licencaVaziOd + "'";
//        return "idVrstaCVM = " + vrsta.getIdVrstaCVM() + ", "
//                + "licencaVaziOd = " + datum;
//    }
/////////////////////////////////////////////////////////////////////////    
/////////////////////////////////////////////////////////////////////////    
/////////////////////////////////////////////////////////////////////////    
/////////////////////////////////////////////////////////////////////////    
/////////////////////////////////////////////////////////////////////////    
/////////////////////////////////////////////////////////////////////////    
/////////////////////////////////////////////////////////////////////////    
/////////////////////////////////////////////////////////////////////////    
//    @Override
//    public String vratiNazivTabele() {
//        return "vcvm_cvm VC JOIN cvm C ON VC.CVMid = C.CVMid "
//                + "JOIN vrsta_cvm V ON VC.idVrstaCVM = V.idVrstaCVM";
//    }
//
//    @Override
//    public String vratiKoloneZaInsert() {
//        return "(CVMid, idVrstaCVM, licencaVaziOd)";
//    }
//
//    @Override
//    public String vratiVrednostiZaInsert() {
//        return cvm.getIdCVM() + ", "
//                + vrsta.getIdVrstaCVM() + ", '"
//                + licencaVaziOd + "'";
//    }
//
//    @Override
//    public String vratiUslov() {
//        return "CVMid = " + cvm.getIdCVM()
//                + " AND idVrstaCVM = " + vrsta.getIdVrstaCVM();
//    }
//
//    @Override
//    public List<Apstraktni_Domenski_Objekat> vratiListu(ResultSet rs) throws SQLException {
//        List<Apstraktni_Domenski_Objekat> lista = new ArrayList<>();
//        while (rs.next()) {
//            // CVM deo
//            CVM cvm = new CVM(
//                    rs.getInt("CVMid"),
//                    rs.getString("licenca"),
//                    rs.getString("naziv"), // naziv iz CVM tabele
//                    rs.getString("broj_telefona"),
//                    rs.getString("email"),
//                    rs.getString("lozinka"),
//                    rs.getString("grad"),
//                    rs.getInt("brojZaposlenih"),
//                    rs.getDate("datumOsnivanja")
//            );
//
//            // VrstaCVM deo
//            VrstaCVM vrsta = new VrstaCVM(
//                    rs.getInt("idVrstaCVM"),
//                    rs.getString("naziv"), // naziv iz VrstaCVM tabele
//                    rs.getString("kratkiOpis")
//            );
//
//            // Veza VCVM
//            VcvmCVM vcvm = new VcvmCVM(
//                    cvm,
//                    vrsta,
//                    rs.getDate("licencaVaziOd")
//            );
//
//            lista.add(vcvm);
//        }
//        return lista;
//    }
//
//    @Override
//    public String vratiVrednostiZaUpdate() {
//        return "licencaVaziOd = '" + licencaVaziOd + "'";
//    }
}
