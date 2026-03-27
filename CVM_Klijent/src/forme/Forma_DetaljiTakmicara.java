/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package forme;

import controlerUI.ThemeManager;
import controlerUI.ValidatorUI;
import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import komunikacija.Komunikacija;
import model.AMKlub;
import model.Kategorija;
import model.Takmicar;
import operacije.Operacije;
import transfer.Klijentski_Zahtev;
import transfer.Serverski_Odgovor;

/**
 *
 * @author Srecko
 */
public class Forma_DetaljiTakmicara extends javax.swing.JFrame {

    private List<Object[]> svePotrebneINFO;
    private Takmicar takmicar;
    private List<Kategorija> sveKategorije;
    private List<AMKlub> sviAMKlubovi;

    private int AMKID = 0;
    private int mestoID = 0;

    private Forma_Takmicari ft;

    /**
     * Creates new form Forma_DetaljiTakmicara
     */
    public Forma_DetaljiTakmicara(Forma_Takmicari ft, Takmicar takmicar) {
        this.takmicar = takmicar;
        this.ft = ft;

        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        ThemeManager.applyTheme(this);

        ValidatorUI.validateNameField(jTextFieldIME, lblErrorIme);
        ValidatorUI.validateNameField(jTextFieldPREZIME, lblErrorPrezime);
        ValidatorUI.validateDatumRodjenja(jTextFieldDAT_RODJ, lblDatum);
        ValidatorUI.validateTelefon(jTextFieldTEL_TAK, jLabelErrTelefon);

// === Validacija polja ===
        ValidatorUI.validateNameField(jTextFieldIME, lblErrorIme);
        ValidatorUI.validateNameField(jTextFieldPREZIME, lblErrorPrezime);
        ValidatorUI.validateDatumRodjenja(jTextFieldDAT_RODJ, lblDatum);
        ValidatorUI.validateTelefon(jTextFieldTEL_TAK, jLabelErrTelefon);

// === Postavljanje name atributa (da regex logika radi) ===
        jTextFieldIME.setName("ime");
        jTextFieldPREZIME.setName("prezime");
        jTextFieldDAT_RODJ.setName("datum");
        jTextFieldTEL_TAK.setName("telefon");

// === Dugme za dodavanje takmičara ===
        ValidatorUI.enableAutoButtonControl(
                jButtonDODAJ_TAKMICARA,
                new JTextField[]{jTextFieldIME, jTextFieldPREZIME, jTextFieldDAT_RODJ, jTextFieldTEL_TAK},
                new JComboBox[]{jComboBoxNAZIV_KAT, jComboBoxAMK_NAZIV}
        );
        ValidatorUI.updateButtonState(
                jButtonDODAJ_TAKMICARA,
                new JTextField[]{jTextFieldIME, jTextFieldPREZIME, jTextFieldDAT_RODJ, jTextFieldTEL_TAK},
                new JComboBox[]{jComboBoxNAZIV_KAT, jComboBoxAMK_NAZIV}
        );

// === Dugme za čuvanje izmena ===
        ValidatorUI.enableAutoButtonControl(
                jButtonSACUVAJ_IZMENE,
                new JTextField[]{jTextFieldIME, jTextFieldPREZIME, jTextFieldDAT_RODJ, jTextFieldTEL_TAK},
                new JComboBox[]{jComboBoxNAZIV_KAT, jComboBoxAMK_NAZIV}
        );
        ValidatorUI.updateButtonState(
                jButtonSACUVAJ_IZMENE,
                new JTextField[]{jTextFieldIME, jTextFieldPREZIME, jTextFieldDAT_RODJ, jTextFieldTEL_TAK},
                new JComboBox[]{jComboBoxNAZIV_KAT, jComboBoxAMK_NAZIV}
        );

//// === VALIDACIJE POLJA ===
//ValidatorUI.validateNameField(jTextFieldIME, lblErrorIme);
//ValidatorUI.validateNameField(jTextFieldPREZIME, lblErrorPrezime);
//ValidatorUI.validateDatumRodjenja(jTextFieldDAT_RODJ);
//ValidatorUI.validateTelefon(jTextFieldTEL_TAK, jLabelErrTelefon);
//
//// === AUTOMATSKA PROVERA I KONTROLA DUGMETA ===
//ValidatorUI.enableAutoButtonControl(
//    jButtonDODAJ_TAKMICARA,
//    new JTextField[]{
//        jTextFieldIME,
//        jTextFieldPREZIME,
//        jTextFieldDAT_RODJ,
//        jTextFieldTEL_TAK
//    },
//    new JComboBox[]{
//        jComboBoxNAZIV_KAT,
//        jComboBoxAMK_NAZIV
//    }
//);
//
//// odmah proveri stanje (da bude tačno i kad se otvori forma)
//ValidatorUI.updateButtonState(
//    jButtonDODAJ_TAKMICARA,
//    new JTextField[]{
//        jTextFieldIME,
//        jTextFieldPREZIME,
//        jTextFieldDAT_RODJ,
//        jTextFieldTEL_TAK
//    },
//    new JComboBox[]{
//        jComboBoxNAZIV_KAT,
//        jComboBoxAMK_NAZIV
//    }
//);
        //this.ft = (Forma_Takmicari) parent; /////////
        // popuniPodatke();
        jButtonDODAJ_TAKMICARA.setVisible(false);

        jTextFieldID_KAT.setEditable(false);
        jTextAreaOPIS_KAT.setEditable(false);
        jTextFieldAMK_TEL.setEditable(false);
        jTextFieldEMAIL.setEditable(false);
        jTextFieldOBELEZJE.setEditable(false);

        // Popunjavanje ComboBox-ova
        popuniComboBoxeve();

        // Dodavanje listener-a
        jComboBoxNAZIV_KAT.addActionListener(e -> azurirajPodatkeKategorije());
        jComboBoxAMK_NAZIV.addActionListener(e -> azurirajPodatkeAMK());

        if (takmicar == null) {
            // Forma za novog takmičara
            pripremiZaNovogTakmicara();
            jButtonSACUVAJ_IZMENE.setVisible(false);
            jButtonOBRISI_TAKMICARA.setVisible(false);
        } else {
            // Prikaz podataka za postojećeg takmičara
            prikaziPodatkeZaTakmicara();
            postaviPocetneVrednostiComboBox();
        }

        //NE TREBA AL AKO ZATREBA
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                if (ft != null) {
                    ft.osveziTabelu();
                }
            }
        });

//        ValidatorUI.validateDateField(jTextFieldDAT_RODJ);
//        ValidatorUI.validatePhoneField(jTextFieldTEL_TAK);
//        ValidatorUI.validateTextField(jTextFieldEMAIL,
//                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
//                "Unesite ispravan e-mail (npr. ime@domen.com)");
//        ValidatorUI.bindValidationToButton(jButtonDODAJ_TAKMICARA,
//                jTextFieldDAT_RODJ, jTextFieldTEL_TAK, jTextFieldEMAIL);
//        ValidatorUI.validateDateFieldBlocking(jTextFieldDAT_RODJ);
//        ValidatorUI.validatePhoneFieldBlocking(jTextFieldTEL_TAK);
//        ValidatorUI.validateTextFieldBlocking(
//                jTextFieldEMAIL,
//                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
//                "Unesite ispravan e-mail (npr. ime@domen.com)"
//        );
//// Dugme koje se aktivira tek kad su svi podaci validni
//        jButtonDODAJ_TAKMICARA.setEnabled(false);
//
//// Validacije
//        ValidatorUI.validateDateField(jTextFieldDAT_RODJ, jButtonDODAJ_TAKMICARA,
//                jTextFieldDAT_RODJ, jTextFieldTEL_TAK, jTextFieldEMAIL);
//
//        ValidatorUI.validatePhoneField(jTextFieldTEL_TAK, jButtonDODAJ_TAKMICARA,
//                jTextFieldDAT_RODJ, jTextFieldTEL_TAK, jTextFieldEMAIL);
//
//        ValidatorUI.validateEmailField(jTextFieldEMAIL, jButtonDODAJ_TAKMICARA,
//                jTextFieldDAT_RODJ, jTextFieldTEL_TAK, jTextFieldEMAIL);
//// Dugme koje se aktivira tek kad su svi podaci validni
//        jButtonDODAJ_TAKMICARA.setEnabled(false);
//
//// Validacija polja
//        ValidatorUI.validateNameField(jTextFieldIME, jButtonDODAJ_TAKMICARA,
//                jTextFieldIME, jTextFieldPREZIME, jTextFieldDAT_RODJ, jTextFieldTEL_TAK);
//
//        ValidatorUI.validateNameField(jTextFieldPREZIME, jButtonDODAJ_TAKMICARA,
//                jTextFieldIME, jTextFieldPREZIME, jTextFieldDAT_RODJ, jTextFieldTEL_TAK);
//
//        ValidatorUI.validateDateField(jTextFieldDAT_RODJ, jButtonDODAJ_TAKMICARA,
//                jTextFieldIME, jTextFieldPREZIME, jTextFieldDAT_RODJ, jTextFieldTEL_TAK);
//
//        ValidatorUI.validatePhoneField(jTextFieldTEL_TAK, jButtonDODAJ_TAKMICARA,
//                jTextFieldIME, jTextFieldPREZIME, jTextFieldDAT_RODJ, jTextFieldTEL_TAK);
//
//  
////////////////////
//
// radlio
//
///////////////
//        jButtonDODAJ_TAKMICARA.setEnabled(false);
//
//        ValidatorUI.validateNameField(jTextFieldIME, lblErrorIme, jButtonDODAJ_TAKMICARA,
//                jTextFieldIME, jTextFieldPREZIME, jTextFieldDAT_RODJ, jTextFieldTEL_TAK, jTextFieldEMAIL);
//
//        ValidatorUI.validateNameField(jTextFieldPREZIME, lblErrorPrezime, jButtonDODAJ_TAKMICARA,
//                jTextFieldIME, jTextFieldPREZIME, jTextFieldDAT_RODJ, jTextFieldTEL_TAK, jTextFieldEMAIL);
//
//        ValidatorUI.validateDateField(jTextFieldDAT_RODJ, lblErrorDatum, jButtonDODAJ_TAKMICARA,
//                jTextFieldIME, jTextFieldPREZIME, jTextFieldDAT_RODJ, jTextFieldTEL_TAK, jTextFieldEMAIL);
//
//        ValidatorUI.validatePhoneField(jTextFieldTEL_TAK, lblErrorIme9, jButtonDODAJ_TAKMICARA,
//                jTextFieldIME, jTextFieldPREZIME, jTextFieldDAT_RODJ, jTextFieldTEL_TAK, jTextFieldEMAIL);
///////////
//
/////////
//        jTextFieldDAT_RODJ.addFocusListener(new java.awt.event.FocusAdapter() {
//            @Override
//            public void focusLost(java.awt.event.FocusEvent e) {
//                String unos = jTextFieldDAT_RODJ.getText().trim();
//                if (!unos.isEmpty()) {
//                    try {
//                        Date dat = parseDatum(unos);
//                        SimpleDateFormat outFormat = new SimpleDateFormat("dd. MM. yyyy.");
//                        jTextFieldDAT_RODJ.setText(outFormat.format(dat));
//                    } catch (ParseException ex) {
//                        JOptionPane.showMessageDialog(Forma_DetaljiTakmicara.this,
//                                "Uneli ste datum u neispravnom formatu!",
//                                "Greška",
//                                JOptionPane.ERROR_MESSAGE);
//                        jTextFieldDAT_RODJ.requestFocus();
//                    }
//                }
//            }
//        });
// Postavljanje validatora
//
//ValidatorUI.validateNameField(jTextFieldIME, lblErrorIme);
//ValidatorUI.validateNameField(jTextFieldPREZIME, lblErrorPrezime);
//ValidatorUI.validateDatumRodjenja(jTextFieldDAT_RODJ);
//ValidatorUI.validateTelefon(jTextFieldTEL_TAK, jLabelErrTelefon);
//
//// Pretpostavimo da je tvoje dugme jButtonDODAJ_TAKMICARA
//javax.swing.JButton btn = jButtonDODAJ_TAKMICARA;
//
//// dugme je inicijalno isključeno
//btn.setEnabled(false);
//
//// nakon svake validacije pozivaj updateButtonState:
//jTextFieldIME.addFocusListener(new java.awt.event.FocusAdapter() {
//    @Override
//    public void focusLost(java.awt.event.FocusEvent e) {
//        ValidatorUI.updateButtonState(btn, jTextFieldIME, jTextFieldPREZIME, jTextFieldDAT_RODJ, jTextFieldTEL_TAK);
//    }
//});
//
//jTextFieldPREZIME.addFocusListener(new java.awt.event.FocusAdapter() {
//    @Override
//    public void focusLost(java.awt.event.FocusEvent e) {
//        ValidatorUI.updateButtonState(btn, jTextFieldIME, jTextFieldPREZIME, jTextFieldDAT_RODJ, jTextFieldTEL_TAK);
//    }
//});
//
//jTextFieldDAT_RODJ.addFocusListener(new java.awt.event.FocusAdapter() {
//    @Override
//    public void focusLost(java.awt.event.FocusEvent e) {
//        ValidatorUI.updateButtonState(btn, jTextFieldIME, jTextFieldPREZIME, jTextFieldDAT_RODJ, jTextFieldTEL_TAK);
//    }
//});
//
//jTextFieldTEL_TAK.addFocusListener(new java.awt.event.FocusAdapter() {
//    @Override
//    public void focusLost(java.awt.event.FocusEvent e) {
//        ValidatorUI.updateButtonState(btn, jTextFieldIME, jTextFieldPREZIME, jTextFieldDAT_RODJ, jTextFieldTEL_TAK);
//    }
//});
//////////////
//
////////////////////
//        jTextFieldTEL_TAK.addFocusListener(new java.awt.event.FocusAdapter() {
//            @Override
//            public void focusLost(java.awt.event.FocusEvent e) {
//                String unos = jTextFieldTEL_TAK.getText().trim();
//                if (!unos.isEmpty()) {
//                    try {
//                        String standardizovan = standardizujTelefon(unos);
//                        jTextFieldTEL_TAK.setText(standardizovan);
//                        jTextFieldTEL_TAK.setBackground(Color.WHITE); // vrati normalnu boju ako je ok
//                    } catch (Exception ex) {
//                        jTextFieldTEL_TAK.setBackground(Color.PINK); // zacrveni polje
//                        JOptionPane.showMessageDialog(Forma_DetaljiTakmicara.this,
//                                "Neispravan format telefona! Broj mora početi sa 06_ ili +381.",
//                                "Greška",
//                                JOptionPane.ERROR_MESSAGE);
//                        jTextFieldTEL_TAK.requestFocus();
//                    }
//                } else {
//                    jTextFieldTEL_TAK.setBackground(Color.WHITE); // ako je prazno, ostavi normalno
//                }
//            }
//        });
//        popuniComboBoxeve();
//
//        // Dodaj listener za ComboBox za kategorije
//        jComboBoxNAZIV_KAT.addActionListener(e -> azurirajPodatkeKategorije());
//
//        // Dodaj listener za ComboBox za AMK
//        jComboBoxAMK_NAZIV.addActionListener(e -> azurirajPodatkeAMK());
//
//        if (takmicar == null) {
//            // Prazna forma za unos novog takmičara
//            pripremiZaNovogTakmicara();
//            jButtonSACUVAJ_IZMENE.setVisible(false);
//            jButtonOBRISI_TAKMICARA.setVisible(false);
//        } else {
//
//            int idTakmicar = takmicar.getIdTakmicar();
//            String ime = takmicar.getIme();
//
//            // System.out.println(idTakmicar + " " + ime);
//            List<Takmicar> listaTakmicara = new ArrayList<>();
//            List<Kategorija> listaKategorija = new ArrayList<>();
//            List<AMKlub> listaAMK = new ArrayList<>();
//
//            Klijentski_Zahtev kz;
//            Serverski_Odgovor so;
//
//            // Inicijalizacija liste kao List<Object[]>
//            svePotrebneINFO = new ArrayList<>();
//
//            kz = new Klijentski_Zahtev(Operacije.VRATI_SVE_POTREBNE_INFO_ZA_TAKMICARA_KAT_I_AMK, null);
//            Komunikacija.getInstance().posalji_zahtev(kz);
//            so = Komunikacija.getInstance().primi_odgovor();
//
//            if (so != null) {
//                if (so.getOdgovor() instanceof List) {
//                    svePotrebneINFO = (List<Object[]>) so.getOdgovor();
//                    if (!svePotrebneINFO.isEmpty()) {
//                        listaTakmicara = new ArrayList<>();
//                        listaAMK = new ArrayList<>();
//                        listaKategorija = new ArrayList<>();
//
//                        for (Object[] info : svePotrebneINFO) {
//                            Takmicar tak = (Takmicar) info[0];
//                            Kategorija kat = (Kategorija) info[1];
//                            AMKlub amk = (AMKlub) info[2];
//
//                            listaTakmicara.add(tak);
//                            listaKategorija.add(kat);
//                            listaAMK.add(amk);
//
//                            if (idTakmicar == tak.getIdTakmicar()) {
//
//                                jLabelID.setText(String.valueOf(tak.getIdTakmicar()));
//                                jTextFieldIME.setText(tak.getIme());
//                                jTextFieldPREZIME.setText(tak.getPrezime());
//                                jTextFieldDAT_RODJ.setText(String.valueOf(tak.getDatumRodjenja()));
//                                jTextFieldTEL_TAK.setText(tak.getBrojTelefona_takmicar());
//                                jTextFieldOBELEZJE.setText(tak.getObelezjeTakmicara());
//
//                                jTextFieldID_KAT.setText(String.valueOf(kat.getIdKategorija()));
//                                //    jTextFieldNAZIV_KAT.setText(kat.getNazivKategorija());
//                                //jComboBoxNAZIV_KAT
//                                jTextAreaOPIS_KAT.setText(kat.getOpisKategorija());
//
//                                //    jTextFieldNAZIV_AMK.setText(amk.getNazivAMKluba());
//                                //jComboBoxAMK_NAZIV
//                                jTextFieldAMK_TEL.setText(amk.getBrojTelefona());
//                                jTextFieldEMAIL.setText(amk.getEmailAMK());
//                            }
//                        }
//
////                    Model_Tabele_Kategorija mtk = new Model_Tabele_Kategorija(listaKategorija);
////                    jTableKATEGORIJE.setModel(mtk);
//                    } else {
//                        JOptionPane.showMessageDialog(this, "Lista podataka je prazna!", "Greška!", JOptionPane.ERROR_MESSAGE);
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(this, "Odgovor nije u očekivanom formatu!", "Greška!", JOptionPane.ERROR_MESSAGE);
//                }
//            } else {
//                JOptionPane.showMessageDialog(this, "Greška prilikom dobijanja podataka", "Greška!", JOptionPane.ERROR_MESSAGE);
//            }
//        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblErrorIme8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelID = new javax.swing.JLabel();
        jTextFieldDAT_RODJ = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldPREZIME = new javax.swing.JTextField();
        jTextFieldID_KAT = new javax.swing.JTextField();
        jTextFieldIME = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jComboBoxAMK_NAZIV = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButtonOBRISI_TAKMICARA = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldEMAIL = new javax.swing.JTextField();
        jButtonSACUVAJ_IZMENE = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldAMK_TEL = new javax.swing.JTextField();
        lblErrorPrezime = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButtonDODAJ_TAKMICARA = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaOPIS_KAT = new javax.swing.JTextArea();
        jTextFieldOBELEZJE = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblErrorIme = new javax.swing.JLabel();
        jTextFieldTEL_TAK = new javax.swing.JTextField();
        jComboBoxNAZIV_KAT = new javax.swing.JComboBox<>();
        jLabelErrTelefon = new javax.swing.JLabel();
        lblDatum = new javax.swing.JLabel();

        lblErrorIme8.setText("jLabel13");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Podaci o takmičaru");

        jPanel1.setToolTipText("Podaci o Takmičaru");
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelID.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelID.setText("ID");
        jPanel1.add(jLabelID, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, -1, -1));
        jPanel1.add(jTextFieldDAT_RODJ, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 240, 223, -1));

        jLabel4.setText("Datum rodjenja");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, -1, -1));

        jLabel6.setText("Obelezje takmicara");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, -1, -1));
        jPanel1.add(jTextFieldPREZIME, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 180, 223, -1));
        jPanel1.add(jTextFieldID_KAT, new org.netbeans.lib.awtextra.AbsoluteConstraints(678, 55, 223, -1));
        jPanel1.add(jTextFieldIME, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 223, -1));

        jButton1.setText("Odustani");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 430, -1, -1));

        jLabel10.setText("Email");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(612, 385, -1, -1));

        jPanel1.add(jComboBoxAMK_NAZIV, new org.netbeans.lib.awtextra.AbsoluteConstraints(674, 272, 223, -1));

        jLabel2.setText("Prezime");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("ID");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, -1, -1));

        jButtonOBRISI_TAKMICARA.setText("Obrisi takmicara");
        jButtonOBRISI_TAKMICARA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOBRISI_TAKMICARAActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonOBRISI_TAKMICARA, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 430, -1, -1));

        jLabel5.setText("Broj telefona");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, -1, -1));
        jPanel1.add(jTextFieldEMAIL, new org.netbeans.lib.awtextra.AbsoluteConstraints(674, 382, 223, -1));

        jButtonSACUVAJ_IZMENE.setText("Savuvaj izmene");
        jButtonSACUVAJ_IZMENE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSACUVAJ_IZMENEActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonSACUVAJ_IZMENE, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 430, -1, -1));

        jLabel7.setText("ID kategorije");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 58, -1, -1));
        jPanel1.add(jTextFieldAMK_TEL, new org.netbeans.lib.awtextra.AbsoluteConstraints(674, 328, 223, -1));
        jPanel1.add(lblErrorPrezime, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 200, 350, 40));

        jLabel9.setText("Opis Kategorije");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 158, -1, -1));

        jLabel3.setText("Ime");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, -1, -1));

        jButtonDODAJ_TAKMICARA.setText("Dodaj takmicara");
        jButtonDODAJ_TAKMICARA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDODAJ_TAKMICARAActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDODAJ_TAKMICARA, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 430, -1, -1));

        jTextAreaOPIS_KAT.setColumns(20);
        jTextAreaOPIS_KAT.setRows(5);
        jScrollPane1.setViewportView(jTextAreaOPIS_KAT);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(674, 158, 223, -1));
        jPanel1.add(jTextFieldOBELEZJE, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 370, 223, -1));

        jLabel11.setText("Naziv AM kluba");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(559, 275, -1, -1));

        jLabel8.setText("Naziv kategorije");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 103, -1, -1));

        jLabel12.setText("Broj telefona");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(573, 331, -1, -1));
        jPanel1.add(lblErrorIme, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 350, 40));
        jPanel1.add(jTextFieldTEL_TAK, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 300, 223, -1));

        jPanel1.add(jComboBoxNAZIV_KAT, new org.netbeans.lib.awtextra.AbsoluteConstraints(678, 103, 223, -1));
        jPanel1.add(jLabelErrTelefon, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 320, 310, 40));
        jPanel1.add(lblDatum, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 260, 310, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(54, 54, 54))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
       // JOptionPane.showMessageDialog(this, "Sistem ne moze da sacuva Takmicara", "Upozorenje", JOptionPane.WARNING_MESSAGE);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonSACUVAJ_IZMENEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSACUVAJ_IZMENEActionPerformed

        try {
            int idTak = Integer.parseInt(jLabelID.getText().trim());
            String ime = jTextFieldIME.getText().trim();
            String prezime = jTextFieldPREZIME.getText().trim();

            // Datum (sa vise formata)
            String dateStr = jTextFieldDAT_RODJ.getText().trim();
            Date datRodj = parseDatum(dateStr); // koristi  za više formata

            String brTelTak = jTextFieldTEL_TAK.getText().trim();

            // Generisanje obelezja format
            int katID = Integer.parseInt(jTextFieldID_KAT.getText().trim());
            String obelezje = idTak + "/" + ime.charAt(0) + prezime.charAt(0) + "/" + jComboBoxNAZIV_KAT.getSelectedItem();

            // 
            Takmicar tak = new Takmicar(idTak, ime, prezime, datRodj, brTelTak, obelezje, katID, AMKID);

            //
            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.AZURIRAJ_TAKMICARA, tak);
            Komunikacija.getInstance().posalji_zahtev(kz);

            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
            if (so != null && "USPEH!".equals(so.getOdgovor())) {
                JOptionPane.showMessageDialog(this, "Podaci su uspešno ažurirani.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Greška pri ažuriranju takmičara.", "Greška", JOptionPane.ERROR_MESSAGE);
            }

            ft.osveziTabelu();  // osveži prikaz u glavnoj formi
            this.dispose();

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Datum nije ispravan format!", "Greška", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Došlo je do greške: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }

//        List<Object[]> listaOBJ = new ArrayList<>();
//
//        try {
//
//            int idTak = Integer.valueOf(jLabelID.getText());
//            String ime = jTextFieldIME.getText();
//            String prezime = jTextFieldPREZIME.getText();
//
//            String dateStr = jTextFieldDAT_RODJ.getText(); // Uzimanje teksta iz polja
//            SimpleDateFormat sdf = new SimpleDateFormat("dd. MM. yyyy."); // Definiši format datuma
//            Date datRodj = sdf.parse(dateStr); // Parsiranje u Date objekat
//            System.out.println("Datum rođenja (Date): " + datRodj);
//
//            String brTelTak = jTextFieldTEL_TAK.getText();
//            //String obelezje = jTextFieldOBELEZJE.getText();
//            String obelezje = idTak + "/" + ime.charAt(0) + "" + prezime.charAt(0) + "/" + jComboBoxNAZIV_KAT.getSelectedItem();
//
//            int katID = Integer.valueOf(jTextFieldID_KAT.getText());
//            // String nazivKAT = jComboBoxNAZIV_KAT.getItemAt(katID - 1);
//            // String opisKAT = jTextAreaOPIS_KAT.getText();
//
//            // String nazivAMK = jComboBoxAMK_NAZIV.getItemAt(0);
//            //String TelAMK = jTextFieldAMK_TEL.getText();
//            // String email = jTextFieldEMAIL.getText();
//            Takmicar tak = new Takmicar(idTak, ime, prezime, datRodj, brTelTak, obelezje, katID, AMKID);
//            // Kategorija kat = new Kategorija(katID, nazivKAT, opisKAT);
//            //  AMKlub amk = new AMKlub(AMKID, nazivAMK, TelAMK, email, katID);
//
//            // listaOBJ.add(new Object[]{tak, kat, amk});  // lista niza objekata burageru xD           
//            //Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.AZURIRAJ_TAKMICARA, listaOBJ); //stali smo treba da se upise u bazu.
//            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.AZURIRAJ_TAKMICARA, tak);
//            Komunikacija.getInstance().posalji_zahtev(kz);
//
//            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//            if (so != null && so.getOdgovor().equals("USPEH!")) {
//                JOptionPane.showMessageDialog(this, "Podaci su uspešno ažurirani.", "Uspeh!", JOptionPane.INFORMATION_MESSAGE);
//            } else {
//                JOptionPane.showMessageDialog(this, "Došlo je do greške prilikom ažuriranja podataka.", "Greška!", JOptionPane.ERROR_MESSAGE);
//            }
//
//        } catch (ParseException e) {
//            System.out.println("Greška pri parsiranju datuma: " + e.getMessage());
//            JOptionPane.showMessageDialog(this, "Greška pri parsiranju datuma: " + e.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
//        }
//
//        ft.osveziTabelu();      /// POPRAVITI OSVEZAVANJE TABELE
//        this.dispose();
    }//GEN-LAST:event_jButtonSACUVAJ_IZMENEActionPerformed

    private void jButtonDODAJ_TAKMICARAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDODAJ_TAKMICARAActionPerformed

//        try {
//
//            int idTak = vrati_id_tak() + 1;
//            //jLabelID.setText(String.valueOf(idTak));
//            //jLabelID.setText(String.valueOf(takmicar.getIdTakmicar()));
//
//            String ime = jTextFieldIME.getText();
//            String prezime = jTextFieldPREZIME.getText();
//
//            String dateStr = jTextFieldDAT_RODJ.getText(); // Uzimanje teksta iz polja
//            SimpleDateFormat sdf = new SimpleDateFormat("dd. MM. yyyy."); // Definiši format datuma
//            Date datRodj = sdf.parse(dateStr); // Parsiranje u Date objekat
//            System.out.println("Datum rođenja (Date): " + datRodj);
//
//            String brTelTak = jTextFieldTEL_TAK.getText();
//            String obelezje = idTak + "/" + ime.charAt(0) + "" + prezime.charAt(0) + "/" + jComboBoxNAZIV_KAT.getSelectedItem();
//
//            int katID = Integer.valueOf(jTextFieldID_KAT.getText());
//
//            Takmicar tak = new Takmicar(idTak, ime, prezime, datRodj, brTelTak, obelezje, katID, AMKID);
//
//            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.DODAJ_TAKMICARA, tak);
//            Komunikacija.getInstance().posalji_zahtev(kz);
//
//            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//            if (so != null && so.getOdgovor().equals("USPEH!")) {
//                JOptionPane.showMessageDialog(this, "Podaci su uspešno upisani.", "Uspeh!", JOptionPane.INFORMATION_MESSAGE);
//            } else {
//                JOptionPane.showMessageDialog(this, "Došlo je do greške prilikom upisivanja podataka.", "Greška!", JOptionPane.ERROR_MESSAGE);
//            }
//
//        } catch (ParseException e) {
//            System.out.println("Greška pri parsiranju datuma: " + e.getMessage());
//            JOptionPane.showMessageDialog(this, "Greška pri parsiranju datuma: " + e.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
//        }
//
//        ft.osveziTabelu();
//        this.dispose();
        try {

            // ID takmičara – komplikacije nad kompl...
            int idTak = vrati_id_tak() + 1;

            String ime = jTextFieldIME.getText().trim();
            String prezime = jTextFieldPREZIME.getText().trim();

            String dateStr = jTextFieldDAT_RODJ.getText().trim();
            Date datRodj = parseDatum(dateStr);

            String brTelTak = jTextFieldTEL_TAK.getText().trim();

            // Generisanje obelezja takmičara
            int katID = Integer.parseInt(jTextFieldID_KAT.getText());
            String obelezje = idTak + "/" + ime.charAt(0) + prezime.charAt(0) + "/" + jComboBoxNAZIV_KAT.getSelectedItem();

            if (jComboBoxNAZIV_KAT.getSelectedIndex() == -1 || jComboBoxAMK_NAZIV.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this,
                        "Morate odabrati opcije iz padajućeg menija!",
                        "Greška",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
//            if (!proveraUnosa()) {
//                return;
//            }

            // generixanje
            Takmicar tak = new Takmicar(idTak, ime, prezime, datRodj, brTelTak, obelezje, katID, AMKID);

            // Slanje 
            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.DODAJ_TAKMICARA, tak);
            Komunikacija.getInstance().posalji_zahtev(kz);

            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();

            if (so != null && "USPEH!".equals(so.getOdgovor())) {
                JOptionPane.showMessageDialog(this, "Takmičar je uspešno dodat!", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Greška pri dodavanju takmičara! \n"
                        + "Proverite konekciju sa BAZOM!", "Greška", JOptionPane.ERROR_MESSAGE);
            }

            ft.osveziTabelu();  
            this.dispose();     

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Datum mora biti u formatu dd. MM. yyyy.", "Greška", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Morate odabrati opcije iz padajuceg menija", "Greška", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButtonDODAJ_TAKMICARAActionPerformed

    private void jButtonOBRISI_TAKMICARAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOBRISI_TAKMICARAActionPerformed

        int potvrda = JOptionPane.showConfirmDialog(this,
                "Da li ste sigurni da želite da obrišete ovog takmičara?",
                "Potvrda brisanja", JOptionPane.YES_NO_OPTION);

        if (potvrda == JOptionPane.YES_OPTION) {
            try {
                Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.OBRISI_TAKMICARA, takmicar);
                Komunikacija.getInstance().posalji_zahtev(kz);
                Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();

                if (so != null && "USPEH!".equals(so.getOdgovor())) {
                    JOptionPane.showMessageDialog(this, "Takmičar je uspešno obrisan.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
                    ft.osveziTabelu();
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Greška pri brisanju takmičara.\n"
                            + "Takmičar je prijavljen za predstojeće takmičenje", "Greška", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Došlo je do greške: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
            }
        }

//        int ID = Integer.valueOf(jLabelID.getText());
//
//        Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.OBRISI_TAKMICARA, ID);
//        Komunikacija.getInstance().posalji_zahtev(kz);
//        Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//        if (so != null && so.getOdgovor().equals("USPEH!")) {
//            JOptionPane.showMessageDialog(this, "Takmicar je uspesno obrisan", "Uspeh!", JOptionPane.INFORMATION_MESSAGE);
//        } else {
//            JOptionPane.showMessageDialog(this, "Greska prilikom brisanja takmicara!", "Greska", JOptionPane.ERROR_MESSAGE);
//        }
//
//        ft.osveziTabelu();
//        this.dispose();
    }//GEN-LAST:event_jButtonOBRISI_TAKMICARAActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonDODAJ_TAKMICARA;
    private javax.swing.JButton jButtonOBRISI_TAKMICARA;
    private javax.swing.JButton jButtonSACUVAJ_IZMENE;
    private javax.swing.JComboBox<String> jComboBoxAMK_NAZIV;
    private javax.swing.JComboBox<String> jComboBoxNAZIV_KAT;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelErrTelefon;
    private javax.swing.JLabel jLabelID;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaOPIS_KAT;
    private javax.swing.JTextField jTextFieldAMK_TEL;
    private javax.swing.JTextField jTextFieldDAT_RODJ;
    private javax.swing.JTextField jTextFieldEMAIL;
    private javax.swing.JTextField jTextFieldID_KAT;
    private javax.swing.JTextField jTextFieldIME;
    private javax.swing.JTextField jTextFieldOBELEZJE;
    private javax.swing.JTextField jTextFieldPREZIME;
    private javax.swing.JTextField jTextFieldTEL_TAK;
    private javax.swing.JLabel lblDatum;
    private javax.swing.JLabel lblErrorIme;
    private javax.swing.JLabel lblErrorIme8;
    private javax.swing.JLabel lblErrorPrezime;
    // End of variables declaration//GEN-END:variables

    private void prikaziPodatkeZaTakmicara() {
        // Popunjavanje osnovnih podataka o takmičaru
        jLabelID.setText(String.valueOf(takmicar.getIdTakmicar()));
        jTextFieldIME.setText(takmicar.getIme());
        jTextFieldPREZIME.setText(takmicar.getPrezime());
        //jTextFieldDAT_RODJ.setText(String.valueOf(takmicar.getDatumRodjenja()));
        jTextFieldTEL_TAK.setText(takmicar.getBrojTelefona_takmicar());
        jTextFieldOBELEZJE.setText(takmicar.getObelezjeTakmicara());

        // Formatiranje datuma
        //SimpleDateFormat sdf = new SimpleDateFormat("dd. MM. yyyy.");
        SimpleDateFormat outFormat = new SimpleDateFormat("dd. MM. yyyy.");
        //jTextFieldDAT_RODJ.setText(outFormat.format(datRodj));

        // Provera da li je datum null pre formatiranja
        if (takmicar.getDatumRodjenja() != null) {
            jTextFieldDAT_RODJ.setText(outFormat.format(takmicar.getDatumRodjenja()));
        } else {
            jTextFieldDAT_RODJ.setText(""); // Postavlja prazan tekst ako je datum null
        }

        // Postavljanje ComboBox-ova na vrednosti takmičara
        for (Kategorija kat : sveKategorije) {
            if (kat.getIdKategorija() == takmicar.getKatID()) {
                jComboBoxNAZIV_KAT.setSelectedItem(kat.getNazivKategorija());
                azurirajPodatkeKategorije(); // Ažuriraj tekstualna polja za kategorije
                break;
            }
        }

        for (AMKlub amk : sviAMKlubovi) {
            if (amk.getIdAMK() == takmicar.getAMKid()) {
                jComboBoxAMK_NAZIV.setSelectedItem(amk.getNazivAMKluba());
                azurirajPodatkeAMK(); // Ažuriraj tekstualna polja za AMK
                break;
            }
        }
    }

    private void pripremiZaNovogTakmicara() {

        int idTak = vrati_id_tak() + 1;
        jLabelID.setText(String.valueOf(idTak)); // Prikaz ID-a za novog takmičara
        
        jLabelID.setVisible(false);    // KAO I YA MESTO DA NE KMOPLIKUJEM SEBI VEC ISKOMPLIKOAVN ZIVOT
        jLabel1.setVisible(false);
        
        jTextFieldIME.setText("");
        jTextFieldPREZIME.setText("");
        jTextFieldDAT_RODJ.setText("");
        jTextFieldTEL_TAK.setText("");
        jTextFieldOBELEZJE.setText("");

        jTextFieldID_KAT.setText("");
        //    jTextFieldNAZIV_KAT.setText("");
        jTextAreaOPIS_KAT.setText("");

        //    jTextFieldNAZIV_AMK.setText("");
        jTextFieldAMK_TEL.setText("");
        jTextFieldEMAIL.setText("");

        // Vidljivost dugmadi
        jButtonDODAJ_TAKMICARA.setVisible(true); // Dugme za dodavanje treba da bude vidljivo
    }

    private void postaviPocetneVrednostiComboBox() {
        if (takmicar != null) {
            // Postavljanje odgovarajuće kategorije u ComboBox
            for (Kategorija kat : sveKategorije) {
                if (kat.getIdKategorija() == takmicar.getKatID()) {
                    jComboBoxNAZIV_KAT.setSelectedItem(kat.getNazivKategorija());
                    azurirajPodatkeKategorije();
                    break;
                }
            }

            // Postavljanje odgovarajućeg AMK u ComboBox
            for (AMKlub amk : sviAMKlubovi) {
                if (amk.getIdAMK() == takmicar.getAMKid()) {
                    jComboBoxAMK_NAZIV.setSelectedItem(amk.getNazivAMKluba());
                    azurirajPodatkeAMK();
                    break;
                }
            }
        }
    }

    private void popuniComboBoxeve() {
        try {
            // Preuzimanje svih kategorija
            Klijentski_Zahtev kzKat = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
            Komunikacija.getInstance().posalji_zahtev(kzKat);
            Serverski_Odgovor soKat = Komunikacija.getInstance().primi_odgovor();
            sveKategorije = (List<Kategorija>) soKat.getOdgovor();

            jComboBoxNAZIV_KAT.removeAllItems();
            for (Kategorija kat : sveKategorije) {
                jComboBoxNAZIV_KAT.addItem(kat.getNazivKategorija());
            }
            jComboBoxNAZIV_KAT.setSelectedIndex(-1);

            // Preuzimanje svih AMK-ova
            Klijentski_Zahtev kzAMK = new Klijentski_Zahtev(Operacije.VRATI_SVE_AMK, null);
            Komunikacija.getInstance().posalji_zahtev(kzAMK);
            Serverski_Odgovor soAMK = Komunikacija.getInstance().primi_odgovor();
            sviAMKlubovi = (List<AMKlub>) soAMK.getOdgovor();

            jComboBoxAMK_NAZIV.removeAllItems();
            for (AMKlub amk : sviAMKlubovi) {
                jComboBoxAMK_NAZIV.addItem(amk.getNazivAMKluba());
            }
            jComboBoxAMK_NAZIV.setSelectedIndex(-1);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška prilikom popunjavanja ComboBox-ova: " + e.getMessage());
        }
    }

    private void azurirajPodatkeKategorije() {
        if (sveKategorije == null || sveKategorije.isEmpty()) {
            return; // Provera da li liste postoje
        }
        String izabranaKategorija = (String) jComboBoxNAZIV_KAT.getSelectedItem();
        if (izabranaKategorija == null) {
            return; // Provera da li je izabrano svatsa nesto
        }
        for (Kategorija kat : sveKategorije) {
            if (kat.getNazivKategorija().equals(izabranaKategorija)) {
                jTextFieldID_KAT.setText(String.valueOf(kat.getIdKategorija()));
                jTextAreaOPIS_KAT.setText(kat.getOpisKategorija());
                return; // Pronađena kategorija
            }
        }

        // Ako nije pronađena odgovarajuća kategorija
        jTextFieldID_KAT.setText("");
        jTextAreaOPIS_KAT.setText("");
    }

    private void azurirajPodatkeAMK() {
        if (sviAMKlubovi == null || sviAMKlubovi.isEmpty()) {
            return; // Provera da li liste postoje
        }
        String izabraniAMK = (String) jComboBoxAMK_NAZIV.getSelectedItem();
        if (izabraniAMK == null) {
            return; // Provera da li je nešto izabrano
        }
        for (AMKlub amk : sviAMKlubovi) {
            if (amk.getNazivAMKluba().equals(izabraniAMK)) {
                jTextFieldAMK_TEL.setText(amk.getBrojTelefona());
                jTextFieldEMAIL.setText(amk.getEmailAMK());
                AMKID = amk.getIdAMK();                         ///////////////////// UZIMA ID KATEGORIJE ZA UPIS U BAZU
                mestoID = amk.getMestoID();
                return; // Pronađen AMK, ydravo dovidjenja
            }
        }

        // Ako nije pronađen odgovarajući AMK
        jTextFieldAMK_TEL.setText("");
        jTextFieldEMAIL.setText("");
    }

    /*sada ne puca nista, ali opet na promenu necega u CB ne menjaju se ostali relevantni podaci.
//srediti to i pritom da ne pica program kad se nesto menja*/
//    private void azurirajPodatkeZaKategoriju() {
//        String odabranaKategorija = (String) jComboBoxNAZIV_KAT.getSelectedItem();
//        try {
//            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_KATEGORIJU_PO_NAZIVU, odabranaKategorija);
//            Komunikacija.getInstance().posalji_zahtev(kz);
//            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//            Kategorija kategorija = (Kategorija) so.getOdgovor();
//
//            if (kategorija != null) {
//                jTextFieldID_KAT.setText(String.valueOf(kategorija.getIdKategorija()));
//                jTextAreaOPIS_KAT.setText(kategorija.getOpisKategorija());
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Greška prilikom ažuriranja podataka za kategoriju: " + e.getMessage());
//        }
//    }
//
//    private void azurirajPodatkeZaAMK() {
//        String odabraniAMK = (String) jComboBoxAMK_NAZIV.getSelectedItem();
//        try {
//            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_AMK_PO_NAZIVU, odabraniAMK);
//            Komunikacija.getInstance().posalji_zahtev(kz);
//            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//            AMKlub amk = (AMKlub) so.getOdgovor();
//
//            if (amk != null) {
//                jTextFieldAMK_TEL.setText(amk.getBrojTelefona());
//                jTextFieldEMAIL.setText(amk.getEmailAMK());
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Greška prilikom ažuriranja podataka za AMK: " + e.getMessage());
//        }
//    }
    private int vrati_id_tak() {
        Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_NAJVECI_ID_TAKMICARA, null);
        Komunikacija.getInstance().posalji_zahtev(kz);
        Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();

        int najveciId = 0;
        if (so != null && so.getOdgovor() instanceof Integer) {
            najveciId = (Integer) so.getOdgovor();
        }
        //System.out.println("Najveći ID takmičara: " + najveciId); // Ispisna poruka
        return najveciId;
    }

    private Date parseDatum(String dateStr) throws ParseException {
        String[] formati = {
            "dd.MM.yyyy",
            "dd.MM.yyyy.",
            "dd/MM/yyyy",
            "yyyy-MM-dd",
            "d.M.yyyy",
            "dd. MM. yyyy."
        };

        for (String f : formati) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(f);
                sdf.setLenient(false);
                return sdf.parse(dateStr);
            } catch (ParseException ignored) {
            }
        }

        throw new ParseException("Nepoznat format datuma: " + dateStr, 0);
    }

    private String standardizujTelefon(String unos) throws Exception {
        String broj = unos.trim().replaceAll("\\s+", ""); // ukloni razmake

        if (broj.startsWith("+381")) {
            // već je u međunarodnom formatu → prebacimo na 06
            broj = "0" + broj.substring(3);
        }

        if (broj.startsWith("06")) {
            return broj;
        } else {
            throw new Exception("Broj mora početi sa 06 ili +381");
        }
    }

//    private Date parseDatum(String dateStr) throws ParseException {
//        // Lista formata koje prihvatamo
//        String[] formati = {
//            "dd.MM.yyyy",
//            "dd.MM.yyyy.",
//            "dd/MM/yyyy",
//            "yyyy-MM-dd",
//            "d.M.yyyy",
//            "dd. MM. yyyy."
//        };
//
//        for (String f : formati) {
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat(f);
//                sdf.setLenient(false); // strogo parsiranje
//                return sdf.parse(dateStr);
//            } catch (ParseException ignored) {
//                // pokušaj sledeći format
//            }
//        }
//
//        throw new ParseException("Nepoznat format datuma: " + dateStr, 0);
//    }
    private boolean proveraUnosa() {
        String ime = jTextFieldIME.getText().trim();
        String prezime = jTextFieldPREZIME.getText().trim();
        String datum = jTextFieldDAT_RODJ.getText().trim();
        String telefon = jTextFieldTEL_TAK.getText().trim();

        // 1. Provera da li su polja prazna
        if (ime.isEmpty() || prezime.isEmpty() || datum.isEmpty() || telefon.isEmpty()) {
            prikaziGresku();
            return false;
        }

        // 2. Provera regexa za ime i prezime (prvo veliko, ostalo slova)
        if (!ime.matches("^[A-ZŠĐČĆŽ][a-zšđčćžA-ZŠĐČĆŽ\\s]*$")
                || !prezime.matches("^[A-ZŠĐČĆŽ][a-zšđčćžA-ZŠĐČĆŽ\\s]*$")) {
            prikaziGresku();
            return false;
        }

        // 3. Provera datuma
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            sdf.setLenient(false);
            Date datRodj = sdf.parse(datum);
            Calendar min = Calendar.getInstance();
            min.set(1930, Calendar.JANUARY, 1);
            Date danas = new Date();
            if (datRodj.before(min.getTime()) || datRodj.after(danas)) {
                prikaziGresku();
                return false;
            }
        } catch (ParseException e) {
            prikaziGresku();
            return false;
        }

        // 4. Provera telefona (mora početi sa 06 i imati min 8 cifara)
        if (!telefon.matches("^06[0-9]{6,}$")) {
            prikaziGresku();
            return false;
        }

        // 5. Provera combo boxeva
        if (jComboBoxNAZIV_KAT.getSelectedIndex() == -1 || jComboBoxAMK_NAZIV.getSelectedIndex() == -1) {
            prikaziGresku();
            return false;
        }

        // Ako je sve u redu
        return true;
    }

// pomoćna metoda za prikaz greške
    private void prikaziGresku() {
        JOptionPane.showMessageDialog(this,
                "Neki podaci nisu uneti pravilno, proverite ih opet.",
                "Greška pri unosu",
                JOptionPane.ERROR_MESSAGE);
    }

}
