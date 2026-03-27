/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package forme;

import controlerUI.ThemeManager;
import controlerUI.ValidatorUI;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import komunikacija.Komunikacija;
import model.AMKlub;
import model.CVM;
import model.Kategorija;
import model.Takmicar;
import model.Takmicenje;
import operacije.Operacije;
import transfer.Klijentski_Zahtev;
import transfer.Serverski_Odgovor;

/**
 *
 * @author sreck
 */
public class Forma_Takmicenje_Detalji extends javax.swing.JFrame {

    private CVM prijavljen;
    private Takmicenje takmicenje;
    private List<Takmicar> listaPrijavljeni = new ArrayList<>();
    private List<AMKlub> listaPrijavljeniAMK = new ArrayList<>();
    private List<Takmicar> listaDostupni = new ArrayList<>();
    private List<AMKlub> listaDostupniAMK = new ArrayList<>();
    private List<Kategorija> sveKategorijeCB = new ArrayList<>();

    Forma_TAKMICENJE parent;

    /**
     * Creates new form Forma_Takmicenje_Detalji
     */
    public Forma_Takmicenje_Detalji(Takmicenje takmicenje, CVM prijavljen, Forma_TAKMICENJE parent) {
        this.takmicenje = takmicenje;
        this.prijavljen = prijavljen;
        this.parent = parent;

        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // jTable_Dostupni_Takmicari.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        ThemeManager.applyTheme(this);

        boolean isProslo = false;

        // Ako takmicenje nije null, proveri datum
        if (takmicenje != null && takmicenje.getDatum() != null) {
            isProslo = takmicenje.getDatum().before(new Date());
        }

        //  pozovi validaciju samo ako nije proslo takmixenje
        frontendProvere(isProslo);

        //  1. Prikaz organizatora odma po otvaranju forme
        if (takmicenje == null && prijavljen != null) {
            jLabel_Organizator.setText(prijavljen.getNazivCVM());
        }

        //  2. Popuni kategorije
        popuniCB();
        
// Postojee takmicenje popuni polja i prikai podatke
        if (takmicenje != null) {
            
            popuniPolja();
            prikaziPrijavljene();
            prikaziDostupne();

            jButton_Kreiraj_Takmicenje.setVisible(false);

            // Listener za promenu kategorije (osveži prikaz, ali BEZ potvrde)
            jComboBox_Kategorija.addActionListener(e -> azurirajPodatkeKategorije());

            // Onemoguci menjanje kategorije za postojece takmicenje
            if (takmicenje.getIdTakmicenje() > 0) {
                jComboBox_Kategorija.setEnabled(false);
            }

        } else {
            // Novo takmicenje  oa
            jButton_Kreiraj_Takmicenje.setVisible(true);
            jButton_Azuriraj_Takmicenje.setVisible(false);
            jButton_Obrisi_Takmicenje.setVisible(false);

            // Dozvoliti izbor kategorije
            jComboBox_Kategorija.setEnabled(true);

            // 3, Popuni polja za novo takmičenje (poziva listener sa potvrdom)
            popuniPolja();
        }

//        initComponents();
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        jTable_Dostupni_Takmicari.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//
//        popuniCB();               // prvo popuni sve kategorije
//
//        // 🔹 Dodaj listener da se dostupni takmičari prikazuju kad se promeni kategorija
//        jComboBox_Kategorija.addActionListener(e -> prikaziDostupne());
//
//        // ✅ 1. Prikaz organizatora odmah po otvaranju forme
//        if (takmicenje == null && prijavljen != null) {
//            jLabel_Organizator.setText(prijavljen.getNazivCVM());
//        }
//
//        if (takmicenje != null) {
//            // Postojeće takmičenje → popuni polja i prikaži podatke
//            popuniPolja();
//            prikaziPrijavljene();
//            prikaziDostupne();
//
//            jButton_Kreiraj_Takmicenje.setVisible(false);
//
//            // Listener za promenu kategorije
//            jComboBox_Kategorija.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    azurirajPodatkeKategorije();
//                }
//            });
//
//            // Onemogući menjanje kategorije za postojeće takmičenje
//            if (takmicenje.getIdTakmicenje() > 0) {
//                jComboBox_Kategorija.setEnabled(false);
//            }
//
//        } else {
//            // Novo takmičenje → ostavi prazna polja
//            jButton_Kreiraj_Takmicenje.setVisible(true);
//            jButton_Azuriraj_Takmicenje.setVisible(false);
//            jButton_Obrisi_Takmicenje.setVisible(false);
//
//            // Dozvoli izbor kategorije
//            jComboBox_Kategorija.setEnabled(true);
//        }
//
//        if (takmicenje == null) {
//            jLabel_Organizator.setText(prijavljen.getNazivCVM());
//        }
    }

    private void popuniPolja() {

        //  NOVO takmicenje
        if (takmicenje == null) {

            //  Isprazni polja
            jTextField_Nazi_Takmicenja.setText("");
            jTextField_Datum_Odrzavanja.setText("");
            jTextField_Lokacija.setText("");

            // da lepo postavimo na pocetku
            jButton_Kreiraj_Takmicenje.setVisible(true);
            jButton_Azuriraj_Takmicenje.setVisible(false);
            jButton_Obrisi_Takmicenje.setVisible(false);
            jButton_Prijavi_Takmicara.setVisible(true);
            jButton_Odjavi_Takmicara.setVisible(true);

            jComboBox_Kategorija.setEnabled(true);
            jTable_Dostupni_Takmicari.setVisible(true);

            //   stari listeneri cao, da se ne dupliraju
            for (ActionListener al : jComboBox_Kategorija.getActionListeners()) {
                jComboBox_Kategorija.removeActionListener(al);
            }

            // novi listener za potvrdu kategorije, jednostavniji nacin za ne tako jednostavan problem [zivotna primena 0, spas za sad +1000000]
            jComboBox_Kategorija.addActionListener(e -> {
                if (jComboBox_Kategorija.isEnabled() && jComboBox_Kategorija.getSelectedItem() != null) {
                    String nazivKat = jComboBox_Kategorija.getSelectedItem().toString();

                    int potvrda = JOptionPane.showConfirmDialog(
                            this,
                            "Da li ste sigurni da želite kategoriju \"" + nazivKat + "\"?\n"
                            + "Nakon izbora, nećete moći da je promenite.",
                            "Potvrda kategorije",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (potvrda == JOptionPane.YES_OPTION) {
                        jComboBox_Kategorija.setEnabled(false);

                        try {
                            //  potraga odabranue kategorije u bazi
                            Klijentski_Zahtev kzSveKat = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
                            Komunikacija.getInstance().posalji_zahtev(kzSveKat);
                            Serverski_Odgovor soSveKat = Komunikacija.getInstance().primi_odgovor();

                            if (soSveKat != null && soSveKat.getOdgovor() instanceof List) {
                                List<Kategorija> sveKat = (List<Kategorija>) soSveKat.getOdgovor();

                                for (Kategorija k : sveKat) {
                                    if (k.getNazivKategorija().equals(nazivKat)) {
                                        // Kreiranje privremeno takmicenje radi prikaza dostupnih
                                        this.takmicenje = new Takmicenje();
                                        this.takmicenje.setKat(k);
                                        this.takmicenje.setCvm(prijavljen); // odmah vezujmo organizatora
                                        prikaziDostupne();
                                        break;
                                    }
                                }
                            }

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Greška pri učitavanju kategorije: " + ex.getMessage());
                        }
                    }
                }
            });

            return; // kraj za novo takmienje
        }

        //  Ako se radi o POSTOJECEM takmicenju
        jLabel_Organizator.setText(takmicenje.getCvm().getNazivCVM());
        jTextField_Nazi_Takmicenja.setText(takmicenje.getNazivTakmicenja());
        jTextField_Datum_Odrzavanja.setText(new SimpleDateFormat("dd. MM. yyyy.").format(takmicenje.getDatum()));
        jTextField_Lokacija.setText(takmicenje.getLokacija());

        //jComboBox_Kategorija.addItem(takmicenje.getKat().getNazivKategorija());
        jComboBox_Kategorija.removeAllItems();
        jComboBox_Kategorija.addItem(takmicenje.getKat().getNazivKategorija());
        jComboBox_Kategorija.setSelectedIndex(0);
        jComboBox_Kategorija.setToolTipText("Kategoriju nije moguće menjati za već kreirano takmičenje.");

        // Ako je takmicenje proclo ili nije organizator  mora se zabrani izmenejivanje
        //
        if (takmicenje.getDatum().before(new Date())
                || prijavljen == null
                || prijavljen.getIdCVM() != takmicenje.getCvm().getIdCVM()) {

            jLabel8.setVisible(false);
            jLabel9.setVisible(false);
            jLabel_Ukupno_Dostupnih.setVisible(false);
            lblErrNaziv.setVisible(false);
            lblErrDatum.setVisible(false);
            lblErrLokacija.setVisible(false);

            jTable_Dostupni_Takmicari.setVisible(false);
            jScrollPane2.setVisible(false); // ZNAO SAM DA IMA NEKA FORA da nije samo ovo iznad, koji sam doktor hahahahhahaaha

            jTextField_Nazi_Takmicenja.setEditable(false);
            jTextField_Datum_Odrzavanja.setEditable(false);
            jTextField_Lokacija.setEditable(false);
            jComboBox_Kategorija.setEnabled(false);

            //jComboBox_Kategorija.setEnabled(true);
            //jComboBox_Kategorija.setEditable(false);
            //jComboBox_Kategorija.setBackground(new Color(240,240,240));
            jButton_Kreiraj_Takmicenje.setVisible(false);
            jButton_Azuriraj_Takmicenje.setVisible(false);
            jButton_Obrisi_Takmicenje.setVisible(false);
            jButton_Prijavi_Takmicara.setVisible(false);
            jButton_Odjavi_Takmicara.setVisible(false);

            jComboBox_Kategorija.setToolTipText("Kategoriju može menjati samo organizator ovog takmičenja.");
            jTextField_Nazi_Takmicenja.setToolTipText("Detalje može menjati samo organizator ovog takmičenja.");
            jTextField_Datum_Odrzavanja.setToolTipText("Detalje može menjati samo organizator ovog takmičenja.");
            jTextField_Lokacija.setToolTipText("Detalje može menjati samo organizator ovog takmičenja.");

            if (takmicenje.getDatum().before(new Date())) {
                jTextField_Nazi_Takmicenja.setToolTipText("Detalje nije moguće menjati za već održano takmičenje.");
                jTextField_Datum_Odrzavanja.setToolTipText("Detalje nije moguće menjati za već održano takmičenje.");
                jTextField_Lokacija.setToolTipText("Detalje nije moguće menjati za već održano takmičenje.");
                jComboBox_Kategorija.setToolTipText("Kategoriju nije moguće menjati za već održano takmičenje.");
            }
        }
    }

//    private void popuniPolja() {
//
//        // 🔹 Ako je NOVO takmičenje (kreiranje)
//        if (takmicenje == null) {
//            // Organizator je trenutno prijavljeni CVM
//            if (prijavljen != null) {
//                jLabel_Organizator.setText(prijavljen.getNazivCVM());
//            } else {
//                jLabel_Organizator.setText("Nepoznat organizator");
//            }
//
//            // Prazna polja
//            jTextField_Nazi_Takmicenja.setText("");
//            jTextField_Datum_Odrzavanja.setText("");
//            jTextField_Lokacija.setText("");
//
//            // Dugmad i kontrole
//            jButton_Kreiraj_Takmicenje.setVisible(true);
//            jButton_Azuriraj_Takmicenje.setVisible(false);
//            jButton_Obrisi_Takmicenje.setVisible(false);
//
//            jComboBox_Kategorija.setEnabled(true);
//            jTable_Dostupni_Takmicari.setVisible(true);
//
//            // Kada korisnik izabere kategoriju – pita za potvrdu i prikaže takmičare
//            jComboBox_Kategorija.addActionListener(e -> {
//                if (jComboBox_Kategorija.getSelectedItem() != null && jComboBox_Kategorija.isEnabled()) {
//                    int potvrda = JOptionPane.showConfirmDialog(this,
//                            "Da li ste sigurni da želite ovu kategoriju? Nakon izbora ne možete je menjati.",
//                            "Potvrda kategorije", JOptionPane.YES_NO_OPTION);
//
//                    if (potvrda == JOptionPane.YES_OPTION) {
//                        jComboBox_Kategorija.setEnabled(false);
//
//                        // 🔹 Popuni dostupne takmičare za tu kategoriju
//                        String nazivKat = (String) jComboBox_Kategorija.getSelectedItem();
//                        if (nazivKat != null) {
//                            try {
//                                // Uzimamo sve kategorije da nađemo izabranu
//                                Klijentski_Zahtev kzSveKat = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
//                                Komunikacija.getInstance().posalji_zahtev(kzSveKat);
//                                Serverski_Odgovor soSveKat = Komunikacija.getInstance().primi_odgovor();
//
//                                if (soSveKat != null && soSveKat.getOdgovor() instanceof List) {
//                                    List<Kategorija> sveKat = (List<Kategorija>) soSveKat.getOdgovor();
//                                    for (Kategorija k : sveKat) {
//                                        if (k.getNazivKategorija().equals(nazivKat)) {
//                                            // Kreiramo privremeni objekat takmičenja (radi prikaza dostupnih)
//                                            Takmicenje novo = new Takmicenje();
//                                            novo.setKat(k);
//                                            this.takmicenje = novo;
//                                            prikaziDostupne(); // koristi postojeću metodu
//                                            break;
//                                        }
//                                    }
//                                }
//
//                            } catch (Exception ex) {
//                                JOptionPane.showMessageDialog(this,
//                                        "Greška pri učitavanju takmičara za kategoriju: " + ex.getMessage());
//                            }
//                        }
//                    }
//                }
//            });
//
//            return; // ⬅️ izlazak, jer novo takmičenje nema ništa da se popunjava dalje
//        }
//
//        // 🔹 Ako se radi o POSTOJEĆEM takmičenju
//        jLabel_Organizator.setText(takmicenje.getCvm().getNazivCVM());
//        jTextField_Nazi_Takmicenja.setText(takmicenje.getNazivTakmicenja());
//        jTextField_Datum_Odrzavanja.setText(new SimpleDateFormat("dd. MM. yyyy.").format(takmicenje.getDatum()));
//        jTextField_Lokacija.setText(takmicenje.getLokacija());
//        jComboBox_Kategorija.addItem(takmicenje.getKat().getNazivKategorija());
//
//        // Ako je takmičenje prošlo ili nije organizator → zabrani izmene
//        if (takmicenje.getDatum().before(new Date())
//                || prijavljen == null
//                || prijavljen.getIdCVM() != takmicenje.getCvm().getIdCVM()) {
//
//            jLabel8.setVisible(false);
//            jLabel9.setVisible(false);
//            jLabel_Ukupno_Dostupnih.setVisible(false);
//            jTable_Dostupni_Takmicari.setVisible(false);
//
//            jTextField_Nazi_Takmicenja.setEditable(false);
//            jTextField_Datum_Odrzavanja.setEditable(false);
//            jTextField_Lokacija.setEditable(false);
//            jComboBox_Kategorija.setEnabled(false);
//
//            jButton_Azuriraj_Takmicenje.setVisible(false);
//            jButton_Kreiraj_Takmicenje.setVisible(false);
//            jButton_Obrisi_Takmicenje.setVisible(false);
//            jButton_Prijavi_Takmicara.setVisible(false);
//            jButton_Odjavi_Takmicara.setVisible(false);
//        }
//    }
//    private void popuniPolja() {
//        if (takmicenje == null) {
//            // === Kreiranje novog takmičenja ===
//            jLabel_Organizator.setText(prijavljen.getNazivCVM()); // organizator je prijavljeni CVM
//            jTextField_Nazi_Takmicenja.setText("");
//            jTextField_Datum_Odrzavanja.setText("");
//            jTextField_Lokacija.setText("");
//
//            // Dugmad: samo kreiraj
//            jButton_Kreiraj_Takmicenje.setVisible(true);
//            jButton_Azuriraj_Takmicenje.setVisible(false);
//            jButton_Obrisi_Takmicenje.setVisible(false);
//
//            // Polja se mogu uređivati
//            jTextField_Nazi_Takmicenja.setEditable(true);
//            jTextField_Datum_Odrzavanja.setEditable(true);
//            jTextField_Lokacija.setEditable(true);
//            jComboBox_Kategorija.setEnabled(true);
//
//            // Listener da se potvrdi kategorija i zaključa nakon izbora
//            jComboBox_Kategorija.addActionListener(e -> {
//                if (jComboBox_Kategorija.getSelectedItem() != null && jComboBox_Kategorija.isEnabled()) {
//                    int potvrda = JOptionPane.showConfirmDialog(this,
//                            "Da li ste sigurni da želite ovu kategoriju? Posle izbora ne možete je promeniti.",
//                            "Potvrda kategorije", JOptionPane.YES_NO_OPTION);
//                    if (potvrda == JOptionPane.YES_OPTION) {
//                        jComboBox_Kategorija.setEnabled(false);
//                    }
//                }
//            });
//
//            return; // 👉 prekid ovde jer nema takmicenja
//        }
//
//        // === Izmena postojećeg takmičenja ===
//        jLabel_Organizator.setText(takmicenje.getCvm().getNazivCVM());
//        jTextField_Nazi_Takmicenja.setText(takmicenje.getNazivTakmicenja());
//        jTextField_Datum_Odrzavanja.setText(
//                new SimpleDateFormat("dd. MM. yyyy.").format(takmicenje.getDatum()));
//        jTextField_Lokacija.setText(takmicenje.getLokacija());
//        jComboBox_Kategorija.addItem(takmicenje.getKat().getNazivKategorija());
//        jComboBox_Kategorija.setEnabled(false); // ne može menjati kategoriju
//
//        // Dugmad: ažuriraj i obriši
//        jButton_Azuriraj_Takmicenje.setVisible(true);
//        jButton_Obrisi_Takmicenje.setVisible(true);
//        jButton_Kreiraj_Takmicenje.setVisible(false);
//
//        // Ako je prošlo ili ako nije organizator
//        if (takmicenje.getDatum().before(new Date())
//                || prijavljen == null
//                || prijavljen.getIdCVM() != takmicenje.getCvm().getIdCVM()) {
//
//            jTable_Dostupni_Takmicari.setVisible(false);
//
//            jTextField_Nazi_Takmicenja.setEditable(false);
//            jTextField_Datum_Odrzavanja.setEditable(false);
//            jTextField_Lokacija.setEditable(false);
//            jComboBox_Kategorija.setEnabled(false);
//
//            jButton_Azuriraj_Takmicenje.setVisible(false);
//            jButton_Obrisi_Takmicenje.setVisible(false);
//            jButton_Prijavi_Takmicara.setVisible(false);
//            jButton_Odjavi_Takmicara.setVisible(false);
//
//            jComboBox_Kategorija.setToolTipText("Kategoriju i detalje može menjati samo organizator ovog takmičenja.");
//            jTextField_Nazi_Takmicenja.setToolTipText("Detalje može menjati samo organizator ovog takmičenja.");
//            jTextField_Datum_Odrzavanja.setToolTipText("Detalje može menjati samo organizator ovog takmičenja.");
//            jTextField_Lokacija.setToolTipText("Detalje može menjati samo organizator ovog takmičenja.");
//
//            if (takmicenje.getDatum().before(new Date())) {
//                jTextField_Nazi_Takmicenja.setToolTipText("Detalje nije moguće menjati za već održano takmičenje.");
//                jTextField_Datum_Odrzavanja.setToolTipText("Detalje nije moguće menjati za već održano takmičenje.");
//                jTextField_Lokacija.setToolTipText("Detalje nije moguće menjati za već održano takmičenje.");
//            }
//        }
//    }
    /////////////////////
    //
    //
    //
    ////////////////////
//    private void popuniPolja() {
//        
//        
//        if (takmicenje == null) {
//            // Kreiranje novog takmičenja
//            jButton_Kreiraj_Takmicenje.setVisible(true);
//            jButton_Azuriraj_Takmicenje.setVisible(false);
//            jButton_Obrisi_Takmicenje.setVisible(false);
//
//            // Omogući kategoriju da se bira
//            jComboBox_Kategorija.setEnabled(true);
//
//            // Dodaj listener za zaključavanje
//            jComboBox_Kategorija.addActionListener(e -> {
//                if (jComboBox_Kategorija.getSelectedItem() != null && jComboBox_Kategorija.isEnabled()) {
//                    int potvrda = JOptionPane.showConfirmDialog(this,
//                            "Da li ste sigurni da želite ovu kategoriju? Posle izbora ne možete je promeniti.",
//                            "Potvrda kategorije", JOptionPane.YES_NO_OPTION);
//                    if (potvrda == JOptionPane.YES_OPTION) {
//                        jComboBox_Kategorija.setEnabled(false);
//                    }
//                }
//            });
//        }
//        
//        // Popunjavanje osnovnih podataka o takmičenju
//        jLabel_Organizator.setText(takmicenje.getCvm().getNazivCVM());
//        jTextField_Nazi_Takmicenja.setText(takmicenje.getNazivTakmicenja());
//        jTextField_Datum_Odrzavanja.setText(new SimpleDateFormat("dd. MM. yyyy.").format(takmicenje.getDatum()));
//        jTextField_Lokacija.setText(takmicenje.getLokacija());
//        jComboBox_Kategorija.addItem(takmicenje.getKat().getNazivKategorija());
//
//        // Ako je takmičenje prošlo ili ako prijavljeni nije organizator → zabrani izmene
//        if (takmicenje.getDatum().before(new Date())
//                || prijavljen == null
//                || prijavljen.getIdCVM() != takmicenje.getCvm().getIdCVM()) {
//
//            // Sakrij nepotrebne labele i kontrole
//            jLabel8.setVisible(false);
//            jLabel9.setVisible(false);
//            jLabel_Ukupno_Dostupnih.setVisible(false);
//
//            // Sakrij tabelu dostupnih takmičara ako ne treba ništa prijavljivati
//            jTable_Dostupni_Takmicari.setVisible(false);
//
//            // Zaključa polja i dugmad
//            jTextField_Nazi_Takmicenja.setEditable(false);
//            jTextField_Datum_Odrzavanja.setEditable(false);
//            jTextField_Lokacija.setEditable(false);
//            jComboBox_Kategorija.setEnabled(false);
//
//            jButton_Azuriraj_Takmicenje.setVisible(false);
//            jButton_Kreiraj_Takmicenje.setVisible(false);
//            jButton_Obrisi_Takmicenje.setVisible(false);
//            jButton_Prijavi_Takmicara.setVisible(false);
//            jButton_Odjavi_Takmicara.setVisible(false);
//
//            // Tooltip objašnjenje
//            jComboBox_Kategorija.setToolTipText("Kategoriju i detalje može menjati samo organizator ovog takmičenja.");
//            jTextField_Nazi_Takmicenja.setToolTipText("Detalje može menjati samo organizator ovog takmičenja.");
//            jTextField_Datum_Odrzavanja.setToolTipText("Detalje može menjati samo organizator ovog takmičenja.");
//            jTextField_Lokacija.setToolTipText("Detalje može menjati samo organizator ovog takmičenja.");
//
//            if (takmicenje.getDatum().before(new Date())) {
//                jTextField_Nazi_Takmicenja.setToolTipText("Detalje nije moguće menjati za već održano takmičenje.");
//                jTextField_Datum_Odrzavanja.setToolTipText("Detalje nije moguće menjati za već održano takmičenje.");
//                jTextField_Lokacija.setToolTipText("Detalje nije moguće menjati za već održano takmičenje.");
//            }
//        }
//        SwingUtilities.invokeLater(() -> {
//            if (!jComboBox_Kategorija.isEnabled()) {
//                jComboBox_Kategorija.setToolTipText("Kategoriju nije moguće menjati za već postojeće takmičenje.");
//            }
//        });
//
//        
//    }
    //////////////////
    //
    //
    /////////////////
//    private void popuniPolja() {
//        jLabel_Organizator.setText(takmicenje.getCvm().getNazivCVM());
//        jTextField_Nazi_Takmicenja.setText(takmicenje.getNazivTakmicenja());
//        jTextField_Datum_Odrzavanja.setText(new SimpleDateFormat("dd. MM. yyyy.").format(takmicenje.getDatum()));
//        jTextField_Lokacija.setText(takmicenje.getLokacija());
//        jComboBox_Kategorija.addItem(takmicenje.getKat().getNazivKategorija());
//
//        // Ako je takmičenje prošlo, disable polja i combo
//        if (takmicenje.getDatum().before(new Date())) {
//            jLabel8.setVisible(false);
//            jLabel9.setVisible(false);
//            jLabel_Ukupno2.setVisible(false);
//
//            jTable_Dostupni_Takmicari.setVisible(false);
//
//            jTextField_Nazi_Takmicenja.setEditable(false);
//            jTextField_Datum_Odrzavanja.setEditable(false);
//            jTextField_Lokacija.setEditable(false);
//            jComboBox_Kategorija.setEnabled(false);
//            jButton_Azuriraj_Takmicenje.setVisible(false);
//            jButton_Kreiraj_Takmicenje.setVisible(false);
//            jButton_Obrisi_Takmicenje.setVisible(false);
//            jButton_Prijavi_Takmicara.setVisible(false);
//            jButton_Odjavi_Takmicara.setVisible(false);
//        }
//
//        // Odloži prikaz tooltips-a dok se forma ne prikaže
//        SwingUtilities.invokeLater(() -> {
//            if (!jComboBox_Kategorija.isEnabled()) {
//                jComboBox_Kategorija.setToolTipText("Kategoriju nije moguće menjati za već postojeće takmičenje.");
//            }
//        });
//
//    }
    private void prikaziPrijavljene() {
        Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_PRIJAVLJENE_TAKMICARE, takmicenje);
        Komunikacija.getInstance().posalji_zahtev(kz);
        Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();

        // Ocisti postojece prijavljene da se ne dupliraju
        if (listaPrijavljeni == null) {
            listaPrijavljeni = new ArrayList<>();
        }
        if (listaPrijavljeniAMK == null) {
            listaPrijavljeniAMK = new ArrayList<>();
        }
        listaPrijavljeni.clear();
        listaPrijavljeniAMK.clear();

        if (so != null && so.getOdgovor() instanceof List) {
            List<Object[]> podaci = (List<Object[]>) so.getOdgovor();

            for (Object[] red : podaci) {
                Takmicar t = (Takmicar) red[0];
                AMKlub a = (AMKlub) red[1];

                // Dodaj samo ako nije vec u listi (za svaki slučaj)
                boolean vecPostoji = listaPrijavljeni.stream()
                        .anyMatch(x -> x.getIdTakmicar() == t.getIdTakmicar());
                if (!vecPostoji) {
                    listaPrijavljeni.add(t);
                    listaPrijavljeniAMK.add(a);
                }
            }

            // Osvezi tabelu prijavljenih
            jTable_Prijavljeni.setModel(new Model_Tabele_Takmicar_Kat(listaPrijavljeni, listaPrijavljeniAMK));
            jTable_Prijavljeni.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            //prikaziPrijavljene();
            osveziUkupanBrojTakmicara();

        }
    }

//radi
//    private void prikaziPrijavljene() {
//        Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_PRIJAVLJENE_TAKMICARE, takmicenje);
//        Komunikacija.getInstance().posalji_zahtev(kz);
//        Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//        if (so != null && so.getOdgovor() instanceof List) {
//            List<Object[]> podaci = (List<Object[]>) so.getOdgovor();
//            List<Takmicar> takmicari = new ArrayList<>();
//            List<AMKlub> amkovi = new ArrayList<>();
//
//            // Uzmi trenutno selektovanu kategoriju iz CB
//            String selektovanaKat = (String) jComboBox_Kategorija.getSelectedItem();
//
//            for (Object[] red : podaci) {
//                Takmicar t = (Takmicar) red[0];
//                AMKlub amk = (AMKlub) red[1];
//
//                // filtriramo da u prijavljenima ostanu samo oni iz izabrane kategorije
//                if (selektovanaKat == null
//                        || takmicenje.getKat().getNazivKategorija().equals(selektovanaKat)
//                        && t.getKatID() == takmicenje.getKat().getIdKategorija()) {
//
//                    takmicari.add(t);
//                    amkovi.add(amk);
//                }
//            }
//
//            jTable_Prijavljeni.setModel(new Model_Tabele_Takmicar_Kat(takmicari, amkovi));
//        }
//    }
//    private void prikaziPrijavljene() {
//        Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_PRIJAVLJENE_TAKMICARE, takmicenje);
//        Komunikacija.getInstance().posalji_zahtev(kz);
//        Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//        listaPrijavljeni.clear();
//        listaPrijavljeniAMK.clear();
//
//        if (so != null && so.getOdgovor() instanceof List) {
//            List<Object[]> podaci = (List<Object[]>) so.getOdgovor();
//            for (Object[] red : podaci) {
//                listaPrijavljeni.add((Takmicar) red[0]);
//                listaPrijavljeniAMK.add((AMKlub) red[1]);
//            }
//        }
//
//        jTable_Prijavljeni.setModel(new Model_Tabele_Takmicar_Kat(listaPrijavljeni, listaPrijavljeniAMK));
//    }
    //////////////////////////////////////////////////////////
    //
    //          razdvajac xD
    //
    //////////////////////////////////////////////////////////
//    private void prikaziPrijavljene() {
//        Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_PRIJAVLJENE_TAKMICARE, takmicenje);
//        Komunikacija.getInstance().posalji_zahtev(kz);
//        Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//        if (so != null) {
//            System.out.println(">>> Odgovor sa servera: " + so.getOdgovor());
//
//            if (so.getOdgovor() instanceof List) {
//                List<Object[]> podaci = (List<Object[]>) so.getOdgovor();
//                System.out.println(">>> Broj prijavljenih: " + podaci.size());
//
//                List<Takmicar> takmicari = new ArrayList<>();
//                List<AMKlub> amkovi = new ArrayList<>();
//
//                for (Object[] red : podaci) {
//                    takmicari.add((Takmicar) red[0]);
//                    amkovi.add((AMKlub) red[1]);
//                }
//
//                jTable_Prijavljeni.setModel(new Model_Tabele_Takmicar_Kat(takmicari, amkovi));
//            }
//        }
//    }
    //////////////////////////////////////////////////////////
    //
    //          razdvajac xD
    //
    //////////////////////////////////////////////////////////
//    private void prikaziDostupne() {
//        // Prikazuje se samo ako takmičenje tek dolazi
//        if (takmicenje.getDatum().after(new Date())) {
//            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_TAKMICARE_IZ_KATEGORIJE, takmicenje.getKat());
//            Komunikacija.getInstance().posalji_zahtev(kz);
//            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//            if (so != null && so.getOdgovor() instanceof List) {
//                List<Object[]> podaci = (List<Object[]>) so.getOdgovor(); 
//                List<Takmicar> takmicari = new ArrayList<>();
//                List<AMKlub> amkovi = new ArrayList<>();
//
//                for (Object[] red : podaci) {
//                    takmicari.add((Takmicar) red[0]);
//                    amkovi.add((AMKlub) red[1]);
//                }
//
//                jTable_Dostupni_Takmicari.setModel(new Model_Tabele_Takmicar_Kat(takmicari, amkovi));
//            }
//        } else {
//            jTable_Dostupni_Takmicari.setVisible(false);
//        }
//    }
    //////////////////////////////////////////////////////////
    //
    //          razdvajac xD
    //
    //////////////////////////////////////////////////////////
//    private void prikaziDostupne() {
//        // Prikazuje se samo ako takmičenje tek dolazi
//        if (takmicenje.getDatum().after(new Date())) {
//            // Uzmi trenutno selektovanu kategoriju iz CB
//            String selektovanaKat = (String) jComboBox_Kategorija.getSelectedItem();
//            if (selektovanaKat == null) {
//                return;
//            }
//
//            try {
//                // Moraš dobiti ceo objekat Kategorija, ne samo naziv
//                Klijentski_Zahtev kzSveKat = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
//                Komunikacija.getInstance().posalji_zahtev(kzSveKat);
//                Serverski_Odgovor soSveKat = Komunikacija.getInstance().primi_odgovor();
//
//                Kategorija trazena = null;
//                if (soSveKat != null && soSveKat.getOdgovor() instanceof List) {
//                    List<Kategorija> sveKat = (List<Kategorija>) soSveKat.getOdgovor();
//                    for (Kategorija k : sveKat) {
//                        if (k.getNazivKategorija().equals(selektovanaKat)) {
//                            trazena = k;
//                            break;
//                        }
//                    }
//                }
//
//                if (trazena == null) {
//                    return;
//                }
//
//                // Sada pošalji zahtev da vrati takmičare iz te kategorije
//                Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_TAKMICARE_IZ_KATEGORIJE, trazena);
//                Komunikacija.getInstance().posalji_zahtev(kz);
//                Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//                if (so != null && so.getOdgovor() instanceof List) {
//                    List<Object[]> podaci = (List<Object[]>) so.getOdgovor();
//                    List<Takmicar> takmicari = new ArrayList<>();
//                    List<AMKlub> amkovi = new ArrayList<>();
//
//                    for (Object[] red : podaci) {
//                        takmicari.add((Takmicar) red[0]);
//                        amkovi.add((AMKlub) red[1]);
//                    }
//
//                    jTable_Dostupni_Takmicari.setModel(new Model_Tabele_Takmicar_Kat(takmicari, amkovi));
//                }
//
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(this, "Greška pri učitavanju dostupnih takmičara: " + e.getMessage());
//            }
//        } else {
//            jTable_Dostupni_Takmicari.setVisible(false);
//        }
//    }
    ////////////
    //
    //
    /////////////////
//    private void prikaziDostupne() {
//        try {
//            // Ako se radi o novom takmičenju — prikazujemo odmah po kategoriji
//            String selektovanaKat = null;
//
//            if (takmicenje != null && takmicenje.getKat() != null) {
//                selektovanaKat = takmicenje.getKat().getNazivKategorija();
//            } else if (jComboBox_Kategorija.getSelectedItem() != null) {
//                selektovanaKat = jComboBox_Kategorija.getSelectedItem().toString();
//            }
//
//            if (selektovanaKat == null || selektovanaKat.isEmpty()) {
//                return;
//            }
//
//            // Dohvati sve kategorije i pronađi odgovarajuću
//            Klijentski_Zahtev kzSveKat = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
//            Komunikacija.getInstance().posalji_zahtev(kzSveKat);
//            Serverski_Odgovor soSveKat = Komunikacija.getInstance().primi_odgovor();
//
//            Kategorija trazena = null;
//            if (soSveKat != null && soSveKat.getOdgovor() instanceof List) {
//                List<Kategorija> sveKat = (List<Kategorija>) soSveKat.getOdgovor();
//                for (Kategorija k : sveKat) {
//                    if (k.getNazivKategorija().equals(selektovanaKat)) {
//                        trazena = k;
//                        break;
//                    }
//                }
//            }
//
//            if (trazena == null) {
//                return;
//            }
//
//            // Pošalji zahtev serveru da vrati takmičare iz izabrane kategorije
//            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_TAKMICARE_IZ_KATEGORIJE, trazena);
//            Komunikacija.getInstance().posalji_zahtev(kz);
//            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//            // Očisti liste
//            listaDostupni.clear();
//            listaDostupniAMK.clear();
//
//            if (so != null && so.getOdgovor() instanceof List) {
//                List<Object[]> podaci = (List<Object[]>) so.getOdgovor();
//
//                for (Object[] red : podaci) {
//                    Takmicar t = (Takmicar) red[0];
//                    AMKlub amk = (AMKlub) red[1];
//
//                    // Ako je već prijavljen, preskoči
//                    boolean vecPrijavljen = listaPrijavljeni.stream()
//                            .anyMatch(p -> p.getIdTakmicar() == t.getIdTakmicar());
//
//                    if (!vecPrijavljen) {
//                        listaDostupni.add(t);
//                        listaDostupniAMK.add(amk);
//                    }
//                }
//            }
//
//            // 🔹 Ažuriraj prikaz u tabeli
//            jTable_Dostupni_Takmicari.setModel(new Model_Tabele_Takmicar_Kat(listaDostupni, listaDostupniAMK));
//            jTable_Dostupni_Takmicari.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Greška pri učitavanju dostupnih takmičara: " + e.getMessage());
//        }
//    }
    private void prikaziDostupne() {
        try {
            //  Odredi izabranu kategoriju  iz takmicenja ili CoB
            String selektovanaKat = null;

            if (takmicenje != null && takmicenje.getKat() != null) {
                selektovanaKat = takmicenje.getKat().getNazivKategorija();
            } else if (jComboBox_Kategorija.getSelectedItem() != null) {
                selektovanaKat = jComboBox_Kategorija.getSelectedItem().toString();
            }

            if (selektovanaKat == null || selektovanaKat.isEmpty()) {
                return;
            }

            //  Ucitaj sve kategorije i pronadji odgovarajuccu
            Klijentski_Zahtev kzSveKat = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
            Komunikacija.getInstance().posalji_zahtev(kzSveKat);
            Serverski_Odgovor soSveKat = Komunikacija.getInstance().primi_odgovor();

            Kategorija trazena = null;
            if (soSveKat != null && soSveKat.getOdgovor() instanceof List) {
                List<Kategorija> sveKat = (List<Kategorija>) soSveKat.getOdgovor();
                for (Kategorija k : sveKat) {
                    if (k.getNazivKategorija().equals(selektovanaKat)) {
                        trazena = k;
                        break;
                    }
                }
            }

            if (trazena == null) {
                return;
            }

            //  Poalji zahtev za takmicare iz te kategorije
            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_TAKMICARE_IZ_KATEGORIJE, trazena);
            Komunikacija.getInstance().posalji_zahtev(kz);
            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();

            // ciscenjeee
            listaDostupni.clear();
            listaDostupniAMK.clear();

            if (so != null && so.getOdgovor() instanceof List) {
                List<Object[]> podaci = (List<Object[]>) so.getOdgovor();

                for (Object[] red : podaci) {
                    Takmicar t = (Takmicar) red[0];
                    AMKlub amk = (AMKlub) red[1];

                    // ️ Preskoci ako je već prijavljen, dupliranjeeeee
                    boolean vecPrijavljen = listaPrijavljeni.stream()
                            .anyMatch(pr -> pr.getIdTakmicar() == t.getIdTakmicar());

                    if (!vecPrijavljen) {
                        listaDostupni.add(t);
                        listaDostupniAMK.add(amk);
                    }
                }
            }

            // ?Prikaz u tabeli
            jTable_Dostupni_Takmicari.setModel(new Model_Tabele_Takmicar_Kat(listaDostupni, listaDostupniAMK));
            jTable_Dostupni_Takmicari.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            //  Auriraj labelu sa brojem dostupnih
            jLabel_Ukupno_Dostupnih.setText(String.valueOf(listaDostupni.size()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška pri učitavanju dostupnih takmičara: " + e.getMessage());
        }
    }

    ////////
    //
    // VALJA
    //
    ////////
//    private void prikaziDostupne() {
//        // Prikazuje se samo ako takmičenje tek dolazi
//        if (takmicenje.getDatum().after(new Date())) {
//            // Uzmi trenutno selektovanu kategoriju iz ComboBox-a
//            String selektovanaKat = (String) jComboBox_Kategorija.getSelectedItem();
//            if (selektovanaKat == null) {
//                return;
//            }
//
//            try {
//                // Dohvati sve kategorije da pronađemo odgovarajuću po nazivu
//                Klijentski_Zahtev kzSveKat = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
//                Komunikacija.getInstance().posalji_zahtev(kzSveKat);
//                Serverski_Odgovor soSveKat = Komunikacija.getInstance().primi_odgovor();
//
//                Kategorija trazena = null;
//                if (soSveKat != null && soSveKat.getOdgovor() instanceof List) {
//                    List<Kategorija> sveKat = (List<Kategorija>) soSveKat.getOdgovor();
//                    for (Kategorija k : sveKat) {
//                        if (k.getNazivKategorija().equals(selektovanaKat)) {
//                            trazena = k;
//                            break;
//                        }
//                    }
//                }
//
//                if (trazena == null) {
//                    return;
//                }
//
//                // Pošalji zahtev da vrati takmičare iz te kategorije
//                Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_TAKMICARE_IZ_KATEGORIJE, trazena);
//                Komunikacija.getInstance().posalji_zahtev(kz);
//                Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//                // Očisti postojeće liste da se ne dupliraju
//                listaDostupni.clear();
//                listaDostupniAMK.clear();
//
//                if (so != null && so.getOdgovor() instanceof List) {
//                    List<Object[]> podaci = (List<Object[]>) so.getOdgovor();
//
//                    for (Object[] red : podaci) {
//                        Takmicar t = (Takmicar) red[0];
//                        AMKlub amk = (AMKlub) red[1];
//
//                        // ✅ Provera da li je već prijavljen (po ID-u takmičara)
//                        boolean vecPrijavljen = listaPrijavljeni.stream()
//                                .anyMatch(prijavljen -> prijavljen.getIdTakmicar() == t.getIdTakmicar());
//
//                        // Dodaj samo ako nije već u prijavljenima
//                        if (!vecPrijavljen) {
//                            listaDostupni.add(t);
//                            listaDostupniAMK.add(amk);
//                        }
//                    }
//                }
//
//                // Osveži prikaz
//                jTable_Dostupni_Takmicari.setModel(new Model_Tabele_Takmicar_Kat(listaDostupni, listaDostupniAMK));
//                jTable_Dostupni_Takmicari.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//
//                osveziUkupanBrojTakmicara();
//
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(this, "Greška pri učitavanju dostupnih takmičara: " + e.getMessage());
//            }
//        } else {
//            jTable_Dostupni_Takmicari.setVisible(false);
//        }
//    }
    ///////////
    //
    //
    //
    /////////////
//    private void prikaziDostupne() {
//        // Prikazuje se samo ako takmičenje tek dolazi
//        if (takmicenje.getDatum().after(new Date())) {
//            // Uzmi trenutno selektovanu kategoriju iz ComboBox-a
//            String selektovanaKat = (String) jComboBox_Kategorija.getSelectedItem();
//            if (selektovanaKat == null) {
//                return;
//            }
//
//            try {
//                // Dohvati sve kategorije da pronađemo odgovarajuću po nazivu
//                Klijentski_Zahtev kzSveKat = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
//                Komunikacija.getInstance().posalji_zahtev(kzSveKat);
//                Serverski_Odgovor soSveKat = Komunikacija.getInstance().primi_odgovor();
//
//                Kategorija trazena = null;
//                if (soSveKat != null && soSveKat.getOdgovor() instanceof List) {
//                    List<Kategorija> sveKat = (List<Kategorija>) soSveKat.getOdgovor();
//                    for (Kategorija k : sveKat) {
//                        if (k.getNazivKategorija().equals(selektovanaKat)) {
//                            trazena = k;
//                            break;
//                        }
//                    }
//                }
//
//                if (trazena == null) {
//                    return;
//                }
//
//                // Pošalji zahtev da vrati takmičare iz te kategorije
//                Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_TAKMICARE_IZ_KATEGORIJE, trazena);
//                Komunikacija.getInstance().posalji_zahtev(kz);
//                Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//                // Očisti postojeće liste da se ne dupliraju
//                listaDostupni.clear();
//                listaDostupniAMK.clear();
//
//                if (so != null && so.getOdgovor() instanceof List) {
//                    List<Object[]> podaci = (List<Object[]>) so.getOdgovor();
//
//                    for (Object[] red : podaci) {
//                        Takmicar t = (Takmicar) red[0];
//                        AMKlub amk = (AMKlub) red[1];
//
//                        // proveri da li je već prijavljen za ovo takmičenje (da ne duplira)
//                        boolean vecPrijavljen = false;
//                        for (Takmicar prijavljen : listaPrijavljeni) {
//                            if (prijavljen.getIdTakmicar() == t.getIdTakmicar()) {
//                                vecPrijavljen = true;
//                                break;
//                            }
//                        }
//
//                        if (!vecPrijavljen) {
//                            listaDostupni.add(t);
//                            listaDostupniAMK.add(amk);
//                        }
//                    }
//                }
//
//                // Osveži prikaz
//                jTable_Dostupni_Takmicari.setModel(new Model_Tabele_Takmicar_Kat(listaDostupni, listaDostupniAMK));
//                jTable_Dostupni_Takmicari.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // dozvoli višestruki izbor
//
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(this, "Greška pri učitavanju dostupnih takmičara: " + e.getMessage());
//            }
//        } else {
//            jTable_Dostupni_Takmicari.setVisible(false);
//        }
//    }
//    private void azurirajPodatkeKategorije() {
//        String izabranaKategorija = (String) jComboBox_Kategorija.getSelectedItem();
//        if (izabranaKategorija == null) {
//            return;
//        }
//
//        try {
//            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
//            Komunikacija.getInstance().posalji_zahtev(kz);
//            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//            if (so != null && so.getOdgovor() instanceof List) {
//                List<Kategorija> sveKategorije = (List<Kategorija>) so.getOdgovor();
//                for (Kategorija k : sveKategorije) {
//                    if (k.getNazivKategorija().equals(izabranaKategorija)) {
//                        // npr. jTextAreaOpisKategorije.setText(k.getOpisKategorija());
//                        prikaziDostupne(); // osvežavanje liste dostupnih takmičara
//                        break;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Greška pri ažuriranju kategorije: " + e.getMessage());
//        }
//    }
    //////////////////////////////////////////////////////////
    //
    //          razdvajac xD
    //
    //////////////////////////////////////////////////////////
//    private void azurirajPodatkeKategorije() {
//        String izabranaKategorija = (String) jComboBox_Kategorija.getSelectedItem();
//        if (izabranaKategorija == null) {
//            return;
//        }
//
//        try {
//            // Učitaj sve kategorije sa servera
//            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
//            Komunikacija.getInstance().posalji_zahtev(kz);
//            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//            if (so != null && so.getOdgovor() instanceof List) {
//                List<Kategorija> sveKategorije = (List<Kategorija>) so.getOdgovor();
//                for (Kategorija k : sveKategorije) {
//                    if (k.getNazivKategorija().equals(izabranaKategorija)) {
//
//                        // 1. Filtriraj prijavljene takmičare da ostanu samo oni iz nove kategorije
//                        List<Takmicar> noviPrijavljeni = new ArrayList<>();
//                        List<AMKlub> noviAMK = new ArrayList<>();
//
//                        for (int i = 0; i < listaPrijavljeni.size(); i++) {
//                            Takmicar t = listaPrijavljeni.get(i);
//                            AMKlub a = listaPrijavljeniAMK.get(i);
//
//                            if (t.getKatID() == k.getIdKategorija()) {
//                                noviPrijavljeni.add(t);
//                                noviAMK.add(a);
//                            }
//                        }
//
//                        listaPrijavljeni = noviPrijavljeni;
//                        listaPrijavljeniAMK = noviAMK;
//                        osveziPrijavljeneTabelu();
//
//                        // 2. Osveži tabelu prijavljenih
//                        jTable_Prijavljeni.setModel(new Model_Tabele_Takmicar_Kat(listaPrijavljeni, listaPrijavljeniAMK));
//
//                        // 3. Ponovo prikaži dostupne (već imaš metodu za to)
//                        prikaziDostupne();
//
//                        break;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Greška pri ažuriranju kategorije: " + e.getMessage());
//        }
//    }
    //////////////////////////////////////////////////////////
    //
    //          razdvajac xD
    //
    //////////////////////////////////////////////////////////
//    private void azurirajPodatkeKategorije() {
//        String izabranaKategorija = (String) jComboBox_Kategorija.getSelectedItem();
//        if (izabranaKategorija == null) {
//            return;
//        }
//
//        try {
//            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
//            Komunikacija.getInstance().posalji_zahtev(kz);
//            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//            if (so != null && so.getOdgovor() instanceof List) {
//                List<Kategorija> sveKategorije = (List<Kategorija>) so.getOdgovor();
//                for (Kategorija k : sveKategorije) {
//                    if (k.getNazivKategorija().equals(izabranaKategorija)) {
//
//                        // filtriraj prijavljene
//                        List<Takmicar> noviPrijavljeni = new ArrayList<>();
//                        List<AMKlub> noviAMK = new ArrayList<>();
//
//                        for (int i = 0; i < listaPrijavljeni.size(); i++) {
//                            Takmicar t = listaPrijavljeni.get(i);
//                            AMKlub a = listaPrijavljeniAMK.get(i);
//
//                            if (t.getKatID() == k.getIdKategorija()) {
//                                noviPrijavljeni.add(t);
//                                noviAMK.add(a);
//                            }
//                        }
//
//                        listaPrijavljeni = noviPrijavljeni;
//                        listaPrijavljeniAMK = noviAMK;
//
//                        // samo jedno osvežavanje
//                        jTable_Prijavljeni.setModel(new Model_Tabele_Takmicar_Kat(listaPrijavljeni, listaPrijavljeniAMK));
//
//                        // dostupni takmičari po novoj kategoriji
//                        prikaziDostupne();
//
//                        // ažuriraj i u objektu takmičenje kategoriju
//                        takmicenje.setKat(k);
//
//                        break;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Greška pri ažuriranju kategorije: " + e.getMessage());
//        }
//    }
    private void azurirajPodatkeKategorije() {
        String izabranaKategorija = (String) jComboBox_Kategorija.getSelectedItem();
        if (izabranaKategorija == null) {
            return;
        }

        try {
            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
            Komunikacija.getInstance().posalji_zahtev(kz);
            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();

            if (so != null && so.getOdgovor() instanceof List) {
                List<Kategorija> sveKategorije = (List<Kategorija>) so.getOdgovor();
                for (Kategorija k : sveKategorije) {
                    if (k.getNazivKategorija().equals(izabranaKategorija)) {

                        // filtriraj prijavljene samo na novu kategoriju
                        List<Takmicar> noviPrijavljeni = new ArrayList<>();
                        List<AMKlub> noviAMK = new ArrayList<>();

                        for (int i = 0; i < listaPrijavljeni.size(); i++) {
                            Takmicar t = listaPrijavljeni.get(i);
                            AMKlub a = listaPrijavljeniAMK.get(i);

                            if (t.getKatID() == k.getIdKategorija()) {
                                noviPrijavljeni.add(t);
                                noviAMK.add(a);
                            }
                        }

                        listaPrijavljeni = noviPrijavljeni;
                        listaPrijavljeniAMK = noviAMK;

                        // samo refresh preko helper metode
                        osveziPrijavljeneTabelu();

                        // dostupni takmicari po novoj kategoriji
                        prikaziDostupne();

                        // update kategorije u objektu takmicenja
                        takmicenje.setKat(k);

                        break;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška pri ažuriranju kategorije: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField_Nazi_Takmicenja = new javax.swing.JTextField();
        jTextField_Datum_Odrzavanja = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox_Kategorija = new javax.swing.JComboBox<>();
        jLabel_Organizator = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Prijavljeni = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel_Ukupno_Prijavljenih = new javax.swing.JLabel();
        jButton_Kreiraj_Takmicenje = new javax.swing.JButton();
        jButton_Azuriraj_Takmicenje = new javax.swing.JButton();
        jButton_Obrisi_Takmicenje = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_Dostupni_Takmicari = new javax.swing.JTable();
        jTextField_Lokacija = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jButton_Prijavi_Takmicara = new javax.swing.JButton();
        jButton_Odjavi_Takmicara = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel_Ukupno_Dostupnih = new javax.swing.JLabel();
        lblErrLokacija = new javax.swing.JLabel();
        lblErrNaziv = new javax.swing.JLabel();
        lblErrDatum = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Detalji Takmičenja");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jTextField_Nazi_Takmicenja, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 72, 191, -1));
        getContentPane().add(jTextField_Datum_Odrzavanja, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 191, -1));

        jLabel5.setText("Datum odrzavanja:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Organizuje");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 24, -1, -1));

        jLabel6.setText("Kategorija:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 75, -1, -1));

        jLabel2.setText("Mesto odrzavanja:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 130, -1, -1));

        getContentPane().add(jComboBox_Kategorija, new org.netbeans.lib.awtextra.AbsoluteConstraints(588, 72, 209, -1));

        jLabel_Organizator.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel_Organizator.setText("jLabel4");
        getContentPane().add(jLabel_Organizator, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 24, 247, -1));

        jLabel4.setText("Takmicenje: ");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 75, -1, -1));

        jLabel3.setText("Prijavljeni takmicari:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, -1, -1));

        jTable_Prijavljeni.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable_Prijavljeni);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 210, 353, 174));

        jLabel7.setText("Ukupno takmicara: ");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 390, -1, -1));
        getContentPane().add(jLabel_Ukupno_Prijavljenih, new org.netbeans.lib.awtextra.AbsoluteConstraints(142, 390, 37, -1));

        jButton_Kreiraj_Takmicenje.setText("Kreiraj takmicenje");
        jButton_Kreiraj_Takmicenje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Kreiraj_TakmicenjeActionPerformed(evt);
            }
        });
        getContentPane().add(jButton_Kreiraj_Takmicenje, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 465, -1, -1));

        jButton_Azuriraj_Takmicenje.setText("Azuriraj takmicenje");
        jButton_Azuriraj_Takmicenje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Azuriraj_TakmicenjeActionPerformed(evt);
            }
        });
        getContentPane().add(jButton_Azuriraj_Takmicenje, new org.netbeans.lib.awtextra.AbsoluteConstraints(243, 465, -1, -1));

        jButton_Obrisi_Takmicenje.setText("Obrisi takmicenje");
        jButton_Obrisi_Takmicenje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Obrisi_TakmicenjeActionPerformed(evt);
            }
        });
        getContentPane().add(jButton_Obrisi_Takmicenje, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 465, -1, -1));

        jButton1.setText("Odustani");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(654, 465, -1, -1));

        jTable_Dostupni_Takmicari.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable_Dostupni_Takmicari);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 210, 353, 174));
        getContentPane().add(jTextField_Lokacija, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 120, 209, -1));

        jLabel8.setText("Lista dostupnih takmicara");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 180, -1, -1));

        jButton_Prijavi_Takmicara.setText("Prijavi takmicara");
        jButton_Prijavi_Takmicara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Prijavi_TakmicaraActionPerformed(evt);
            }
        });
        getContentPane().add(jButton_Prijavi_Takmicara, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 424, -1, -1));

        jButton_Odjavi_Takmicara.setText("Odjavi takmicara");
        jButton_Odjavi_Takmicara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Odjavi_TakmicaraActionPerformed(evt);
            }
        });
        getContentPane().add(jButton_Odjavi_Takmicara, new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 424, -1, -1));

        jLabel9.setText("Ukupno dostupnih takmicara: ");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(589, 390, -1, -1));
        getContentPane().add(jLabel_Ukupno_Dostupnih, new org.netbeans.lib.awtextra.AbsoluteConstraints(765, 390, 37, -1));
        getContentPane().add(lblErrLokacija, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 150, 210, 30));
        getContentPane().add(lblErrNaziv, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 96, 190, 33));
        getContentPane().add(lblErrDatum, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 150, 240, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton_Prijavi_TakmicaraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Prijavi_TakmicaraActionPerformed

        int[] selektovaniRedovi = jTable_Dostupni_Takmicari.getSelectedRows();

        if (selektovaniRedovi.length == 0) {
            JOptionPane.showMessageDialog(this, "Morate izabrati barem jednog takmičara za prijavu.");
            return;
        }

        Model_Tabele_Takmicar_Kat modelDostupni = (Model_Tabele_Takmicar_Kat) jTable_Dostupni_Takmicari.getModel();

        // prolazimo unazad da se indeksi ne poremete prilikom uklanjanja
        for (int i = selektovaniRedovi.length - 1; i >= 0; i--) {
            int red = selektovaniRedovi[i];
            Takmicar t = modelDostupni.getTakmicarAt(red);
            AMKlub a = modelDostupni.getAmkAt(red);

            // Dodaj u prijavljene
            listaPrijavljeni.add(t);
            listaPrijavljeniAMK.add(a);

            // Ukloni iz dostupnih
            listaDostupni.remove(t);
            listaDostupniAMK.remove(a);
        }

        // Osvezi obe tabelu
        osveziPrijavljeneTabelu();
        jTable_Dostupni_Takmicari.setModel(new Model_Tabele_Takmicar_Kat(listaDostupni, listaDostupniAMK));
        jTable_Dostupni_Takmicari.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        osveziUkupanBrojTakmicara();

//        int[] selektovaniRedovi = jTable_Dostupni_Takmicari.getSelectedRows();
//
//        if (selektovaniRedovi.length == 0) {
//            JOptionPane.showMessageDialog(this, "Niste izabrali nijednog takmičara!");
//            return;
//        }
//
//        List<Takmicar> noviPrijavljeni = new ArrayList<>();
//        List<AMKlub> noviPrijavljeniAMK = new ArrayList<>();
//
//        // Dodaj selektovane u listu prijavljenih
//        for (int red : selektovaniRedovi) {
//            Takmicar t = listaDostupni.get(red);
//            AMKlub amk = listaDostupniAMK.get(red);
//
//            listaPrijavljeni.add(t);
//            listaPrijavljeniAMK.add(amk);
//
//            noviPrijavljeni.add(t);
//            noviPrijavljeniAMK.add(amk);
//        }
//
//        // Ukloni ih iz liste dostupnih
//        for (Takmicar t : noviPrijavljeni) {
//            int index = listaDostupni.indexOf(t);
//            if (index != -1) {
//                listaDostupni.remove(index);
//                listaDostupniAMK.remove(index);
//            }
//        }
//
//        // Osveži obe tabele
//        jTable_Dostupni_Takmicari.setModel(new Model_Tabele_Takmicar_Kat(listaDostupni, listaDostupniAMK));
//        jTable_Prijavljeni.setModel(new Model_Tabele_Takmicar_Kat(listaPrijavljeni, listaPrijavljeniAMK));
//
//        JOptionPane.showMessageDialog(this, "Izabrani takmičari su prijavljeni!");

    }//GEN-LAST:event_jButton_Prijavi_TakmicaraActionPerformed

    private void jButton_Odjavi_TakmicaraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Odjavi_TakmicaraActionPerformed

        int[] selektovaniRedovi = jTable_Prijavljeni.getSelectedRows();

        if (selektovaniRedovi.length == 0) {
            JOptionPane.showMessageDialog(this, "Morate izabrati barem jednog takmičara za odjavu.");
            return;
        }

        Model_Tabele_Takmicar_Kat modelPrijavljeni = (Model_Tabele_Takmicar_Kat) jTable_Prijavljeni.getModel();

        // prolazimo unazad da se indeksi ne poremete
        for (int i = selektovaniRedovi.length - 1; i >= 0; i--) {
            int red = selektovaniRedovi[i];
            Takmicar t = modelPrijavljeni.getTakmicarAt(red);
            AMKlub a = modelPrijavljeni.getAmkAt(red);

            // Dodaj nazad u dostupne
            listaDostupni.add(t);
            listaDostupniAMK.add(a);

            // Ukloni iz prijavljenih
            listaPrijavljeni.remove(t);
            listaPrijavljeniAMK.remove(a);
        }

        // Osveži obe tabele
        osveziPrijavljeneTabelu();
        jTable_Dostupni_Takmicari.setModel(new Model_Tabele_Takmicar_Kat(listaDostupni, listaDostupniAMK));
        jTable_Dostupni_Takmicari.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        osveziUkupanBrojTakmicara();

//        int[] selektovaniRedovi = jTable_Prijavljeni.getSelectedRows();
//
//        if (selektovaniRedovi.length == 0) {
//            JOptionPane.showMessageDialog(this, "Morate izabrati barem jednog takmičara za odjavu.");
//            return;
//        }
//
//        // Model prijavljenih
//        Model_Tabele_Takmicar_Kat modelPrijavljeni = (Model_Tabele_Takmicar_Kat) jTable_Prijavljeni.getModel();
//        Model_Tabele_Takmicar_Kat modelDostupni = (Model_Tabele_Takmicar_Kat) jTable_Dostupni_Takmicari.getModel();
//
//        // privremene liste jer brišemo iz originala
//        List<Takmicar> zaDodatiTakmicari = new ArrayList<>();
//        List<AMKlub> zaDodatiAMK = new ArrayList<>();
//
//        // prolazimo unazad da se indeksi ne poremete prilikom uklanjanja
//        for (int i = selektovaniRedovi.length - 1; i >= 0; i--) {
//            int red = selektovaniRedovi[i];
//
//            Takmicar t = modelPrijavljeni.getTakmicarAt(red);
//            AMKlub amk = modelPrijavljeni.getAmkAt(red);
//
//            // Dodaj nazad u dostupne (ako nije već tamo)
//            boolean postoji = false;
//            for (Takmicar d : listaDostupni) {
//                if (d.getIdTakmicar() == t.getIdTakmicar()) {
//                    postoji = true;
//                    break;
//                }
//            }
//            if (!postoji) {
//                zaDodatiTakmicari.add(t);
//                zaDodatiAMK.add(amk);
//            }
//
//            // Ukloni iz prijavljenih
//            listaPrijavljeni.remove(t);
//            listaPrijavljeniAMK.remove(amk);
//        }
//
//        // Dodaj sve odjavljene u listu dostupnih
//        listaDostupni.addAll(zaDodatiTakmicari);
//        listaDostupniAMK.addAll(zaDodatiAMK);
//
//        // Osveži obe tabele
//        jTable_Prijavljeni.setModel(new Model_Tabele_Takmicar_Kat(listaPrijavljeni, listaPrijavljeniAMK));
//        jTable_Dostupni_Takmicari.setModel(new Model_Tabele_Takmicar_Kat(listaDostupni, listaDostupniAMK));
//
//        // Omogući višestruki izbor
//        jTable_Dostupni_Takmicari.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//        jTable_Prijavljeni.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    }//GEN-LAST:event_jButton_Odjavi_TakmicaraActionPerformed

    private void jButton_Azuriraj_TakmicenjeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Azuriraj_TakmicenjeActionPerformed

        try {
            //
            String naziv = jTextField_Nazi_Takmicenja.getText().trim();
            String lokacija = jTextField_Lokacija.getText().trim();
            Date datum = new SimpleDateFormat("dd. MM. yyyy.").parse(jTextField_Datum_Odrzavanja.getText().trim());

            takmicenje.setNazivTakmicenja(naziv);
            takmicenje.setLokacija(lokacija);
            takmicenje.setDatum(new java.sql.Date(datum.getTime()));
            
            // Pre slanja  proverio da li ima prijavljenih takmicara
if (listaPrijavljeni == null || listaPrijavljeni.isEmpty()) {
    JOptionPane.showMessageDialog(this,
        "Morate imati prijavljenog makar jednog takmičara da biste ažurirali takmičenje!",
        "Upozorenje", JOptionPane.WARNING_MESSAGE);
    return;
}


            //  Pripremi podatke za zahtev
            Object[] podaci = new Object[]{takmicenje, listaPrijavljeni};

            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.AZURIRAJ_TAKMICENJE, podaci);
            Komunikacija.getInstance().posalji_zahtev(kz);
            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();

            if ("USPEH".equals(so.getOdgovor())) {
                JOptionPane.showMessageDialog(this, "Takmičenje uspešno ažurirano!");

                if (parent != null) {
                    parent.osveziTabele(); //  osvei prikaz u glavnoj formi
                }

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Greška pri ažuriranju takmičenja. \n"
                        + "Proverite konekciju sa BAZOM!", "GRESKA!", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška: " + e.getMessage());
        }

//        try {
//            // 1. Pokupi podatke sa forme
//            String naziv = jTextField_Nazi_Takmicenja.getText().trim();
//            String lokacija = jTextField_Lokacija.getText().trim();
//            Date datum = new SimpleDateFormat("dd. MM. yyyy.").parse(jTextField_Datum_Odrzavanja.getText().trim());
//
//            takmicenje.setNazivTakmicenja(naziv);
//            takmicenje.setLokacija(lokacija);
//            takmicenje.setDatum(new java.sql.Date(datum.getTime())); // ✅ ispravno
//
//            // 2. Pripremi podatke za zahtev
//            Object[] podaci = new Object[]{takmicenje, listaPrijavljeni};
//
//            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.AZURIRAJ_TAKMICENJE, podaci);
//            Komunikacija.getInstance().posalji_zahtev(kz);
//            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//            if (so.getOdgovor().equals("USPEH")) {
//                JOptionPane.showMessageDialog(this, "Takmičenje uspešno ažurirano!");
//                this.dispose();
//            } else {
//                JOptionPane.showMessageDialog(this, "Greška pri ažuriranju takmičenja.");
//            }
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Greška: " + e.getMessage());
//        }

    }//GEN-LAST:event_jButton_Azuriraj_TakmicenjeActionPerformed

    private void jButton_Obrisi_TakmicenjeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Obrisi_TakmicenjeActionPerformed

        try {

            int potvrda = JOptionPane.showConfirmDialog(this,
                    "Da li ste sigurni da želite da obrišete takmičenje?",
                    "Potvrda", JOptionPane.YES_NO_OPTION);

            if (potvrda == JOptionPane.YES_OPTION) {

                Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.OBRISI_TAKMICENJE, takmicenje);
                Komunikacija.getInstance().posalji_zahtev(kz);
                Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();

                if ("USPEH".equals(so.getOdgovor())) {
                    JOptionPane.showMessageDialog(this, "Takmičenje obrisano!");

                    if (parent != null) {
                        parent.osveziTabele();
                    }

                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Greška pri brisanju takmičenja.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška: " + e.getMessage());
        }

//        int potvrda = JOptionPane.showConfirmDialog(this,
//                "Da li ste sigurni da želite da obrišete takmičenje?",
//                "Potvrda", JOptionPane.YES_NO_OPTION);
//
//        if (potvrda == JOptionPane.YES_OPTION) {
//            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.OBRISI_TAKMICENJE, takmicenje);
//            Komunikacija.getInstance().posalji_zahtev(kz);
//            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//            if ("USPEH".equals(so.getOdgovor())) {
//                JOptionPane.showMessageDialog(this, "Takmičenje uspešno obrisano!");
//                this.dispose();
//            } else {
//                JOptionPane.showMessageDialog(this, "Greška pri brisanju takmičenja!");
//            }
//        }
//        int potvrda = JOptionPane.showConfirmDialog(this,
//                "Da li ste sigurni da želite da obrišete takmičenje \"" + takmicenje.getNazivTakmicenja() + "\"?",
//                "Potvrda brisanja",
//                JOptionPane.YES_NO_OPTION);
//
//        if (potvrda == JOptionPane.YES_OPTION) {
//            try {
//                // 1. Pripremi zahtev za server
//                Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.OBRISI_TAKMICENJE, takmicenje);
//                Komunikacija.getInstance().posalji_zahtev(kz);
//                Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//                // 2. Obradi odgovor
//                if (so != null && "USPEH".equals(so.getOdgovor())) {
//                    JOptionPane.showMessageDialog(this, "Takmičenje je uspešno obrisano.");
//                    this.dispose(); // zatvori formu jer takmičenje više ne postoji
//                } else {
//                    JOptionPane.showMessageDialog(this, "Greška pri brisanju takmičenja.");
//                }
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(this, "Greška: " + e.getMessage());
//            }
//        }

    }//GEN-LAST:event_jButton_Obrisi_TakmicenjeActionPerformed

    private void jButton_Kreiraj_TakmicenjeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Kreiraj_TakmicenjeActionPerformed

        try {
            // validacija osnovnih polja
            String naziv = jTextField_Nazi_Takmicenja.getText().trim();
            String lokacija = jTextField_Lokacija.getText().trim();
            String datumStr = jTextField_Datum_Odrzavanja.getText().trim();
            String nazivKat = (String) jComboBox_Kategorija.getSelectedItem();

            if (naziv.isEmpty() || lokacija.isEmpty() || datumStr.isEmpty() || nazivKat == null) {
                JOptionPane.showMessageDialog(this, "Popunite naziv, datum, lokaciju i izaberite kategoriju.");
                return;
            }

            // parsiraj datum u util.Date pa u sql.Date
            java.util.Date utilDate = new SimpleDateFormat("dd. MM. yyyy.").parse(datumStr);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            // pronadji izabranu kategoriju iz zapamcene liste
            Kategorija izabranaKat = null;
            for (Kategorija k : sveKategorijeCB) {
                if (k.getNazivKategorija().equals(nazivKat)) {
                    izabranaKat = k;
                    break;
                }
            }
            if (izabranaKat == null) {
                JOptionPane.showMessageDialog(this, "Nije moguće odrediti izabranu kategoriju.");
                return;
            }

            
            Takmicenje novo = new Takmicenje();
            
            novo.setNazivTakmicenja(naziv);
            novo.setLokacija(lokacija);
            novo.setDatum(sqlDate);
            
            novo.setCvm(prijavljen);
            
            novo.setKat(izabranaKat);

            
            Object[] podaci = new Object[]{novo, listaPrijavljeni};

            
            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.KREIRAJ_TAKMICENJE, podaci);
            Komunikacija.getInstance().posalji_zahtev(kz);
            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();

            if ("GRESKA_BAZA".equals(so.getOdgovor())) {
                JOptionPane.showMessageDialog(this, "Greška, prilikom kreiranja takmicenja. \n"
                        + "Veza sa bazom je prekinuta!",
                        "Greška!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("USPEH".equals(so.getOdgovor())) {
                JOptionPane.showMessageDialog(this, "Takmičenje je uspešno kreirano.");
                if (parent != null) {
                    parent.osveziTabele();  
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Morate prijaviti makar jednog takmičara!",
                        "Greška pri kreiranju takmičenja.", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška, prilikom kreiranja takmicenja. \n"
                    + "Proverite konekciju sa BAZOM");
        }

//        try {
//        // 1. Validacija
//        if (jTextField_Nazi_Takmicenja.getText().trim().isEmpty() ||
//            jTextField_Lokacija.getText().trim().isEmpty() ||
//            jTextField_Datum_Odrzavanja.getText().trim().isEmpty() ||
//            jComboBox_Kategorija.getSelectedItem() == null) {
//
//            JOptionPane.showMessageDialog(this, "Popunite sva polja!");
//            return;
//        }
//
//        // 2. Kreiraj objekat Takmicenje
//        String naziv = jTextField_Nazi_Takmicenja.getText().trim();
//        String lokacija = jTextField_Lokacija.getText().trim();
//        Date datum = new SimpleDateFormat("dd. MM. yyyy.").parse(jTextField_Datum_Odrzavanja.getText().trim());
//
//        // Nađi kategoriju
//        String nazivKat = (String) jComboBox_Kategorija.getSelectedItem();
//        Kategorija kategorija = null;
//
//        Klijentski_Zahtev kzKat = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
//        Komunikacija.getInstance().posalji_zahtev(kzKat);
//        Serverski_Odgovor soKat = Komunikacija.getInstance().primi_odgovor();
//        if (soKat != null && soKat.getOdgovor() instanceof List) {
//            List<Kategorija> sveKat = (List<Kategorija>) soKat.getOdgovor();
//            for (Kategorija k : sveKat) {
//                if (k.getNazivKategorija().equals(nazivKat)) {
//                    kategorija = k;
//                    break;
//                }
//            }
//        }
//
//        if (kategorija == null) {
//            JOptionPane.showMessageDialog(this, "Kategorija nije pronađena!");
//            return;
//        }
//
//        Takmicenje novo = new Takmicenje();
//        novo.setNazivTakmicenja(naziv);
//        novo.setLokacija(lokacija);
//        novo.setDatum(new java.sql.Date(datum.getTime()));
//        novo.setCvm(prijavljen); // CVM koji kreira takmičenje
//        novo.setKat(kategorija);
//
//        // 3. Kreiraj zahtev
//        Object[] podaci = new Object[]{novo, listaPrijavljeni}; // prijavljeni takmičari
//        Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.KREIRAJ_TAKMICENJE, podaci);
//
//        Komunikacija.getInstance().posalji_zahtev(kz);
//        Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//        if ("USPEH".equals(so.getOdgovor())) {
//            JOptionPane.showMessageDialog(this, "Takmičenje uspešno kreirano!");
//
//            if (parent != null) parent.osveziTabele(); // osveži glavnu formu
//            this.dispose();
//        } else {
//            JOptionPane.showMessageDialog(this, "Greška pri kreiranju takmičenja.");
//        }
//
//    } catch (Exception e) {
//        JOptionPane.showMessageDialog(this, "Greška: " + e.getMessage());
//    }

    }//GEN-LAST:event_jButton_Kreiraj_TakmicenjeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton_Azuriraj_Takmicenje;
    private javax.swing.JButton jButton_Kreiraj_Takmicenje;
    private javax.swing.JButton jButton_Obrisi_Takmicenje;
    private javax.swing.JButton jButton_Odjavi_Takmicara;
    private javax.swing.JButton jButton_Prijavi_Takmicara;
    private javax.swing.JComboBox<String> jComboBox_Kategorija;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_Organizator;
    private javax.swing.JLabel jLabel_Ukupno_Dostupnih;
    private javax.swing.JLabel jLabel_Ukupno_Prijavljenih;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable_Dostupni_Takmicari;
    private javax.swing.JTable jTable_Prijavljeni;
    private javax.swing.JTextField jTextField_Datum_Odrzavanja;
    private javax.swing.JTextField jTextField_Lokacija;
    private javax.swing.JTextField jTextField_Nazi_Takmicenja;
    private javax.swing.JLabel lblErrDatum;
    private javax.swing.JLabel lblErrLokacija;
    private javax.swing.JLabel lblErrNaziv;
    // End of variables declaration//GEN-END:variables

//    private void popuniCB() {
//        try {
//            // Traži sve kategorije
//            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
//            Komunikacija.getInstance().posalji_zahtev(kz);
//            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//            if (so != null && so.getOdgovor() instanceof List) {
//                List<Kategorija> sveKategorije = (List<Kategorija>) so.getOdgovor();
//
//                jComboBox_Kategorija.removeAllItems();
//                for (Kategorija k : sveKategorije) {
//                    jComboBox_Kategorija.addItem(k.getNazivKategorija());
//                    // Ako je ova kategorija ona od takmičenja, selektuj je
//                    if (takmicenje.getKat().getIdKategorija() == k.getIdKategorija()) {
//                        jComboBox_Kategorija.setSelectedItem(k.getNazivKategorija());
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Greška pri učitavanju kategorija: " + e.getMessage());
//        }
//    }
    /////////////
    //
    // RADI KAKO TREBA
    //
    ///////////
//    private void popuniCB() {
//        try {
//            // Traži sve kategorije
//            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
//            Komunikacija.getInstance().posalji_zahtev(kz);
//            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//            if (so != null && so.getOdgovor() instanceof List) {
//                List<Kategorija> sveKategorije = (List<Kategorija>) so.getOdgovor();
//
//                jComboBox_Kategorija.removeAllItems();
//                for (Kategorija k : sveKategorije) {
//                    jComboBox_Kategorija.addItem(k.getNazivKategorija());
//                }
//
//                // ✅ Ako je u pitanju postojeće takmičenje, selektuj njegovu kategoriju
//                if (takmicenje != null && takmicenje.getKat() != null) {
//                    for (Kategorija k : sveKategorije) {
//                        if (takmicenje.getKat().getIdKategorija() == k.getIdKategorija()) {
//                            jComboBox_Kategorija.setSelectedItem(k.getNazivKategorija());
//                            break;
//                        }
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Greška pri učitavanju kategorija: " + e.getMessage());
//        }
//    }
    private void popuniCB() {
        try {
            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
            Komunikacija.getInstance().posalji_zahtev(kz);
            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();

            jComboBox_Kategorija.removeAllItems();
            sveKategorijeCB.clear();

            if (so != null && so.getOdgovor() instanceof List) {
                sveKategorijeCB = (List<Kategorija>) so.getOdgovor();
                for (Kategorija k : sveKategorijeCB) {
                    jComboBox_Kategorija.addItem(k.getNazivKategorija());
                }
            }
            jComboBox_Kategorija.setSelectedIndex(-1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška pri učitavanju kategorija: " + e.getMessage());
        }
    }

    // pomocna  metoda za osvezavanje prijavljenih
    private void osveziPrijavljeneTabelu() {
        if (listaPrijavljeni == null) {
            listaPrijavljeni = new ArrayList<>();
        }
        if (listaPrijavljeniAMK == null) {
            listaPrijavljeniAMK = new ArrayList<>();
        }

        jTable_Prijavljeni.setModel(new Model_Tabele_Takmicar_Kat(listaPrijavljeni, listaPrijavljeniAMK));

        //  visestruki izbor 
        jTable_Prijavljeni.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

//    //  metoda za osvežavanje dostupnih
//    private void osveziDostupneTabelu(List<Takmicar> listaDostupni, List<AMKlub> listaDostupniAMK) {
//        if (listaDostupni == null) {
//            listaDostupni = new ArrayList<>();
//        }
//        if (listaDostupniAMK == null) {
//            listaDostupniAMK = new ArrayList<>();
//        }
//
//        jTable_Dostupni_Takmicari.setModel(new Model_Tabele_Takmicar_Kat(listaDostupni, listaDostupniAMK));
//
//        // Omogući višestruki izbor (uvek lepo postavi)
//        jTable_Dostupni_Takmicari.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//    }
    private void osveziUkupanBrojTakmicara() {
        int brojPrijavljenih = (listaPrijavljeni != null) ? listaPrijavljeni.size() : 0;
        int brojDostupnih = (listaDostupni != null) ? listaDostupni.size() : 0;

        jLabel_Ukupno_Prijavljenih.setText(brojPrijavljenih + "");
        jLabel_Ukupno_Dostupnih.setText(brojDostupnih + "");
    }

    private void frontendProvere(boolean isProslo) {

        // === VALIDACIJA POLJA ===
        // ✅ Datum validiraj samo ako takmičenje nije prošlo
        if (!isProslo) {
            ValidatorUI.validateDatumTakmicenja(jTextField_Datum_Odrzavanja, lblErrDatum);
            ValidatorUI.validateNaziv(jTextField_Nazi_Takmicenja, lblErrNaziv);
            ValidatorUI.validateGrad(jTextField_Lokacija, lblErrLokacija);
        }

        // === Dugmad ===
        ValidatorUI.enableAutoButtonControl(
                jButton_Kreiraj_Takmicenje,
                new JTextField[]{jTextField_Nazi_Takmicenja, jTextField_Lokacija, jTextField_Datum_Odrzavanja},
                new JComboBox[]{jComboBox_Kategorija}
        );

        ValidatorUI.enableAutoButtonControl(
                jButton_Azuriraj_Takmicenje,
                new JTextField[]{jTextField_Nazi_Takmicenja, jTextField_Lokacija, jTextField_Datum_Odrzavanja},
                null
        );
    }

//    private void frontendProvere() {
//
//        // === VALIDACIJA POLJA ===
//        ValidatorUI.validateNameField(jTextField_Nazi_Takmicenja, lblErrNaziv);
//        ValidatorUI.validateNaziv(jTextField_Lokacija, lblErrLokacija);
//        ValidatorUI.validateDatumTakmicenja(jTextField_Datum_Odrzavanja, lblErrDatum);
//
//// === Za dugme KREIRAJ (proverava i ComboBox) ===
//        ValidatorUI.enableAutoButtonControl(
//                jButton_Kreiraj_Takmicenje,
//                new JTextField[]{jTextField_Nazi_Takmicenja, jTextField_Lokacija, jTextField_Datum_Odrzavanja},
//                new JComboBox[]{jComboBox_Kategorija}
//        );
//
//// === Za dugme AŽURIRAJ (bez ComboBox-a) ===
//        ValidatorUI.enableAutoButtonControl(
//                jButton_Azuriraj_Takmicenje,
//                new JTextField[]{jTextField_Nazi_Takmicenja, jTextField_Lokacija, jTextField_Datum_Odrzavanja},
//                null
//        );
//
//    }
}
