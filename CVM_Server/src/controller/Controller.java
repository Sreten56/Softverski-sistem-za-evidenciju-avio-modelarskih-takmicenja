/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import baza.DB_Broke_Stari;
import baza.DB_Broker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.AMKlub;
import model.Apstraktni_Domenski_Objekat;
import model.CVM;
import model.VrstaCVM;
import model.Kategorija;
import model.Mesto;
import model.Prijava;
import model.Takmicar;
import model.Takmicenje;
import model.VcvmCVM;
import operacije.AzurirajTakmicenjeSaPrijavamaSO;
import operacije.AzurirajVezuSO;
import operacije.DodajSO;
import operacije.IzmeniSO;
import operacije.KreirajTakmicenjeSO;
import operacije.LoginSO;
import operacije.ObrisiSO;
import operacije.ObrisiTakmicenjeSO;
//import operacije.PromeniVrstuVezeSO;
import operacije.VratiSveSO;
import transfer.Klijentski_Zahtev;

/**
 *
 * @author Srecko
 */
public class Controller {

    private static Controller instance;
    private DB_Broke_Stari dbb;

    List<CVM> podaciCVM;

    private Controller() {

//        dbb = new DB_Broke_Stari();
//        podaciCVM = new ArrayList<>();
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public List<CVM> getPodaciCVM() {
        return podaciCVM;
    }

    public void setPodaciCVM(List<CVM> podaciCVM) {
        this.podaciCVM = podaciCVM;
    }
    ////////////////////
    //
    // GENERICKI CRUD
    //
    ////////////////////

    public <T extends Apstraktni_Domenski_Objekat> boolean dodaj(T object) {
        try {
            return (boolean) new DodajSO<T>().izvrsi(object);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public <T extends Apstraktni_Domenski_Objekat> boolean izmeni(T object) {
        try {
            return (boolean) new IzmeniSO<T>().izvrsi(object);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public <T extends Apstraktni_Domenski_Objekat> boolean obrisi(T object) {
        try {
            return (boolean) new ObrisiSO<T>().izvrsi(object);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public <T extends Apstraktni_Domenski_Objekat> List<T> vratiSve(T object) {
        try {
            return (List<T>) new VratiSveSO<T>().izvrsi(object);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

////////////////////////////////////////
    //==================================================================
    // LOGIN
    //==================================================================
////////////////////////////////////////
//    public CVM login(String email, String lozinka) throws Exception {
//        ArrayList<Apstraktni_Domenski_Objekat> lista = DB_Broker.getInstance()
//                .select(new CVM()); // vrati sve CVM
//        for (Apstraktni_Domenski_Objekat ado : lista) {
//            CVM c = (CVM) ado;
//            if (c.getEmail().equals(email) && c.getLozinka().equals(lozinka)) {
//                return c; // korisnik pronađen
//            }
//        }
//        return null; // ako nema
//    }
    public CVM login(String email, String lozinka) throws Exception {
        try {
            return (CVM) new LoginSO().izvrsi(new CVM(email, lozinka));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
// BEZ NOVOG KONSTRUKTORA
//  public CVM login(String email, String lozinka) throws Exception {
//    try {
//        CVM cvm = new CVM();
//        cvm.setEmail(email);
//        cvm.setLozinka(lozinka);
//        return (CVM) new LoginSO().izvrsi(cvm);
//    } catch (Exception ex) {
//        ex.printStackTrace();
//        throw ex;
//    }
//}

////////////////////////////////////////
    //==================================================================
    //  OPERACIJE S' CVM
    //==================================================================
////////////////////////////////////////
    public CVM vratiCVMdetalje(CVM cvm) throws Exception {
        try {
            // koristi generičku SO klasu da povučeš sve CVM-ove
            List<CVM> svi = (List<CVM>) new VratiSveSO<CVM>().izvrsi(new CVM());

            // pronađi tačan po ID-u ili emailu
            for (CVM c : svi) {
                if (c.getIdCVM() == cvm.getIdCVM()
                        || (c.getEmail() != null && c.getEmail().equals(cvm.getEmail()))) {
                    return c;
                }
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public boolean dodaj_CVM(CVM cvm) {
//        try {
//            return DB_Broker.getInstance().insert(cvm);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return false;
//        }
//    }
    public boolean dodaj_CVM(CVM cvm) {
        try {
            return (boolean) new DodajSO<CVM>().izvrsi(cvm);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    
    /////////////////////
    //=====
    // POMOCNA ZZ REGISTRACIJU
    //=====
    //////////////////////
    
    public boolean postojiCVMEmail(String email) {
    try {
        List<CVM> svi = (List<CVM>) new VratiSveSO<CVM>().izvrsi(new CVM());
        for (CVM c : svi) {
            if (c.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}


//    public List<CVM> vrati_sve_CVM() {
//        try {
//            ArrayList<Apstraktni_Domenski_Objekat> lista = DB_Broker.getInstance().select(new CVM());
//            List<CVM> rezultat = new ArrayList<>();
//            for (Apstraktni_Domenski_Objekat ado : lista) {
//                rezultat.add((CVM) ado);
//            }
//            return rezultat;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return new ArrayList<>();
//        }
//    }
    public List<CVM> vrati_sve_CVM() {
        try {
            return (List<CVM>) new VratiSveSO<CVM>().izvrsi(new CVM());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

//    public boolean azurirajCVM(CVM cvm) throws Exception {
//        try {
//            boolean ok = DB_Broker.getInstance().update(cvm);
//            if (ok) {
//                DB_Broker.getInstance().commit();
//            } else {
//                DB_Broker.getInstance().rollback();
//            }
//            return ok;
//        } catch (Exception e) {
//            DB_Broker.getInstance().rollback();
//            throw e;
//        }
//    }
    public boolean azurirajCVM(CVM cvm) throws Exception {
        try {
            return (boolean) new IzmeniSO<CVM>().izvrsi(cvm);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    public boolean azurirajVezu(VcvmCVM veza) throws Exception {
//        try {
//            boolean ok = DB_Broker.getInstance().update(veza);
//            if (!ok) {
//                ok = DB_Broker.getInstance().insert(veza);
//            }
//
//            if (ok) {
//                DB_Broker.getInstance().commit();
//            } else {
//                DB_Broker.getInstance().rollback();
//            }
//
//            return ok;
//        } catch (Exception e) {
//            DB_Broker.getInstance().rollback();
//            throw e;
//        }
//    }
    public boolean azurirajVezu(VcvmCVM veza) throws Exception {
        try {
            return (boolean) new AzurirajVezuSO().izvrsi(veza);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//public boolean promeniVrstuVeze(VcvmCVM stara, VcvmCVM nova) {
//    try {
//        return (boolean) new PromeniVrstuVezeSO(nova).izvrsi(stara);
//    } catch (Exception e) {
//        e.printStackTrace();
//        return false;
//    }
//}
//    public List<VrstaCVM> vratiSveVrsteCVM() throws Exception {
//        ArrayList<Apstraktni_Domenski_Objekat> lista = DB_Broker.getInstance().select(new VrstaCVM());
//        List<VrstaCVM> rezultat = new ArrayList<>();
//        for (Apstraktni_Domenski_Objekat ado : lista) {
//            rezultat.add((VrstaCVM) ado);
//        }
//        return rezultat;
//    }
    public List<VrstaCVM> vratiSveVrsteCVM() {
        try {
            return (List<VrstaCVM>) new VratiSveSO<VrstaCVM>().izvrsi(new VrstaCVM());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

//    public List<VcvmCVM> vratiSvePodatkeCVMVrsta(int idCVM) throws Exception {
//        ArrayList<Apstraktni_Domenski_Objekat> sviCvm = DB_Broker.getInstance().select(new CVM());
//        CVM prijavljen = null;
//        for (Apstraktni_Domenski_Objekat ado : sviCvm) {
//            CVM c = (CVM) ado;
//            if (c.getIdCVM() == idCVM) {
//                prijavljen = c;
//                break;
//            }
//        }
//        if (prijavljen == null) {
//            return null;
//        }
//
//        ArrayList<Apstraktni_Domenski_Objekat> sveVeze = DB_Broker.getInstance().select(new VcvmCVM());
//        List<VcvmCVM> rezultat = new ArrayList<>();
//        for (Apstraktni_Domenski_Objekat ado : sveVeze) {
//            VcvmCVM veza = (VcvmCVM) ado;
//            if (veza.getCvm().getIdCVM() == prijavljen.getIdCVM()) {
//                rezultat.add(veza);
//            }
//        }
//
//        if (rezultat.isEmpty()) {
//            rezultat.add(new VcvmCVM(prijavljen, null, null));
//        }
//
//        return rezultat;
//    }
    public List<VcvmCVM> vratiSvePodatkeCVMVrsta(int idCVM) throws Exception {
        //  Vrati sve CVM iz baze
        List<CVM> sviCvm = (List<CVM>) new VratiSveSO<CVM>().izvrsi(new CVM());

        CVM prijavljen = null;
        for (CVM c : sviCvm) {
            if (c.getIdCVM() == idCVM) {
                prijavljen = c;
                break;
            }
        }

        if (prijavljen == null) {
            return null;
        }

        //  Vrati sve veze između CVM i VrstaCVM
        List<VcvmCVM> sveVeze = (List<VcvmCVM>) new VratiSveSO<VcvmCVM>().izvrsi(new VcvmCVM());

        //  Filtriraj veze po izabranom CVM
        List<VcvmCVM> rezultat = new ArrayList<>();
        for (VcvmCVM veza : sveVeze) {
            if (veza.getCvm().getIdCVM() == prijavljen.getIdCVM()) {
                rezultat.add(veza);
            }
        }

        //  Ako nema rezultata — kreiraj praznu vezu radi konzistentnosti
        if (rezultat.isEmpty()) {
            rezultat.add(new VcvmCVM(prijavljen, null, null));
        }

        return rezultat;
    }

////////////////////////////////////////
    //==================================================================
    //  OPERACIJE S' AMK
    //==================================================================
////////////////////////////////////////
    // vrati sve AMK
//    public List<AMKlub> vrati_sve_AMK() throws Exception {
//        ArrayList<Apstraktni_Domenski_Objekat> lista = DB_Broker.getInstance().select(new AMKlub());
//        List<AMKlub> rezultat = new ArrayList<>();
//        for (Apstraktni_Domenski_Objekat ado : lista) {
//            rezultat.add((AMKlub) ado);
//        }
//        return rezultat;
//    }
    public List<AMKlub> vrati_sve_AMK() {
        try {
            return (List<AMKlub>) new VratiSveSO<AMKlub>().izvrsi(new AMKlub());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    // vrati sva mesta
//    public List<Mesto> vrati_mesta() throws Exception {
//        ArrayList<Apstraktni_Domenski_Objekat> lista = DB_Broker.getInstance().select(new Mesto());
//        List<Mesto> rezultat = new ArrayList<>();
//        for (Apstraktni_Domenski_Objekat ado : lista) {
//            rezultat.add((Mesto) ado);
//        }
//        return rezultat;
//    }
    public List<Mesto> vrati_mesta() {
        try {
            return (List<Mesto>) new VratiSveSO<Mesto>().izvrsi(new Mesto());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

//    public List<Object[]> vratiSveInfoAMK() throws Exception {
//        ArrayList<Apstraktni_Domenski_Objekat> amkLista = DB_Broker.getInstance().select(new AMKlub());
//        ArrayList<Apstraktni_Domenski_Objekat> mestoLista = DB_Broker.getInstance().select(new Mesto());
//
//        List<Object[]> rezultat = new ArrayList<>();
//
//        for (Apstraktni_Domenski_Objekat adoAMK : amkLista) {
//            AMKlub amk = (AMKlub) adoAMK;
//
//            // pronađi mesto po mestoID
//            Mesto pronadjeno = null;
//            for (Apstraktni_Domenski_Objekat adoM : mestoLista) {
//                Mesto m = (Mesto) adoM;
//                if (m.getIdMesto() == amk.getMestoID()) {
//                    pronadjeno = m;
//                    break;
//                }
//            }
//
//            rezultat.add(new Object[]{amk, pronadjeno});
//        }
//
//        return rezultat;
//    }
    public List<Object[]> vratiSveInfoAMK() throws Exception {
        //  Učitaj sve klubove
        List<AMKlub> amkLista = (List<AMKlub>) new VratiSveSO<AMKlub>().izvrsi(new AMKlub());

        //  Učitaj sva mesta
        List<Mesto> mestoLista = (List<Mesto>) new VratiSveSO<Mesto>().izvrsi(new Mesto());

        //  Kombinuj rezultate
        List<Object[]> rezultat = new ArrayList<>();

        for (AMKlub amk : amkLista) {
            Mesto pronadjeno = null;
            for (Mesto m : mestoLista) {
                if (m.getIdMesto() == amk.getMestoID()) {
                    pronadjeno = m;
                    break;
                }
            }
            rezultat.add(new Object[]{amk, pronadjeno});
        }

        return rezultat;
    }

//// dodaj AMK
//    public boolean dodaj_amk(AMKlub amk) throws Exception {
//        try {
//            boolean uspesno = DB_Broker.getInstance().insert(amk);
//            DB_Broker.getInstance().commit();   // ✅ obavezno, jer si isključio autoCommit
//            return uspesno;
//        } catch (Exception e) {
//            DB_Broker.getInstance().rollback();
//            throw e;
//        }
//    }
    public boolean dodaj_amk(AMKlub amk) {
        try {
            return (boolean) new DodajSO<AMKlub>().izvrsi(amk);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    public boolean azuriraj_mesto(Mesto mesto) throws Exception {
//        try {
//            boolean uspesno = DB_Broker.getInstance().update(mesto);
//            DB_Broker.getInstance().commit();   // ✅ obavezno
//            return uspesno;
//        } catch (Exception e) {
//            DB_Broker.getInstance().rollback();
//            throw e;
//        }
//    }
    public boolean azuriraj_mesto(Mesto mesto) {
        try {
            return (boolean) new IzmeniSO<Mesto>().izvrsi(mesto);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//// vrati najveći ID AMK
//    public int vratiNajveciIdAMK() throws Exception {
//        ArrayList<Apstraktni_Domenski_Objekat> lista = DB_Broker.getInstance().select(new AMKlub());
//        int maxId = 0;
//        for (Apstraktni_Domenski_Objekat ado : lista) {
//            AMKlub amk = (AMKlub) ado;
//            if (amk.getIdAMK() > maxId) {
//                maxId = amk.getIdAMK();
//            }
//        }
//        return maxId;
//    }
    public int vratiNajveciIdAMK() {
        try {
            // koristi SO umesto direktnog DB_Broker poziva
            List<AMKlub> lista = (List<AMKlub>) new VratiSveSO<AMKlub>().izvrsi(new AMKlub());

            int maxId = 0;
            for (AMKlub amk : lista) {
                if (amk.getIdAMK() > maxId) {
                    maxId = amk.getIdAMK();
                }
            }
            return maxId;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

//    public boolean obrisi_amk(int idAMK) {
//        try {
//            AMKlub amk = new AMKlub();
//            amk.setIdAMK(idAMK);
//
//            boolean obrisan = DB_Broker.getInstance().delete(amk);
//
//            if (obrisan) {
//                DB_Broker.getInstance().getConnection().commit();
//                return true;
//            } else {
//                DB_Broker.getInstance().getConnection().rollback();
//                return false; // nije pogođen nijedan red
//            }
//        } catch (Exception e) {
//            try {
//                DB_Broker.getInstance().getConnection().rollback();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//            return false;
//        }
//    }
    public boolean obrisi_amk(int idAMK) {
        try {
            AMKlub amk = new AMKlub();
            amk.setIdAMK(idAMK);
            return (boolean) new ObrisiSO<AMKlub>().izvrsi(amk);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    public boolean azuriraj_amk(AMKlub amk) {
//        try {
//            boolean azuriran = DB_Broker.getInstance().update(amk);
//
//            if (azuriran) {
//                DB_Broker.getInstance().getConnection().commit();  // potvrdi izmene
//                return true;
//            } else {
//                DB_Broker.getInstance().getConnection().rollback(); // nema promena, vrati unazad
//                return false; // AMK ne postoji
//            }
//        } catch (Exception e) {
//            try {
//                DB_Broker.getInstance().getConnection().rollback();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//            return false;
//        }
//    }
    public boolean azuriraj_amk(AMKlub amk) {
        try {
            return (boolean) new IzmeniSO<AMKlub>().izvrsi(amk);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

////////////////////////////////////////
    //==================================================================
    //  OPERACIJE S' TAKMICARI
    //==================================================================
////////////////////////////////////////
//    public List<Takmicar> vratiSveTakmicare() throws Exception {
//        ArrayList<Apstraktni_Domenski_Objekat> lista = DB_Broker.getInstance().select(new Takmicar());
//        List<Takmicar> rezultat = new ArrayList<>();
//        for (Apstraktni_Domenski_Objekat ado : lista) {
//            rezultat.add((Takmicar) ado);
//        }
//        return rezultat;
//    }
    public List<Takmicar> vratiSveTakmicare() throws Exception {
        try {
            return (List<Takmicar>) new VratiSveSO<Takmicar>().izvrsi(new Takmicar());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

//    public List<Kategorija> vrati_SVE_kategorije() throws Exception {
//        ArrayList<Apstraktni_Domenski_Objekat> lista = DB_Broker.getInstance().select(new Kategorija());
//        List<Kategorija> rezultat = new ArrayList<>();
//        for (Apstraktni_Domenski_Objekat ado : lista) {
//            rezultat.add((Kategorija) ado);
//        }
//        return rezultat;
//    }
    public List<Kategorija> vrati_SVE_kategorije() throws Exception {
        try {
            return (List<Kategorija>) new VratiSveSO<Kategorija>().izvrsi(new Kategorija());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

//    public int vratiNajveciIdTakmicara() throws Exception {
//        ArrayList<Apstraktni_Domenski_Objekat> lista = DB_Broker.getInstance().select(new Takmicar());
//        int maxId = 0;
//        for (Apstraktni_Domenski_Objekat ado : lista) {
//            Takmicar t = (Takmicar) ado;
//            if (t.getIdTakmicar() > maxId) {
//                maxId = t.getIdTakmicar();
//            }
//        }
//        return maxId;
//    }
    public int vratiNajveciIdTakmicara() {
        try {
            List<Takmicar> lista = (List<Takmicar>) new VratiSveSO<Takmicar>().izvrsi(new Takmicar());
            int maxId = 0;

            for (Takmicar t : lista) {
                if (t.getIdTakmicar() > maxId) {
                    maxId = t.getIdTakmicar();
                }
            }
            return maxId;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

//    public boolean dodaj_takmicara(Takmicar tak) throws Exception {
//        try {
//            boolean uspesno = DB_Broker.getInstance().insert(tak);
//            DB_Broker.getInstance().commit();
//            return uspesno;
//        } catch (Exception e) {
//            DB_Broker.getInstance().rollback();
//            throw e;
//        }
//    }
    public boolean dodaj_takmicara(Takmicar tak) {
        try {
            return (boolean) new DodajSO<Takmicar>().izvrsi(tak);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    public boolean obrisi_takmicara(Takmicar tak) throws Exception {
//        try {
//            boolean uspesno = DB_Broker.getInstance().delete(tak);
//            DB_Broker.getInstance().commit();
//            return uspesno;
//        } catch (Exception e) {
//            DB_Broker.getInstance().rollback();
//            throw e;
//        }
//    }
    public boolean obrisi_takmicara(Takmicar tak) {
        try {
            return (boolean) new ObrisiSO<Takmicar>().izvrsi(tak);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean azuriraj_takmicara(Takmicar tak) {
        try {
            return (boolean) new IzmeniSO<Takmicar>().izvrsi(tak);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    public List<Object[]> vrati_SVE_potrebne_info_tak_kat_amk() {
//        try {
//            List<Apstraktni_Domenski_Objekat> sviTakmicari = DB_Broker.getInstance().select(new Takmicar());
//            List<Apstraktni_Domenski_Objekat> sveKategorije = DB_Broker.getInstance().select(new Kategorija());
//            List<Apstraktni_Domenski_Objekat> sviAMK = DB_Broker.getInstance().select(new AMKlub());
//
//            List<Object[]> rezultat = new ArrayList<>();
//
//            for (Apstraktni_Domenski_Objekat adoTak : sviTakmicari) {
//                Takmicar tak = (Takmicar) adoTak;
//
//                // Nađi kategoriju preko ID
//                Kategorija kat = sveKategorije.stream()
//                        .map(o -> (Kategorija) o)
//                        .filter(k -> k.getIdKategorija() == tak.getKatID())
//                        .findFirst()
//                        .orElse(null);
//
//                // Nađi AM klub preko ID
//                AMKlub amk = sviAMK.stream()
//                        .map(o -> (AMKlub) o)
//                        .filter(a -> a.getIdAMK() == tak.getAMKid())
//                        .findFirst()
//                        .orElse(null);
//
//                // ubaci sve troje u niz
//                rezultat.add(new Object[]{tak, kat, amk});
//            }
//
//            return rezultat;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    public List<Object[]> vrati_SVE_potrebne_info_tak_kat_amk() {
        try {
            //  Učitaj sve preko postojeće sistemske operacije
            List<Takmicar> sviTakmicari = (List<Takmicar>) new VratiSveSO<Takmicar>().izvrsi(new Takmicar());
            List<Kategorija> sveKategorije = (List<Kategorija>) new VratiSveSO<Kategorija>().izvrsi(new Kategorija());
            List<AMKlub> sviAMK = (List<AMKlub>) new VratiSveSO<AMKlub>().izvrsi(new AMKlub());

            //  Poveži ih
            List<Object[]> rezultat = new ArrayList<>();

            for (Takmicar tak : sviTakmicari) {
                // nađi kategoriju
                Kategorija kat = sveKategorije.stream()
                        .filter(k -> k.getIdKategorija() == tak.getKatID())
                        .findFirst()
                        .orElse(null);

                // nađi AM klub
                AMKlub amk = sviAMK.stream()
                        .filter(a -> a.getIdAMK() == tak.getAMKid())
                        .findFirst()
                        .orElse(null);

                // dodaj kombinaciju
                rezultat.add(new Object[]{tak, kat, amk});
            }

            return rezultat;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

////////////////////////////////////////
    //==================================================================
    //  OPERACIJE S' TAKMICENJA
    //==================================================================
////////////////////////////////////////
//    public List<Takmicenje> vrati_sva_takmicenja() throws Exception {
//        // 1. Učitaj takmičenja
//        ArrayList<Apstraktni_Domenski_Objekat> listaTak = DB_Broker.getInstance().select(new Takmicenje());
//        List<Takmicenje> takmicenja = new ArrayList<>();
//        for (Apstraktni_Domenski_Objekat ado : listaTak) {
//            takmicenja.add((Takmicenje) ado);
//        }
//
//        // 2. Učitaj CVM-ove
//        ArrayList<Apstraktni_Domenski_Objekat> listaCvm = DB_Broker.getInstance().select(new CVM());
//        List<CVM> sviCvm = new ArrayList<>();
//        for (Apstraktni_Domenski_Objekat ado : listaCvm) {
//            sviCvm.add((CVM) ado);
//        }
//
//        // 3. Učitaj kategorije
//        ArrayList<Apstraktni_Domenski_Objekat> listaKat = DB_Broker.getInstance().select(new Kategorija());
//        List<Kategorija> sveKat = new ArrayList<>();
//        for (Apstraktni_Domenski_Objekat ado : listaKat) {
//            sveKat.add((Kategorija) ado);
//        }
//
//        // 4. Poveži
//        for (Takmicenje t : takmicenja) {
//            for (CVM c : sviCvm) {
//                if (t.getCvm().getIdCVM() == c.getIdCVM()) {
//                    t.setCvm(c); // upiše ceo objekat sa nazivom
//                    break;
//                }
//            }
//            for (Kategorija k : sveKat) {
//                if (t.getKat().getIdKategorija() == k.getIdKategorija()) {
//                    t.setKat(k); // upiše ceo objekat sa nazivom
//                    break;
//                }
//            }
//        }
//
//        return takmicenja;
//    }
    public List<Takmicenje> vrati_sva_takmicenja() {
        try {
            //  Učitaj takmičenja kroz SO sloj
            List<Takmicenje> takmicenja = (List<Takmicenje>) new VratiSveSO<Takmicenje>().izvrsi(new Takmicenje());

            //  Učitaj CVM-ove kroz SO sloj
            List<CVM> sviCvm = (List<CVM>) new VratiSveSO<CVM>().izvrsi(new CVM());

            //  Učitaj kategorije kroz SO sloj
            List<Kategorija> sveKat = (List<Kategorija>) new VratiSveSO<Kategorija>().izvrsi(new Kategorija());

            //  Poveži podatke po ID-jevima
            for (Takmicenje t : takmicenja) {
                // poveži CVM
                for (CVM c : sviCvm) {
                    if (t.getCvm() != null && t.getCvm().getIdCVM() == c.getIdCVM()) {
                        t.setCvm(c);
                        break;
                    }
                }

                // poveži Kategoriju
                for (Kategorija k : sveKat) {
                    if (t.getKat() != null && t.getKat().getIdKategorija() == k.getIdKategorija()) {
                        t.setKat(k);
                        break;
                    }
                }
            }

            return takmicenja;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Vrati prijavljene takmičare za takmičenje
//    public List<Object[]> vratiPrijavljeneTakmicare(Takmicenje tak) throws Exception {
//        // 1. Učitaj sve prijave
//        ArrayList<Apstraktni_Domenski_Objekat> listaPrijava = DB_Broker.getInstance().select(new Prijava());
//        List<Prijava> prijave = new ArrayList<>();
//        for (Apstraktni_Domenski_Objekat ado : listaPrijava) {
//            Prijava p = (Prijava) ado;
//            if (p.getTakmicenje().getIdTakmicenje() == tak.getIdTakmicenje()) {
//                prijave.add(p);
//            }
//        }
//
//        // 2. Učitaj sve takmičare
//        ArrayList<Apstraktni_Domenski_Objekat> listaTak = DB_Broker.getInstance().select(new Takmicar());
//        List<Takmicar> sviTakmicari = new ArrayList<>();
//        for (Apstraktni_Domenski_Objekat ado : listaTak) {
//            sviTakmicari.add((Takmicar) ado);
//        }
//
//        // 3. Učitaj sve AM klubove
//        ArrayList<Apstraktni_Domenski_Objekat> listaAmk = DB_Broker.getInstance().select(new AMKlub());
//        List<AMKlub> sviAmkovi = new ArrayList<>();
//        for (Apstraktni_Domenski_Objekat ado : listaAmk) {
//            sviAmkovi.add((AMKlub) ado);
//        }
//
//        // 4. Formiraj rezultat kao listu [Takmicar, AMKlub]
//        List<Object[]> rezultat = new ArrayList<>();
//        for (Prijava p : prijave) {
//            Takmicar trazeni = null;
//            for (Takmicar t : sviTakmicari) {
//                if (t.getIdTakmicar() == p.getTakmicar().getIdTakmicar()) {
//                    trazeni = t;
//                    break;
//                }
//            }
//
//            AMKlub amk = null;
//            if (trazeni != null) {
//                for (AMKlub aa : sviAmkovi) {
//                    if (aa.getIdAMK() == trazeni.getAMKid()) {   // ✅ OVO JE KLJUČNA PROMENA
//                        amk = aa;
//                        break;
//                    }
//                }
//                rezultat.add(new Object[]{trazeni, amk});
//            }
//        }
//
//        return rezultat;
//    }
    public List<Object[]> vratiPrijavljeneTakmicare(Takmicenje tak) {
        try {
            //  Učitaj sve prijave
            List<Prijava> svePrijave = (List<Prijava>) new VratiSveSO<Prijava>().izvrsi(new Prijava());

            // filtriraj samo prijave za konkretno takmičenje
            List<Prijava> prijaveZaTakmicenje = new ArrayList<>();
            for (Prijava p : svePrijave) {
                if (p.getTakmicenje() != null && p.getTakmicenje().getIdTakmicenje() == tak.getIdTakmicenje()) {
                    prijaveZaTakmicenje.add(p);
                }
            }

            //  Učitaj sve takmičare
            List<Takmicar> sviTakmicari = (List<Takmicar>) new VratiSveSO<Takmicar>().izvrsi(new Takmicar());

            //  Učitaj sve AM klubove
            List<AMKlub> sviAmkovi = (List<AMKlub>) new VratiSveSO<AMKlub>().izvrsi(new AMKlub());

            //  Formiraj rezultat [Takmicar, AMKlub]
            List<Object[]> rezultat = new ArrayList<>();

            for (Prijava p : prijaveZaTakmicenje) {
                Takmicar trazeni = null;
                for (Takmicar t : sviTakmicari) {
                    if (p.getTakmicar() != null && t.getIdTakmicar() == p.getTakmicar().getIdTakmicar()) {
                        trazeni = t;
                        break;
                    }
                }

                AMKlub amk = null;
                if (trazeni != null) {
                    for (AMKlub a : sviAmkovi) {
                        if (a.getIdAMK() == trazeni.getAMKid()) {
                            amk = a;
                            break;
                        }
                    }
                    rezultat.add(new Object[]{trazeni, amk});
                }
            }

            return rezultat;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

//    public List<Object[]> vratiTakmicareIzKategorije(Kategorija kat) throws Exception {
//        // 1. Učitaj sve takmičare
//        ArrayList<Apstraktni_Domenski_Objekat> listaTak = DB_Broker.getInstance().select(new Takmicar());
//        List<Takmicar> takmicari = new ArrayList<>();
//        for (Apstraktni_Domenski_Objekat ado : listaTak) {
//            takmicari.add((Takmicar) ado);
//        }
//
//        // 2. Učitaj sve AMK-ove
//        ArrayList<Apstraktni_Domenski_Objekat> listaAMK = DB_Broker.getInstance().select(new AMKlub());
//        List<AMKlub> amkovi = new ArrayList<>();
//        for (Apstraktni_Domenski_Objekat ado : listaAMK) {
//            amkovi.add((AMKlub) ado);
//        }
//
//        // 3. Filtriraj takmičare po kategoriji
//        List<Object[]> rezultat = new ArrayList<>();
//        for (Takmicar t : takmicari) {
//            if (t.getKatID() == kat.getIdKategorija()) {
//                AMKlub pripada = amkovi.stream()
//                        .filter(a -> a.getIdAMK() == t.getAMKid())
//                        .findFirst()
//                        .orElse(null);
//                rezultat.add(new Object[]{t, pripada});
//            }
//        }
//
//        return rezultat;
//    }
    public List<Object[]> vratiTakmicareIzKategorije(Kategorija kat) {
        try {
            //  vrni sve takmicare
            List<Takmicar> takmicari = (List<Takmicar>) new VratiSveSO<Takmicar>().izvrsi(new Takmicar());

            //  vrni sve AMK klubove
            List<AMKlub> amkovi = (List<AMKlub>) new VratiSveSO<AMKlub>().izvrsi(new AMKlub());

            //  Filtriraj takmicare po kategoriji
            List<Object[]> rezultat = new ArrayList<>();

            for (Takmicar t : takmicari) {
                if (t.getKatID() == kat.getIdKategorija()) {

                    // pronadji odgovarajući AMK klub
                    AMKlub pripada = null;
                    for (AMKlub a : amkovi) {
                        if (a.getIdAMK() == t.getAMKid()) {
                            pripada = a;
                            break;
                        }
                    }

                    rezultat.add(new Object[]{t, pripada});
                }
            }

            return rezultat;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // === Ažuriraj osnovne podatke o takmičenju ===
//    public boolean azuriraj_takmicenje(Takmicenje t) throws Exception {
//        try {
//            boolean uspesno = DB_Broker.getInstance().update(t);
//            DB_Broker.getInstance().commit();
//            return uspesno;
//        } catch (Exception e) {
//            DB_Broker.getInstance().rollback();
//            throw e;
//        }
//    }
    public boolean azuriraj_takmicenje(Takmicenje t) {
        try {
            return (boolean) new IzmeniSO<Takmicenje>().izvrsi(t);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    public boolean obrisiTakmicenje(Takmicenje tak) throws Exception {
//        try {
//            // 1. Obriši sve prijave vezane za ovo takmičenje
//            Prijava p = new Prijava();
//            p.setTakmicenje(tak);
//            DB_Broker.getInstance().delete(p);
//
//            // 2. Obriši samo takmičenje
//            boolean uspesno = DB_Broker.getInstance().delete(tak);
//
//            DB_Broker.getInstance().commit();
//            return uspesno;
//        } catch (Exception e) {
//            DB_Broker.getInstance().rollback();
//            throw e;
//        }
//    }
    public boolean obrisiTakmicenje(Takmicenje tak) {
        try {
            return (boolean) new ObrisiTakmicenjeSO().izvrsi(tak);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    public boolean kreirajTakmicenje(Takmicenje takmicenje, List<Takmicar> prijavljeni) throws Exception {
//        try {
//            DB_Broker db = DB_Broker.getInstance();
//
//            // 1️⃣ Unesi takmičenje i preuzmi njegov ID
//            int idTakmicenje = db.insertReturnID(takmicenje);
//            takmicenje.setIdTakmicenje(idTakmicenje);
//
//            // 2️⃣ Dodaj sve prijave
//            for (Takmicar t : prijavljeni) {
//                Prijava p = new Prijava();
//                p.setTakmicenje(takmicenje);
//                p.setTakmicar(t);
//                db.insert(p);
//            }
//
//            db.getConnection().commit();
//            return true;
//
//        } catch (Exception e) {
//            DB_Broker.getInstance().getConnection().rollback();
//            throw new Exception("Greška pri kreiranju takmičenja: " + e.getMessage());
//        }
//    }
    public boolean kreirajTakmicenje(Takmicenje takmicenje, List<Takmicar> prijavljeni) {
        try {
            return (boolean) new KreirajTakmicenjeSO(prijavljeni).izvrsi(takmicenje);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

////////////////////////////////////////
    //==================================================================
    //  OPERACIJE S' PRIJAVAMA 
    //==================================================================
////////////////////////////////////////
// Vrati sve prijave za jedno takmičenje
//    public List<Prijava> vrati_prijave_za_takmicenje(Takmicenje tak) throws Exception {
//        ArrayList<Apstraktni_Domenski_Objekat> lista = DB_Broker.getInstance().select(new Prijava());
//        List<Prijava> rezultat = new ArrayList<>();
//
//        for (Apstraktni_Domenski_Objekat ado : lista) {
//            Prijava p = (Prijava) ado;
//            if (p.getTakmicenje().getIdTakmicenje() == tak.getIdTakmicenje()) {
//                rezultat.add(p);
//            }
//        }
//        return rezultat;
//    }
    public List<Prijava> vrati_prijave_za_takmicenje(Takmicenje tak) {
        try {
            //  vrati sve prijave 
            List<Prijava> svePrijave = (List<Prijava>) new VratiSveSO<Prijava>().izvrsi(new Prijava());

            //  filtriraj samo one koje pripadaju prosledenom takmicenju
            List<Prijava> rezultat = new ArrayList<>();
            for (Prijava p : svePrijave) {
                if (p.getTakmicenje() != null && p.getTakmicenje().getIdTakmicenje() == tak.getIdTakmicenje()) {
                    rezultat.add(p);
                }
            }

            return rezultat;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

//    public boolean azurirajTakmicenje(Takmicenje tak, List<Takmicar> prijavljeni) throws Exception {
//        try {
//            // 1. Ažuriraj osnovne podatke
//            DB_Broker.getInstance().update(tak);
//
//            // 2. Obrisi sve postojeće prijave za ovo takmičenje
//            Prijava p = new Prijava();
//            p.setTakmicenje(tak);
//            DB_Broker.getInstance().delete(p);
//
//            // 3. Ubaci nove prijave
//            for (Takmicar t : prijavljeni) {
//                Prijava nova = new Prijava();
//                nova.setTakmicenje(tak);
//                nova.setTakmicar(t);
//                DB_Broker.getInstance().insert(nova);
//            }
//
//            DB_Broker.getInstance().commit();
//            return true;
//        } catch (Exception e) {
//            DB_Broker.getInstance().rollback();
//            throw e;
//        }
//    }
/////    ///////////////
    //==========
    // ovo radi bez upotrebe SO baas za ovu metodu, ali je nekako nabacano ovde
    // i naravno u obradi kz mora da se preimenuje metoda iz slucaja AZURIRAJ_TAKMICENJE
    //==========
/////    /////////////// 
//    public boolean azurirajTakmicenje(Takmicenje tak, List<Takmicar> prijavljeni) {
//    try {
//        // 1️⃣ Ažuriraj osnovne podatke o takmičenju
//        new IzmeniSO<Takmicenje>().izvrsi(tak);
//
//        // 2️⃣ Obriši postojeće prijave
//        Prijava p = new Prijava();
//        p.setTakmicenje(tak);
//        new ObrisiSO<Prijava>().izvrsi(p);
//
//        // 3️⃣ Dodaj nove prijave
//        for (Takmicar t : prijavljeni) {
//            Prijava nova = new Prijava();
//            nova.setTakmicenje(tak);
//            nova.setTakmicar(t);
//            new DodajSO<Prijava>().izvrsi(nova);
//        }
//
//        DB_Broker.getInstance().commit();
//        return true;
//
//    } catch (Exception e) {
//        try {
//            DB_Broker.getInstance().rollback();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        e.printStackTrace();
//        return false;
//    }
//}
    public boolean azurirajTakmicenjeSaPrijavama(Takmicenje tak, List<Takmicar> prijavljeni) {
        try {
            return (boolean) new AzurirajTakmicenjeSaPrijavamaSO(prijavljeni).izvrsi(tak);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

// Dodaj prijavu
//    public boolean dodaj_prijavu(Prijava prijava) throws Exception {
//        try {
//            boolean uspesno = DB_Broker.getInstance().insert(prijava);
//            DB_Broker.getInstance().commit();
//            return uspesno;
//        } catch (Exception e) {
//            DB_Broker.getInstance().rollback();
//            throw e;
//        }
//    }
    public boolean dodaj_prijavu(Prijava prijava) {
        try {
            return (boolean) new DodajSO<Prijava>().izvrsi(prijava);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

// Obriši prijavu
//    public boolean obrisi_prijavu(Prijava prijava) throws Exception {
//        try {
//            boolean uspesno = DB_Broker.getInstance().delete(prijava);
//            DB_Broker.getInstance().commit();
//            return uspesno;
//        } catch (Exception e) {
//            DB_Broker.getInstance().rollback();
//            throw e;
//        }
//    }
    public boolean obrisi_prijavu(Prijava prijava) {
        try {
            return (boolean) new ObrisiSO<Prijava>().izvrsi(prijava);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

////////////////////////
    ///////////////////// IZGLEDA NEPOTREBNA ZA NOVO STANJE SA SO
//// === Uskladi prijave sa trenutnim stanjem ===
//    public void azuriraj_prijave(Takmicenje tak, List<Takmicar> prijavljeni) throws Exception {
//        List<Prijava> starePrijave = vrati_prijave_za_takmicenje(tak);
//
//        // 1. Dodaj nove prijave
//        for (Takmicar novi : prijavljeni) {
//            boolean vecPostoji = false;
//            for (Prijava stara : starePrijave) {
//                if (stara.getTakmicar().getIdTakmicar() == novi.getIdTakmicar()) {
//                    vecPostoji = true;
//                    break;
//                }
//            }
//            if (!vecPostoji) {
//                DB_Broker.getInstance().insert(new Prijava(0, tak, novi));
//            }
//        }
//
//        // 2. Ukloni odjavljene
//        for (Prijava stara : starePrijave) {
//            boolean josPostoji = false;
//            for (Takmicar novi : prijavljeni) {
//                if (stara.getTakmicar().getIdTakmicar() == novi.getIdTakmicar()) {
//                    josPostoji = true;
//                    break;
//                }
//            }
//            if (!josPostoji) {
//                DB_Broker.getInstance().delete(stara);
//            }
//        }
//
//        DB_Broker.getInstance().commit();
//    }
////////////////////////////////////////
    //==================================================================
    //  NOVE AL ZASTARELE METODE
    // =====================================
//  public boolean azurirajCVM(CVM cvm) throws Exception {
//        try {
//            boolean updated = DB_Broker.getInstance().update(cvm);
//            if (updated) {
//                DB_Broker.getInstance().commit();
//            } else {
//                DB_Broker.getInstance().rollback();
//            }
//            return updated;
//        } catch (Exception e) {
//            DB_Broker.getInstance().rollback();
//            throw e;
//        }
//    }
//
//    public boolean azurirajVezu(VcvmCVM veza) throws Exception {
//        try {
//            boolean updated = DB_Broker.getInstance().update(veza);
//            if (!updated) {
//                updated = DB_Broker.getInstance().insert(veza);
//            }
//
//            if (updated) {
//                DB_Broker.getInstance().commit();
//            } else {
//                DB_Broker.getInstance().rollback();
//            }
//            return updated;
//        } catch (Exception e) {
//            DB_Broker.getInstance().rollback();
//            throw e;
//        }
//    }
// Vrati sve takmičare iz određene kategorije
//public List<Object[]> vratiTakmicareIzKategorije(Kategorija kat) throws Exception {
//    ArrayList<Apstraktni_Domenski_Objekat> listaTak = DB_Broker.getInstance().select(new Takmicar());
//    List<Takmicar> sviTakmicari = new ArrayList<>();
//    for (Apstraktni_Domenski_Objekat ado : listaTak) {
//        sviTakmicari.add((Takmicar) ado);
//    }
//
//    ArrayList<Apstraktni_Domenski_Objekat> listaAmk = DB_Broker.getInstance().select(new AMKlub());
//    List<AMKlub> sviAmkovi = new ArrayList<>();
//    for (Apstraktni_Domenski_Objekat ado : listaAmk) {
//        sviAmkovi.add((AMKlub) ado);
//    }
//
//    List<Object[]> rezultat = new ArrayList<>();
//    for (Takmicar t : sviTakmicari) {
//        if (t.getKatID() == kat.getIdKategorija()) {
//            AMKlub amk = null;
//            for (AMKlub aa : sviAmkovi) {
//                if (aa.getIdAMK()== t.getAMKid()) {   // ✅ I OVDE PROMENA
//                    amk = aa;
//                    break;
//                }
//            }
//            rezultat.add(new Object[]{t, amk});
//        }
//    }
//    return rezultat;
//}
    //
//// === Vrati sve prijave za dato takmičenje ===
//public List<Prijava> vrati_prijave_za_takmicenje(Takmicenje tak) throws Exception {
//    ArrayList<Apstraktni_Domenski_Objekat> lista = DB_Broker.getInstance().select(new Prijava());
//    List<Prijava> rezultat = new ArrayList<>();
//    for (Apstraktni_Domenski_Objekat ado : lista) {
//        Prijava p = (Prijava) ado;
//        if (p.getTakmicenje().getIdTakmicenje() == tak.getIdTakmicenje()) {
//            rezultat.add(p);
//        }
//    }
//    return rezultat;
//}
//    // === TAKMIČAR ===
//public List<Takmicar> vratiSveTakmicare() throws Exception {
//    ArrayList<Apstraktni_Domenski_Objekat> lista = DB_Broker.getInstance().select(new Takmicar());
//    List<Takmicar> rezultat = new ArrayList<>();
//    for (Apstraktni_Domenski_Objekat ado : lista) {
//        rezultat.add((Takmicar) ado);
//    }
//    return rezultat;
//}
//
//public boolean dodaj_takmicara(Takmicar takmicar) throws Exception {
//    try {
//        boolean ok = DB_Broker.getInstance().insert(takmicar);
//        DB_Broker.getInstance().commit();
//        return ok;
//    } catch (Exception e) {
//        DB_Broker.getInstance().rollback();
//        throw e;
//    }
//}
//
//public boolean azuriraj_takmicara(Takmicar takmicar) throws Exception {
//    try {
//        boolean ok = DB_Broker.getInstance().update(takmicar);
//        if (ok) DB_Broker.getInstance().commit();
//        else DB_Broker.getInstance().rollback();
//        return ok;
//    } catch (Exception e) {
//        DB_Broker.getInstance().rollback();
//        throw e;
//    }
//}
//
//public boolean obrisi_takmicara(int idTakmicar) throws Exception {
//    try {
//        Takmicar t = new Takmicar();
//        t.setIdTakmicar(idTakmicar);
//        boolean ok = DB_Broker.getInstance().delete(t);
//        if (ok) DB_Broker.getInstance().commit();
//        else DB_Broker.getInstance().rollback();
//        return ok;
//    } catch (Exception e) {
//        DB_Broker.getInstance().rollback();
//        throw e;
//    }
//}
//
//
//// === KATEGORIJA ===
//public List<Kategorija> vratiSveKategorije() throws Exception {
//    ArrayList<Apstraktni_Domenski_Objekat> lista = DB_Broker.getInstance().select(new Kategorija());
//    List<Kategorija> rezultat = new ArrayList<>();
//    for (Apstraktni_Domenski_Objekat ado : lista) {
//        rezultat.add((Kategorija) ado);
//    }
//    return rezultat;
//}
//
//    
//    public boolean obrisi_amk(int idAMK) {
//        try {
//            AMKlub amk = new AMKlub();
//            amk.setIdAMK(idAMK);
//
//            boolean obrisan = DB_Broker.getInstance().delete(amk);
//            DB_Broker.getInstance().getConnection().commit(); // obavezno commit
//            return obrisan;
//        } catch (Exception e) {
//            try {
//                DB_Broker.getInstance().getConnection().rollback();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//            return false;
//        }
//    }
//    public boolean azuriraj_amk(AMKlub amk) {
//        try {
//            boolean azuriran = DB_Broker.getInstance().update(amk);
//            DB_Broker.getInstance().getConnection().commit();  // potvrdi izmene
//            return azuriran;
//        } catch (Exception e) {
//            try {
//                DB_Broker.getInstance().getConnection().rollback();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//            return false;
//        }
//    }
///////////////////////////////
    //
    // STARI CONTROLLER
    //
///////////////////////////////    
    //    public boolean login(String email, String lozinka) {
//   
//        return dbb.login(email, lozinka);
//
//    }
    //    public List<CVM> vrati_SVE_podatke(String email, String lozinka) {
//        return dbb.vrati_SVE_podatke(email, lozinka);
//    }
//
//    public List<VrstaCVM> getAllDrzave() throws Exception {
//        return dbb.getAllDrzave();
//    }
//
//    public List<Object[]> vrati_SVE_potrebne_info_tak_kat_amk() {
//        return dbb.vrati_SVE_potrebne_info_tak_kat_amk();
//    }
//
//    public List<Kategorija> vrati_SVE_kategorije() {
//        return dbb.vrati_SVE_kategorije();
//    }
//
//    public List<AMKlub> vrati_sve_AMK() {
//        return dbb.vrati_sve_AMK();
//    }
//
//    public boolean azuriraj_takmicare(Takmicar tak) {
//        return dbb.azuriraj_takmicare(tak);
//    }
//
//    public boolean dodaj_takmicara(Takmicar tak) {
//        return dbb.dodaj_takmicara(tak);
//    }
//
//    public int vratiNajveciIdTakmicara() {
//        return dbb.vratiNajveciIdTakmicara();
//    }
//
//    public boolean obrisi_takmicara(int idTakmicar) {
//        return dbb.obrisi_takmicara(idTakmicar);
//    }
//
//    public List<Object[]> vrati_SVE_potrebne_info_amk_mesto() {
//        return dbb.vrati_SVE_potrebne_info_amk_mesto();
//    }
//
//    public List<Mesto> vrati_mesta() {
//        return dbb.vrati_mesta();
//    }
//
//    public int vratiNajveciIdAMK() {
//        return dbb.vratiNajveciIdAMK();
//    }
//
//    public boolean dodaj_amk(AMKlub amk) {
//        return dbb.dodaj_amk(amk);
//    }
//
//    public boolean azuriraj_amk(AMKlub amk) {
//        return dbb.azuriraj_amk(amk);
//    }
//
//    public boolean obrisi_amk(int idAMK) {
//        return dbb.obrisi_amk(idAMK);
//    }
}
