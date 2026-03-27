/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baza;

import controller.Controller;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CVM;
import model.VcvmCVM;
import model.VrstaCVM;
import pomocne.Pomocne;
import transfer.Klijentski_Zahtev;
import java.util.*;
import model.AMKlub;
import model.Kategorija;
import model.Mesto;
import model.Takmicar;

/**
 *
 * @author Srecko
 */
public class DB_Broke_Stari {

    public boolean login(String email, String lozinka) {

        try {
            String upit = "SELECT *\n"
                    + "FROM CVM\n"
                    + "WHERE EMAIL = ? AND LOZINKA = ?";

            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);
            ps.setString(1, email);
            ps.setString(2, lozinka);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

//    public boolean dodaj_CVM(String nazivCVM, String licenca, String brTel, String email, String privremena_sifra) {
//
//        try {
//            String upit = "INSERT INTO CVM (LICENCA,NAZIV,BROJ_TELEFONA,EMAIL,LOZINKA)\n"
//                    + "VALUES (?,?,?,?,?)";
//
//            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);
//
//            ps.setString(1, licenca);
//            ps.setString(2, nazivCVM);
//            ps.setString(3, brTel);
//            ps.setString(4, email);
//            ps.setString(5, privremena_sifra);
//
//            int rows_affacted = ps.executeUpdate();
//
//            if (rows_affacted == 0) {
//                Konekcija.getInstance().getConnection().rollback();
//                throw new SQLException("GRESKA PRI DODAVANJU!");
//            } else {
//                Konekcija.getInstance().getConnection().commit();
//                return true;
//            }
//
//        } catch (SQLException ex) {
//            Logger.getLogger(DB_Broker.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
//    }
    public boolean dodaj_CVM(CVM cvm) {
        try {
            String upit = "INSERT INTO CVM (LICENCA, NAZIV, BROJ_TELEFONA, EMAIL, LOZINKA)"
                    + " VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);

            ps.setString(1, cvm.getLicenca());
            ps.setString(2, cvm.getNazivCVM());
            ps.setString(3, cvm.getBr_telefona());
            ps.setString(4, cvm.getEmail());
            ps.setString(5, cvm.getLozinka());

            int rows_affected = ps.executeUpdate();

            if (rows_affected == 0) {
                Konekcija.getInstance().getConnection().rollback();
                throw new SQLException("GRESKA PRI DODAVANJU!");
            } else {
                Konekcija.getInstance().getConnection().commit();
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public String vrati_naziv_CVM(String email, String lozinka) {

        String nazivCVM = null;

        try {
            String upit = "SELECT *\n"
                    + "FROM CVM\n"
                    + "WHERE EMAIL = ? AND LOZINKA = ?";

            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);
            ps.setString(1, email);
            ps.setString(2, lozinka);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nazivCVM = rs.getString("naziv");
            } else {
                // Ako nema rezultata, bacite izuzetak ili vratite null
                throw new SQLException("Ne postoji CVM sa datim email-om i lozinkom.");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nazivCVM;
    }

    public List<CVM> vrati_podatke(String email, String lozinka) {

        List<CVM> lista = new ArrayList<>();

        try {
            String upit = "SELECT *\n"
                    + "FROM CVM\n"
                    + "WHERE EMAIL = ? AND LOZINKA = ?";

            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);
            ps.setString(1, email);
            ps.setString(2, lozinka);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) { // Proveri da li postoji rezultat
                String naziv = rs.getString("naziv");
                String licenca = rs.getString("licenca");
                String br_tel = rs.getString("broj_telefona");

                CVM podaci = new CVM(0, licenca, naziv, br_tel, email, lozinka, upit, 0, null);
                lista.add(podaci);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;

    }

    public List<CVM> vrati_sve_CVM() {

        List<CVM> lista = new ArrayList<>();

        try {
            String upit = "select *\n"
                    + "from cvm";

            Statement st = Konekcija.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(upit);

            while (rs.next()) {

                int idCVM = rs.getInt("CVMid");
                String licenca = rs.getString("licenca");
                String naziv = rs.getString("naziv");
                String brTel = rs.getString("broj_telefona");
                String email = rs.getString("email");
                String lozinka = rs.getString("lozinka");
                String grad = rs.getString("grad");
                int idDrz = rs.getInt("idDrz");

                CVM cvm = new CVM(idCVM, licenca, naziv, brTel, email, lozinka, grad, idDrz, null);
                lista.add(cvm);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public List<Object[]> vrati_SVE_podatke(String email, String lozinka) {
        List<Object[]> lista = new ArrayList<>();

        try {
            String upit = "SELECT C.CVMid, C.licenca, C.naziv AS nazivCVM, C.broj_telefona, C.grad, C.idDrz, D.idDrzava, D.nazivDrzava "
                    + "FROM cvm C JOIN drzava D ON C.idDrz = D.idDrzava "
                    + "WHERE C.email = ? AND C.lozinka = ?";
            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);
            ps.setString(1, email);
            ps.setString(2, lozinka);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int CVMid = rs.getInt("CVMid");
                String licenca = rs.getString("licenca");
                String nazivCVM = rs.getString("nazivCVM");
                String brojTelefona = rs.getString("broj_telefona");
                String grad = rs.getString("grad");
                int idDrz = rs.getInt("idDrz");
                int idDrzava = rs.getInt("idDrzava");
                String nazivDrzava = rs.getString("nazivDrzava");

//                CVM cvm = new CVM(CVMid, licenca, nazivCVM, brojTelefona, email, lozinka, grad, idDrz);
//                VrstaCVM drzava = new VrstaCVM(idDrzava, nazivDrzava);
//                lista.add(new Object[]{cvm, drzava});
            }

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    // OPCIJA SA DVMDrzava KLASOM AKO ZATREBA
//    public List<CVMDrzava> vrati_SVE_podatke(String email, String lozinka) {
//
//        List<CVMDrzava> lista = new ArrayList<>();
//        
//        try {
//            
//            String upit = "select *\n"
//                    + "from cvm C join drzava D on (C.idDrz = D.idDrzava)\n"
//                    + "where email = ? and lozinka = ?";
//            
//            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);
//            ps.setString(1, email);
//            ps.setString(2, lozinka);
//            
//            ResultSet rs = ps.executeQuery();
//            
//            if (rs.next()) {
//                
//                int CVMid = rs.getInt("CVMid");
//                String licenca = rs.getString("licenca");
//                String naziv = rs.getString("naziv");
//                String brTel = rs.getString("broj_telefona");
//                String grad = rs.getString("grad");
//                int idDrz = rs.getInt("idDrz");
//                int idDrzava = rs.getInt("idDrzava");
//                String nazivDrzava = rs.getString("nazivDrzava");
//                
//
//                CVM cvm = new CVM(CVMid, licenca, naziv, brTel, email, lozinka, grad, idDrz);
//                Drzava drzava = new Drzava(idDrzava, nazivDrzava);
//                CVMDrzava cvmDrzava = new CVMDrzava(cvm, drzava);
//                lista.add(cvmDrzava);
//                
//            }
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(DB_Broker.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return lista;
//    }
    public List<VrstaCVM> getAllDrzave() {

        List<VrstaCVM> listaDrzava = new ArrayList<>();

        try {
            String upit = "SELECT * "
                    + "FROM Drzava";

            Statement st = Konekcija.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(upit);

            while (rs.next()) {

                int idDrz = rs.getInt("idDrzava");
                String nazivDrz = rs.getString("nazivDrzava");

//                VrstaCVM drz = new VrstaCVM(idDrz, nazivDrz);
//                listaDrzava.add(drz);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaDrzava;
    }

    public List<Object[]> vrati_SVE_potrebne_info_tak_kat_amk() {
        List<Object[]> lista = new ArrayList<>();
        try {
            String upit = "select * \n"
                    + "from takmicar t join kategorija k on (t.katID = k.idKategorija)\n"
                    + "join amklub a on (t.AMKid = a.idAMK)";

            Statement st = Konekcija.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(upit);

            while (rs.next()) {

                int idTakmicar = rs.getInt("t.idTakmicar");
                String ime = rs.getString("t.Ime");
                String prezime = rs.getString("t.Prezime");

                java.sql.Date datumSQL = rs.getDate("t.datumRodjenja");
                java.util.Date datumRodjenja = new java.util.Date(datumSQL.getTime());

                String brojTelefona = rs.getString("t.brojTelefona");
                String obelezjeTakmicara = rs.getString("t.obelezjeTakmicara");
                int katID = rs.getInt("t.katID");
                int AMKid = rs.getInt("t.AMKid");

                int idKategorija = rs.getInt("k.idKategorija");
                String nazivKategorija = rs.getString("k.nazivKategorija");
                String opisKategorija = rs.getString("k.opisKategorija");

                int idAKM = rs.getInt("a.idAMK");
                String nazivAMK = rs.getString("a.nazivAMK");
                String AMK_brojTelefona = rs.getString("a.brojTelefona");
                String email = rs.getString("a.email");
                int mestoID = rs.getInt("a.mestoID");

                Takmicar takmicar = new Takmicar(idTakmicar, ime, prezime, datumRodjenja, brojTelefona, obelezjeTakmicara, katID, AMKid);
                Kategorija kat = new Kategorija(idKategorija, nazivKategorija, opisKategorija);
                AMKlub amk = new AMKlub(idAKM, nazivAMK, AMK_brojTelefona, email, mestoID);

                lista.add(new Object[]{takmicar, kat, amk});

                // Debugging output
//            System.out.println("Takmicar: " + takmicar);
//            System.out.println("Kategorija: " + kat);
//            System.out.println("AMKlub: " + amk);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

//    public List<Object[]> vrati_SVE_potrebne_info_tak_kat_amk() {
//
//        List<Object[]> lista = new ArrayList<>();
//
//        try {
//
//            String upit = "select * \n"
//                    + "from takmicar t join kategorija k on (t.katID = k.idKategorija)\n"
//                    + "join amklub a on (t.AMKid = a.idAMK)";
//
//            Statement st = Konekcija.getInstance().getConnection().createStatement();
//            ResultSet rs = st.executeQuery(upit);
//
//            while (rs.next()) {
//
//                int idTakmicar = rs.getInt("t.idTakmicar");
//                String ime = rs.getString("t.ime");
//                String prezime = rs.getString("t.prezime");
//                
//                java.sql.Date datumSQL = rs.getDate("t.datumRodjenja");
//                java.util.Date datumRodjenja = new java.util.Date(datumSQL.getTime());
//               
//                String brojTelefona = rs.getString("t.brojTelefona");
//                String obelezjeTakmicara = rs.getString("t.obelezjeTakmicara");
//                int katID = rs.getInt("t.katID");
//                int AMKid = rs.getInt("t.AMKid");
//                
//               
//                
//                int idKategorija = rs.getInt("k.idKategorija");
//                String nazivKategorija = rs.getString("t.nazivKategorija");
//                String opisKategorija = rs.getString("t.opisKategorija");
//                
//                
//                
//                int idAKM = rs.getInt("a.idAKM");
//                String nazivAMK = rs.getString("a.nazivAMK");
//                String AMK_brojTelefona = rs.getString("a.brojTelefona");
//                String email = rs.getString("a.email");
//                int mestoID = rs.getInt("a.mestoID");
//                
//                
//                
//                Takmicar takmicar = new Takmicar(idTakmicar, ime, prezime, datumRodjenja, brojTelefona, obelezjeTakmicara, katID, AMKid);
//                Kategorija kat = new Kategorija(idKategorija, nazivKategorija, opisKategorija);
//                AMKlub amk = new AMKlub(idAKM, nazivAMK, AMK_brojTelefona, email, mestoID);
//                
//                lista.add(new Object[] {takmicar,kat,amk});
//                
//                System.out.println(takmicar);
//                System.out.println(kat);
//                System.out.println(amk);
//                
//            }
//
//        } catch (SQLException ex) {
//            Logger.getLogger(DB_Broker.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return lista;
//    }
    public List<Kategorija> vrati_SVE_kategorije() {

        List<Kategorija> lista = new ArrayList<>();

        try {

            String upit = "select * \n"
                    + "from kategorija";

            Statement st = Konekcija.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(upit);

            while (rs.next()) {
                int id = rs.getInt("idKategorija");
                String naziv = rs.getString("nazivKategorija");
                String opis = rs.getString("opisKategorija");

                Kategorija kat = new Kategorija(id, naziv, opis);
                lista.add(kat);

            }

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lista;
    }

    public List<AMKlub> vrati_sve_AMK() {

        List<AMKlub> lista = new ArrayList<>();

        try {

            String upit = "SELECT * \n"
                    + "FROM amklub";

            Statement st = Konekcija.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(upit);

            while (rs.next()) {

                int idAMK = rs.getInt("idAMK");
                String nazivAMK = rs.getString("nazivAMK");
                String brojTelefona = rs.getString("brojTelefona");
                String email = rs.getString("email");
                int mestoID = rs.getInt("mestoID");

                AMKlub amk = new AMKlub(idAMK, nazivAMK, brojTelefona, email, mestoID);
                lista.add(amk);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public boolean azuriraj_takmicare(Takmicar takmicar) {

        try {
            String upit = "UPDATE takmicar\n"
                    + "SET \n"
                    + "    Ime = ?,\n"
                    + "    Prezime = ?,\n"
                    + "    datumRodjenja = ?,\n"
                    + "    brojTelefona = ?,\n"
                    + "    obelezjeTakmicara = ?,\n"
                    + "    katID = ?,\n"
                    + "    AMKid = ?\n"
                    + "WHERE idTakmicar = ?;";

            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareCall(upit);
            ps.setString(1, takmicar.getIme());
            ps.setString(2, takmicar.getPrezime());
            ps.setDate(3, new java.sql.Date(takmicar.getDatumRodjenja().getTime()));
            ps.setString(4, takmicar.getBrojTelefona_takmicar());
            ps.setString(5, takmicar.getObelezjeTakmicara());
            ps.setInt(6, takmicar.getKatID());
            ps.setInt(7, takmicar.getAMKid());
            ps.setInt(8, takmicar.getIdTakmicar());

            int rows_affected = ps.executeUpdate();

            if (rows_affected == 0) {
                Konekcija.getInstance().getConnection().rollback();
                throw new SQLException("GRESKA PRI AZURIRANJU!");
            } else {
                Konekcija.getInstance().getConnection().commit();
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean dodaj_takmicara(Takmicar takmicar) {

        try {
            String upit = "INSERT INTO takmicar (Ime, Prezime, datumRodjenja, brojTelefona, obelezjeTakmicara, katID, AMKid) \n"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);

            ps.setString(1, takmicar.getIme());
            ps.setString(2, takmicar.getPrezime());
            ps.setDate(3, new java.sql.Date(takmicar.getDatumRodjenja().getTime()));
            ps.setString(4, takmicar.getBrojTelefona_takmicar());
            ps.setString(5, takmicar.getObelezjeTakmicara());
            ps.setInt(6, takmicar.getKatID());
            ps.setInt(7, takmicar.getAMKid());

            int rows_affected = ps.executeUpdate();

            if (rows_affected == 0) {
                Konekcija.getInstance().getConnection().rollback();
                throw new SQLException("GRESKA PRI AZURIRANJU!");
            } else {
                Konekcija.getInstance().getConnection().commit();
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public int vratiNajveciIdTakmicara() {
        int najveciId = 0;
        try {
            String upit = "SELECT MAX(idTakmicar) as najveci FROM takmicar";
            Statement st = Konekcija.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(upit);

            if (rs.next()) {
                najveciId = rs.getInt("najveci");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return najveciId;
    }

    public int vratiNajveciIdAMK() {

        int najveciId = 0;
        try {
            String upit = "SELECT MAX(idAMK) as najveci FROM amklub";
            Statement st = Konekcija.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(upit);

            if (rs.next()) {
                najveciId = rs.getInt("najveci");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return najveciId;
    }

    public boolean obrisi_takmicara(int idTakmicar) {

        try {
            String upit = "DELETE FROM takmicar \n"
                    + "WHERE idTakmicar = " + idTakmicar;

            Statement st = Konekcija.getInstance().getConnection().createStatement();
            int RowsAffected = st.executeUpdate(upit);

            if (RowsAffected > 0) {
                Konekcija.getInstance().getConnection().commit();
                return true;
            } else {
                Konekcija.getInstance().getConnection().rollback();
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Object[]> vrati_SVE_potrebne_info_amk_mesto() {

        List<Object[]> lista = new ArrayList<>();

        try {

            String upit = "select *\n"
                    + "from amklub as A join mesto as M on (A.mestoID = M.idMesto)";

            Statement st = Konekcija.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(upit);

            while (rs.next()) {
                int idAMK = rs.getInt("idAMK");
                String nazivAMK = rs.getString("nazivAMK");
                String brojTelefona = rs.getString("brojTelefona");
                String email = rs.getString("email");
                int mestoID = rs.getInt("mestoID");

                int idMesto = rs.getInt("idMesto");
                String nazivMesto = rs.getString("nazivMesto");
                String opstina = rs.getString("opstina");

                AMKlub amk = new AMKlub(idAMK, nazivAMK, brojTelefona, email, mestoID);
                Mesto mesto = new Mesto(idMesto, nazivMesto, opstina);

                lista.add(new Object[]{amk, mesto});

            }

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

        return lista;
    }

    public List<Mesto> vrati_mesta() {

        List<Mesto> lista = new ArrayList<>();

        try {

            String upit = "select * \n"
                    + "from mesto";

            Statement st = Konekcija.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(upit);

            while (rs.next()) {

                int id = rs.getInt("idMesto");
                String naziv = rs.getString("nazivMesto");
                String opstina = rs.getString("opstina");

                Mesto mes = new Mesto(id, naziv, opstina);
                lista.add(mes);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public boolean dodaj_amk(AMKlub amk) {

        try {
            String upit = "insert into amklub (nazivAMK, brojTelefona, email, mestoID)\n"
                    + "values (?, ?, ?, ?)";

            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);

            ps.setString(1, amk.getNazivAMKluba());
            ps.setString(2, amk.getBrojTelefona());
            ps.setString(3, amk.getEmailAMK());
            ps.setInt(4, amk.getMestoID());

            int rows_affected = ps.executeUpdate();

            if (rows_affected == 0) {
                Konekcija.getInstance().getConnection().rollback();
                throw new SQLException("GRESKA PRI UPISIVANJU!");
                //return false;
            }
            Konekcija.getInstance().getConnection().commit();
            return true;

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean azuriraj_amk(AMKlub amk) {

        try {
            String upit = "update amklub\n"
                    + "set\n"
                    + "nazivAMK = ?,\n"
                    + "brojTelefona = ?,\n"
                    + "email = ?,\n"
                    + "mestoId = ?\n"
                    + "where idAMK = ?";

            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);

            ps.setString(1, amk.getNazivAMKluba());
            ps.setString(2, amk.getBrojTelefona());
            ps.setString(3, amk.getEmailAMK());
            ps.setInt(4, amk.getMestoID());
            ps.setInt(5, amk.getIdAMK());

            int affected_rows = ps.executeUpdate();

            if (affected_rows == 0) {
                Konekcija.getInstance().getConnection().rollback();
                throw new SQLException("GRESKA PRI AZURIRANJU!");
            }
            Konekcija.getInstance().getConnection().commit();
            return true;

        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean obrisi_amk(int idAMK) {

        try {
            String upit = "delete from amklub\n"
                    + "where idAMK = " + idAMK;
            
            Statement st = Konekcija.getInstance().getConnection().createStatement();
            int affected_rows = st.executeUpdate(upit);
            
            if (affected_rows == 0) {
                Konekcija.getInstance().getConnection().rollback();
                throw new SQLException("GRESKA PRI AZURIRANJU!");
            }
            Konekcija.getInstance().getConnection().commit();
            return true;
            
        } catch (SQLException ex) {
            Logger.getLogger(DB_Broke_Stari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
