/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import controller.Controller;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.AMKlub;
import model.CVM;
import model.VrstaCVM;
import model.Kategorija;
import model.Mesto;
import model.Takmicar;
import model.Takmicenje;
import model.VcvmCVM;
import operacije.Operacije;
import pomocne.Pomocne;
import transfer.Klijentski_Zahtev;
import transfer.Serverski_Odgovor;
import java.sql.*;

/**
 *
 * @author Srecko
 */
public class Obrada_Klijentskih_Zahteva extends Thread {

    private Socket soket;
    private boolean aktivan = true;

    public Obrada_Klijentskih_Zahteva(Socket soket) {
        this.soket = soket;
    }

    @Override
    public void run() {
        try {
            while (aktivan && soket != null && !soket.isClosed()) {

                Klijentski_Zahtev kz = primiZahtev();

                // Ako je null (klijent otišao ili greška u čitanju), prekidaj
                if (kz == null) {
                    System.out.println("Klijent se odvojio.");
                    break;
                }
                Serverski_Odgovor so = new Serverski_Odgovor();
                try {
                    switch (kz.getOperacija()) {

                        case Operacije.PRIJAVA:
                            if (kz.getParametar() instanceof CVM) {
                                try {
                                    CVM cvm = (CVM) kz.getParametar();
                                    CVM prijavljen = Controller.getInstance().login(cvm.getEmail(), cvm.getLozinka());
                                    if (prijavljen != null) {
                                        so.setOdgovor(prijavljen); // šalje ceo CVM nazad
                                    } else {
                                        so.setOdgovor("NEUSPEH!");
                                    }
                                } catch (Exception ex) {
                                    Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;

                        case Operacije.REGISTRACIJA:
                            if (kz.getParametar() instanceof CVM) {
                                CVM cvm = (CVM) kz.getParametar();

                                try {
                                    //Provera da li već postoji korisnik sa tim e-mailom (фрешх додато)
                                    boolean postoji = Controller.getInstance().postojiCVMEmail(cvm.getEmail());
                                    if (postoji) {
                                        so.setOdgovor("EMAIL_POSTOJI");
                                        break;
                                    }

                                    //Ako ne postoji — nastavi sa registracijom
                                    String privremenaLozinka = Pomocne.generateTemporaryPassword();
                                    cvm.setLozinka(privremenaLozinka);
                                    boolean registrovan = Controller.getInstance().dodaj_CVM(cvm);

                                    if (registrovan) {
                                        Pomocne.posalji_Email(cvm.getEmail(), privremenaLozinka);
                                        so.setOdgovor("USPEH!");
                                    } else {
                                        so.setOdgovor("NEUSPEH!");
                                    }

                                } catch (Exception ex) {
                                    Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
                                    so.setOdgovor("GRESKA!");
                                }
                            } else {
                                so.setOdgovor("NEUSPEH!");
                            }
                            break;

//                        case Operacije.REGISTRACIJA:
//                            if (kz.getParametar() instanceof CVM) {
//                                CVM cvm = (CVM) kz.getParametar();
//                                String privremenaLozinka = Pomocne.generateTemporaryPassword(); // ✅ generiše samo server
//                                cvm.setLozinka(privremenaLozinka);
//                                boolean registrovan = Controller.getInstance().dodaj_CVM(cvm);
//                                if (registrovan) {
//                                    Pomocne.posalji_Email(cvm.getEmail(), privremenaLozinka);
//                                    so.setOdgovor("USPEH!");
//                                } else {
//                                    so.setOdgovor("NEUSPEH!");
//                                }
//                            } else {
//                                so.setOdgovor("NEUSPEH!");
//                            }
//                            break;
                        case Operacije.VRATI_CVM_DETALJE:
                            try {
                                CVM zahtevani = (CVM) kz.getParametar();
                                CVM detalji = Controller.getInstance().vratiCVMdetalje(zahtevani);

                                if (detalji != null) {
                                    so.setOdgovor(detalji);
                                } else {
                                    so.setOdgovor("NEUSPEH!");
                                }
                            } catch (Exception e) {
                                Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, e);
                                so.setOdgovor("GRESKA!");
                            }
                            break;

                        case Operacije.VRATI_SVE_CVM:
                            List<CVM> sviCVM = Controller.getInstance().vrati_sve_CVM();
                            so.setOdgovor(sviCVM != null ? sviCVM : "NEUSPEH!");
                            break;

                        case Operacije.VRATI_SVE_PODATKE_CVM_VRSTA:
                            try {
                                int idCVM = (int) kz.getParametar();
                                List<VcvmCVM> sviPodaci = Controller.getInstance().vratiSvePodatkeCVMVrsta(idCVM);
                                so.setOdgovor(sviPodaci != null ? sviPodaci : "NEUSPEH!");
                            } catch (Exception e) {
                                Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, e);
                                so.setOdgovor("NEUSPEH!");
                            }
                            break;

                        case Operacije.AZURIRAJ_CVM:
                            CVM cvm = (CVM) kz.getParametar();
                            try {
                                boolean uspesno = Controller.getInstance().azurirajCVM(cvm);
                                so.setOdgovor(uspesno ? "USPEH!" : "NEUSPEH!");
                            } catch (Exception ex) {
                                Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
                                so.setOdgovor("GRESKA!");
                            }
                            break;

                        case Operacije.AZURIRAJ_VEZU:
                            VcvmCVM veza = (VcvmCVM) kz.getParametar();
                            try {
                                boolean uspesno = Controller.getInstance().azurirajVezu(veza);
                                so.setOdgovor(uspesno ? "USPEH!" : "NEUSPEH!");
                            } catch (Exception ex) {
                                Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
                                so.setOdgovor("GRESKA!");
                            }
                            break;

                        case Operacije.VRATI_SVE_VRSTE_CVM:
                            try {
                                List<VrstaCVM> sveVrste = Controller.getInstance().vratiSveVrsteCVM();
                                so.setOdgovor(sveVrste != null ? sveVrste : "NEUSPEH!");
                            } catch (Exception e) {
                                so.setOdgovor("NEUSPEH!");
                            }
                            break;

/////////////////////////////////////////////////////////////////////////////////////////////////////
                        case Operacije.VRATI_SVE_AMK:
                            try {
                                List<AMKlub> sviAMK = Controller.getInstance().vrati_sve_AMK();
                                so.setOdgovor(sviAMK != null && !sviAMK.isEmpty() ? sviAMK : "NEUSPEH!");
                            } catch (Exception e) {
                                so.setOdgovor("NEUSPEH!");
                            }
                            break;

                        case Operacije.VRATI_SVA_MESTA:
                            try {
                                List<Mesto> mesta = Controller.getInstance().vrati_mesta();
                                so.setOdgovor(mesta != null ? mesta : "NEUSPEH!");
                            } catch (Exception e) {
                                so.setOdgovor("NEUSPEH!");
                            }
                            break;

                        case Operacije.DODAJ_AMK:
                            try {
                                if (kz.getParametar() instanceof AMKlub) {
                                    AMKlub amk = (AMKlub) kz.getParametar();
                                    boolean dodat = Controller.getInstance().dodaj_amk(amk);
                                    so.setOdgovor(dodat ? "USPEH!" : "NEUSPEH!");
                                } else {
                                    so.setOdgovor("NEUSPEH!");
                                }
                            } catch (Exception e) {
                                Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, e);
                                so.setOdgovor("NEUSPEH!");
                            }
                            break;

                        case Operacije.VRATI_NAJVECI_ID_AMK:
                            try {
                                int najveciId = Controller.getInstance().vratiNajveciIdAMK();
                                so.setOdgovor(najveciId);
                            } catch (Exception e) {
                                so.setOdgovor(0);
                            }
                            break;

                        case Operacije.OBRISI_AMK:
                            if (kz.getParametar() instanceof Integer) {
                                int idAMK = (Integer) kz.getParametar();
                                boolean obrisan = Controller.getInstance().obrisi_amk(idAMK);

                                if (obrisan) {
                                    so.setOdgovor("USPEH!");
                                } else {
                                    so.setOdgovor("NEPOSTOJI!"); // nije pogođen nijedan red
                                }
                            } else {
                                so.setOdgovor("NEUSPEH!");
                            }
                            break;

                        case Operacije.AZURIRAJ_AMK:
                            if (kz.getParametar() instanceof AMKlub) {
                                AMKlub amk = (AMKlub) kz.getParametar();
                                boolean azuriran = Controller.getInstance().azuriraj_amk(amk);

                                if (!azuriran) {
                                    so.setOdgovor("NEPOSTOJI!");  // poseban status za slučaj kada AMK više nije u bazi
                                } else {
                                    so.setOdgovor("USPEH!");
                                }
                            } else {
                                so.setOdgovor("NEUSPEH!");
                            }
                            break;

                        case Operacije.VRATI_SVE_TAKMICARE:
                            List<Takmicar> sviTakmicari;
                            try {
                                sviTakmicari = Controller.getInstance().vratiSveTakmicare();
                                so.setOdgovor(sviTakmicari != null ? sviTakmicari : "NEUSPEH!");
                                break;
                            } catch (Exception ex) {
                                Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        case Operacije.VRATI_SVE_KATEGORIJE:
                            List<Kategorija> sveKategorije;
                            try {
                                sveKategorije = Controller.getInstance().vrati_SVE_kategorije();
                                so.setOdgovor(sveKategorije != null ? sveKategorije : "NEUSPEH!");
                                break;
                            } catch (Exception ex) {
                                Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        case Operacije.DODAJ_TAKMICARA:
                            if (kz.getParametar() instanceof Takmicar) {
                                Takmicar noviTak = (Takmicar) kz.getParametar();
                                boolean dodat;
                                try {
                                    dodat = Controller.getInstance().dodaj_takmicara(noviTak);
                                    so.setOdgovor(dodat ? "USPEH!" : "NEUSPEH!");
                                } catch (Exception ex) {
                                    Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } else {
                                so.setOdgovor("NEUSPEH!");
                            }
                            break;

                        case Operacije.VRATI_NAJVECI_ID_TAKMICARA:
                            int maxId;
                            try {
                                maxId = Controller.getInstance().vratiNajveciIdTakmicara();
                                so.setOdgovor(maxId);
                            } catch (Exception ex) {
                                Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            break;

                        case Operacije.OBRISI_TAKMICARA:
                            if (kz.getParametar() instanceof Takmicar) {
                                Takmicar tak = (Takmicar) kz.getParametar();
                                try {
                                    boolean obrisan = Controller.getInstance().obrisi_takmicara(tak);
                                    so.setOdgovor(obrisan ? "USPEH!" : "NEUSPEH!");
                                } catch (Exception ex) {
                                    Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } else {
                                so.setOdgovor("NEUSPEH!");
                            }
                            break;

                        case Operacije.AZURIRAJ_TAKMICARA:
                            if (kz.getParametar() instanceof Takmicar) {
                                Takmicar tak = (Takmicar) kz.getParametar();
                                try {
                                    boolean azuriran = Controller.getInstance().azuriraj_takmicara(tak);
                                    so.setOdgovor(azuriran ? "USPEH!" : "NEUSPEH!");
                                } catch (Exception ex) {
                                    Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } else {
                                so.setOdgovor("NEUSPEH!");
                            }
                            break;

                        case Operacije.VRATI_SVA_TAKMICENJA: {
                            try {
                                List<Takmicenje> svaTakmicenja = Controller.getInstance().vrati_sva_takmicenja();
                                so.setOdgovor(svaTakmicenja != null ? svaTakmicenja : "NEUSPEH!");
                            } catch (Exception ex) {
                                Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        break;

                        case Operacije.VRATI_PRIJAVLJENE_TAKMICARE:
                            try {
                                Takmicenje tak = (Takmicenje) kz.getParametar();
                                List<Object[]> prijavljeni = Controller.getInstance().vratiPrijavljeneTakmicare(tak);
                                so.setOdgovor(prijavljeni != null ? prijavljeni : "GRESKA!");
                            } catch (Exception e) {
                                e.printStackTrace();
                                so.setOdgovor("GRESKA!");
                            }
                            break;

                        case Operacije.VRATI_TAKMICARE_IZ_KATEGORIJE:
                            try {
                                Kategorija kat = (Kategorija) kz.getParametar();
                                List<Object[]> takmicari = Controller.getInstance().vratiTakmicareIzKategorije(kat);
                                so.setOdgovor(takmicari != null ? takmicari : "GRESKA!");
                            } catch (Exception e) {
                                e.printStackTrace();
                                so.setOdgovor("GRESKA!");
                            }
                            break;

                        case Operacije.AZURIRAJ_TAKMICENJE:
                            try {
                                Object[] podaci = (Object[]) kz.getParametar();
                                Takmicenje tak = (Takmicenje) podaci[0];
                                List<Takmicar> prijavljeni = (List<Takmicar>) podaci[1];

                                boolean uspesno = Controller.getInstance().azurirajTakmicenjeSaPrijavama(tak, prijavljeni);

                                so.setOdgovor(uspesno ? "USPEH" : "GRESKA");
                            } catch (Exception e) {
                                e.printStackTrace();
                                so.setOdgovor("GRESKA");
                            }
                            break;

                        case Operacije.OBRISI_TAKMICENJE:
                            try {
                                Takmicenje tak = (Takmicenje) kz.getParametar();
                                boolean uspesno = Controller.getInstance().obrisiTakmicenje(tak);
                                so.setOdgovor(uspesno ? "USPEH" : "GRESKA");
                            } catch (Exception e) {
                                e.printStackTrace();
                                so.setOdgovor("GRESKA");
                            }
                            break;

                        case Operacije.KREIRAJ_TAKMICENJE:
                            try {
                                Object[] podaci = (Object[]) kz.getParametar();
                                Takmicenje novoTakmicenje = (Takmicenje) podaci[0];
                                List<Takmicar> prijavljeni = (List<Takmicar>) podaci[1];

                                boolean uspesno = Controller.getInstance().kreirajTakmicenje(novoTakmicenje, prijavljeni);

                                if (uspesno) {
                                    so.setOdgovor("USPEH");
                                } else {
                                    so.setOdgovor("GRESKA");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("Veza sa bazom je izgubljena!");
                                so.setOdgovor("GRESKA_BAZA");
                            }
                            break;

                        //////////////////////////////////////////////////////
                        //
                        // STARI KOD
                        //
                        /////////////////////////////////////////////////////

                        /*          
                    
                    //                case Operacije.OBRISI_AMK:
//                    if (kz.getParametar() instanceof Integer) {
//                        int idAMK = (Integer) kz.getParametar();
//                        boolean obrisan = Controller.getInstance().obrisi_amk(idAMK);
//                        so.setOdgovor(obrisan ? "USPEH!" : "NEUSPEH!");
//                    } else {
//                        so.setOdgovor("NEUSPEH!");
//                    }
//                    break;
//                case Operacije.AZURIRAJ_AMK:
//                    if (kz.getParametar() instanceof AMKlub) {
//                        AMKlub amk = (AMKlub) kz.getParametar();
//                        boolean azuriran = Controller.getInstance().azuriraj_amk(amk);
//                        so.setOdgovor(azuriran ? "USPEH!" : "NEUSPEH!");
//                    } else {
//                        so.setOdgovor("NEUSPEH!");
//                    }
//                    break;
                    //                case Operacije.AZURIRAJ_TAKMICENJE:
//                    try {
//                        Object[] podaci = (Object[]) kz.getParametar();
//                        Takmicenje t = (Takmicenje) podaci[0];
//                        List<Takmicar> prijavljeni = (List<Takmicar>) podaci[1];
//
//                        boolean ok = Controller.getInstance().azuriraj_takmicenje(t);
//                        if (ok) {
//                            Controller.getInstance().azuriraj_prijave(t, prijavljeni);
//                            so.setOdgovor("USPEH");
//                        } else {
//                            so.setOdgovor("GRESKA");
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        so.setOdgovor("GRESKA");
//                    }
//                    break;
                    
                    //                case Operacije.OBRISI_AMK:
//                    if (kz.getParametar() instanceof Integer) {
//                        int idAMK = (Integer) kz.getParametar();
//                        boolean obrisan = Controller.getInstance().obrisi_amk(idAMK);
//                        so.setOdgovor(obrisan ? "USPEH!" : "NEUSPEH!");
//                    } else {
//                        so.setOdgovor("NEUSPEH!");
//                    }
//                    break;
//                case Operacije.AZURIRAJ_AMK:
//                    if (kz.getParametar() instanceof AMKlub) {
//                        AMKlub amk = (AMKlub) kz.getParametar();
//                        boolean azuriran = Controller.getInstance().azuriraj_amk(amk);
//                        so.setOdgovor(azuriran ? "USPEH!" : "NEUSPEH!");
//                    } else {
//                        so.setOdgovor("NEUSPEH!");
//                    }
//                    break;
                    
                    //                case Operacije.OBRISI_AMK:
//                    if (kz.getParametar() instanceof Integer) {
//                        int idAMK = (Integer) kz.getParametar();
//                        boolean obrisan = Controller.getInstance().obrisi_amk(idAMK);
//                        so.setOdgovor(obrisan ? "USPEH!" : "NEUSPEH!");
//                    } else {
//                        so.setOdgovor("NEUSPEH!");
//                    }
//                    break;
//                case Operacije.AZURIRAJ_AMK:
//                    if (kz.getParametar() instanceof AMKlub) {
//                        AMKlub amk = (AMKlub) kz.getParametar();
//                        boolean azuriran = Controller.getInstance().azuriraj_amk(amk);
//                        so.setOdgovor(azuriran ? "USPEH!" : "NEUSPEH!");
//                    } else {
//                        so.setOdgovor("NEUSPEH!");
//                    }
//                    break;
                    
                    //                case Operacije.AZURIRAJ_CVM:
//                    try {
//                        CVM cvm = (CVM) kz.getParametar();
//                        boolean uspesno = Controller.getInstance().azurirajCVM(cvm);
//                        so.setOdgovor(uspesno ? "USPEH!" : "NEUSPEH!");
//                    } catch (Exception e) {
//                        Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, e);
//                        so.setOdgovor("NEUSPEH!");
//                    }
//                    break;
//
//                case Operacije.AZURIRAJ_VEZU:
//                    try {
//                        VcvmCVM veza = (VcvmCVM) kz.getParametar();
//                        boolean uspesno = Controller.getInstance().azurirajVezu(veza);
//                        so.setOdgovor(uspesno ? "USPEH!" : "NEUSPEH!");
//                    } catch (Exception e) {
//                        Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, e);
//                        so.setOdgovor("NEUSPEH!");
//                    }
//                    break;
                    
                    //                case Operacije.POPUNI_CB_DRZAVE:
//                    try {
//                        List<VrstaCVM> sveDrzave = Controller.getInstance().getAllDrzave();
//                        so.setOdgovor(sveDrzave != null ? sveDrzave : "NEUSPEH!");
//                    } catch (Exception e) {
//                        so.setOdgovor("NEUSPEH!");
//                    }
//                    break;

                case Operacije.VRATI_SVE_POTREBNE_INFO_ZA_TAKMICARA_KAT_I_AMK:
                    List<Object[]> sviPodaci = Controller.getInstance().vrati_SVE_potrebne_info_tak_kat_amk();
                    so.setOdgovor(sviPodaci != null ? sviPodaci : "NEUSPEH!");
                    break;

                case Operacije.VRATI_SVE_KATEGORIJE:
                    List<Kategorija> sveKategorije = Controller.getInstance().vrati_SVE_kategorije();
                    if (sveKategorije != null && !sveKategorije.isEmpty()) {
                        so.setOdgovor(sveKategorije);
                    } else {
                        so.setOdgovor("NEUSPEH!"); // Vraća poruku u slučaju greške ili prazne liste
                    }
                    break;

                case Operacije.VRATI_SVE_AMK:
                    List<AMKlub> sviAMK = Controller.getInstance().vrati_sve_AMK();
                    if (sviAMK != null && !sviAMK.isEmpty()) {
                        so.setOdgovor(sviAMK);
                    } else {
                        so.setOdgovor("NEUSPEH!");
                    }
                    break;

                case Operacije.AZURIRAJ_TAKMICARA:
                    if (kz.getParametar() instanceof Takmicar) {
                        Takmicar tak = (Takmicar) kz.getParametar();
                        boolean azuriran = Controller.getInstance().azuriraj_takmicare(tak);
                        so.setOdgovor(azuriran ? "USPEH!" : "NEUSPEH!");
                    } else {
                        so.setOdgovor("NEUSPEH!");
                    }
                    break;

                case Operacije.DODAJ_TAKMICARA:
                    if (kz.getParametar() instanceof Takmicar) {
                        Takmicar tak = (Takmicar) kz.getParametar();
                        boolean azuriran = Controller.getInstance().dodaj_takmicara(tak);
                        so.setOdgovor(azuriran ? "USPEH!" : "NEUSPEH!");
                    } else {
                        so.setOdgovor("NEUSPEH!");
                    }
                    break;

                case Operacije.VRATI_NAJVECI_ID_TAKMICARA:
                    int najveciId = Controller.getInstance().vratiNajveciIdTakmicara();
                    so.setOdgovor(najveciId);
                    break;

                case Operacije.VRATI_NAJVECI_ID_AMK:
                    int najveci_Id = Controller.getInstance().vratiNajveciIdAMK();
                    so.setOdgovor(najveci_Id);
                    break;

                case Operacije.OBRISI_TAKMICARA:
                    if (kz.getParametar() instanceof Integer) { // Očekuješ ID kao Integer
                        int idTakmicar = (Integer) kz.getParametar();
                        boolean obrisan = Controller.getInstance().obrisi_takmicara(idTakmicar);
                        so.setOdgovor(obrisan ? "USPEH!" : "NEUSPEH!");
                    } else {
                        so.setOdgovor("NEUSPEH!");
                    }
                    break;
                         */
                        case Operacije.VRATI_SVE_INFO_AMK:
                            try {
                                List<Object[]> sviInfo = Controller.getInstance().vratiSveInfoAMK();
                                so.setOdgovor(sviInfo != null ? sviInfo : "NEUSPEH!");
                            } catch (Exception e) {
                                Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, e);
                                so.setOdgovor("NEUSPEH!");
                            }
                            break;

                        /*
                case Operacije.VRATI_SVA_MESTA:
                    List<Mesto> mesta = Controller.getInstance().vrati_mesta();
                    so.setOdgovor(mesta != null ? mesta : "NEUSPEH!");
                    break;

                case Operacije.DODAJ_AMK:
                    if (kz.getParametar() instanceof AMKlub) {
                        AMKlub amk = (AMKlub) kz.getParametar();
                        boolean azuriran = Controller.getInstance().dodaj_amk(amk);
                        so.setOdgovor(azuriran ? "USPEH!" : "NEUSPEH!");
                    } else {
                        so.setOdgovor("NEUSPEH!");
                    }
                    break;

                case Operacije.AZURIRAJ_AMK:
                    if (kz.getParametar() instanceof AMKlub) {
                        AMKlub amk = (AMKlub) kz.getParametar();
                        boolean azuriran = Controller.getInstance().azuriraj_amk(amk);
                        so.setOdgovor(azuriran ? "USPEH!" : "NEUSPEH!");
                    } else {
                        so.setOdgovor("NEUSPEH!");
                    }
                    break;

                case Operacije.OBRISI_AMK:
                    if (kz.getParametar() instanceof Integer) {
                        int idAMK = (Integer) kz.getParametar();
                        boolean obrisan = Controller.getInstance().obrisi_amk(idAMK);
                        so.setOdgovor(obrisan ? "USPEH!" : "NEUSPEH!");
                    } else {
                        so.setOdgovor("NEUSPEH!");
                    }
                    break;
                         */
                        default:
                            System.out.println("GRESKA!");
                            so.setOdgovor("GRESKA!");
                    }

                } catch (Exception ex) {
                    if (ex instanceof java.sql.SQLNonTransientConnectionException) {
                        System.out.println("⚠️ Veza sa bazom izgubljena!");
                        so.setOdgovor("GRESKA_BAZA");
                    } else {
                        Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName())
                                .log(Level.SEVERE, null, ex);
                        so.setOdgovor("GRESKA!");
                    }
                }

                posaljiOdgovor(so);
            }

        } catch (Exception e) {
            if (!(e instanceof SocketException)) {
                e.printStackTrace();
            }
        } finally {
            zatvoriKonekciju();
        }
    }

    private Klijentski_Zahtev primiZahtev() {
        try {
            ObjectInputStream ois = new ObjectInputStream(soket.getInputStream());
            return (Klijentski_Zahtev) ois.readObject();

        } catch (EOFException | SocketException e) {
            // klijent se odvojio — nema potrebe da prijavljujemo grešku
            aktivan = false;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void posaljiOdgovor(Serverski_Odgovor so) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(soket.getOutputStream());
            oos.writeObject(so);
            oos.flush();
        } catch (SocketException e) {
            // klijent se odvojio — ne prijavljuj
            aktivan = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void zatvoriKonekciju() {
        try {
            if (soket != null && !soket.isClosed()) {
                soket.close();
            }
        } catch (IOException ignored) {
        }
    }
}

//        }
//    }
//
//    private Klijentski_Zahtev primiZahtev() {
//
//        try {
//            ObjectInputStream ois = new ObjectInputStream(soket.getInputStream());
//            return (Klijentski_Zahtev) ois.readObject();
//
//        } catch (IOException ex) {
//            Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    private void posaljiOdgovor(Serverski_Odgovor so) {
//
//        try {
//            ObjectOutputStream oos = new ObjectOutputStream(soket.getOutputStream());
//            oos.writeObject(so);
//            oos.flush();
//
//        } catch (IOException ex) {
//            Logger.getLogger(Obrada_Klijentskih_Zahteva.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }

