/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package forme;

import controlerUI.ThemeManager;
import forme.Model_Tabele_Takmicar_Kat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import komunikacija.Komunikacija;
import model.AMKlub;
import model.Kategorija;
import model.Mesto;
import model.Takmicar;
import niti.Osvezi_Tabele;
import operacije.Operacije;
import transfer.Klijentski_Zahtev;
import transfer.Serverski_Odgovor;

/**
 *
 * @author Srecko
 */
public class Forma_Takmicari extends javax.swing.JFrame {

    Forma_Takmicari ft;

    public static List<AMKlub> LIST_AMK = new ArrayList<>();
    public static List<Takmicar> LIST_TAK = new ArrayList<>();

    boolean bazaNedostupna;

    /**
     * Creates new form Forma_Takmicari
     */
    public Forma_Takmicari() {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ThemeManager.applyTheme(this);

        List<Takmicar> listaTakmicara = new ArrayList<>();
        List<Kategorija> listaKategorija = new ArrayList<>();
        List<AMKlub> listaAMK = new ArrayList<>();

        Klijentski_Zahtev kz;
        Serverski_Odgovor so;

        osveziTabelu();

        Osvezi_Tabele.dodajZadatak(() -> osveziTabelu());
        Osvezi_Tabele.pokreni();

        kz = new Klijentski_Zahtev(Operacije.VRATI_SVE_KATEGORIJE, null);
        Komunikacija.getInstance().posalji_zahtev(kz);
        so = Komunikacija.getInstance().primi_odgovor();

        if (so != null && so.getOdgovor() instanceof List) {
            listaKategorija = (List<Kategorija>) so.getOdgovor();
            if (!listaKategorija.isEmpty()) {
                Model_Tabele_Kategorija mtk = new Model_Tabele_Kategorija(listaKategorija);
                jTableKATEGORIJE.setModel(mtk);
            } else {
                JOptionPane.showMessageDialog(this, "Lista kategorija je prazna!", "Greška!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Greška prilikom dobijanja kategorija!", "Greška!", JOptionPane.ERROR_MESSAGE);
        }

        // OTVARANJE FORME ZA DETALJE NAKON KLIKA NA ODREDJENOG TAKMICARA
        jTableTAKMICARI.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int selectedRow = jTableTAKMICARI.getSelectedRow(); 
                if (selectedRow != -1) { 
                    Takmicar selektovaniTakmicar
                            = ((Model_Tabele_Takmicar_Kat) jTableTAKMICARI.getModel())
                                    .getTakmicarAt(selectedRow); // Dobijanje objekta Takmicar

                    // Otvaranje  forme
                    Forma_DetaljiTakmicara formaDetalji = new Forma_DetaljiTakmicara(Forma_Takmicari.this, selektovaniTakmicar); ////////////////////////////////////////////////
                    formaDetalji.setVisible(true);
                }
            }
        });                             // STIGLI SMO DO PRIKUPLJANJA ID-A TAKMICARA ID DA SE PROSLEDI U ONU FORMU RADI DALJIH RADNJI...

//        // Inicijalizacija liste kao List<Object[]>
//        List<Object[]> svePotrebneINFO = new ArrayList<>();
//
//        kz = new Klijentski_Zahtev(Operacije.VRATI_SVE_POTREBNE_INFO_ZA_TAKMICARA_KAT_I_AMK, null);
//        Komunikacija.getInstance().posalji_zahtev(kz);
//        so = Komunikacija.getInstance().primi_odgovor();
//
//        if (so != null) {
//            if (so.getOdgovor() instanceof List) {
//                svePotrebneINFO = (List<Object[]>) so.getOdgovor();
//                if (!svePotrebneINFO.isEmpty()) {
//                    listaTakmicara = new ArrayList<>();
//                    listaAMK = new ArrayList<>();
//                    listaKategorija = new ArrayList<>();
//
//                    for (Object[] info : svePotrebneINFO) {
//                        Takmicar tak = (Takmicar) info[0];
//                        // Kategorija kat = (Kategorija) info[1];
//                        AMKlub amk = (AMKlub) info[2];
//
//                        listaTakmicara.add(tak);
//                        //listaKategorija.add(kat);
//                        listaAMK.add(amk);
//                        
//                        LIST_AMK = listaAMK;
//                        LIST_TAK = listaTakmicara;
//                    }
//
//                    Model_Tabele_Takmicar_Kat mttk = new Model_Tabele_Takmicar_Kat(listaTakmicara, listaAMK);
//                    jTableTAKMICARI.setModel(mttk);
//
////                    Model_Tabele_Kategorija mtk = new Model_Tabele_Kategorija(listaKategorija);
////                    jTableKATEGORIJE.setModel(mtk);
//                } else {
//                    JOptionPane.showMessageDialog(this, "Lista podataka je prazna!", "Greška!", JOptionPane.ERROR_MESSAGE);
//                }
//            } else {
//                JOptionPane.showMessageDialog(this, "Odgovor nije u očekivanom formatu!", "Greška!", JOptionPane.ERROR_MESSAGE);
//            }
//        } else {
//            JOptionPane.showMessageDialog(this, "Greška prilikom dobijanja podataka", "Greška!", JOptionPane.ERROR_MESSAGE);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableTAKMICARI = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableKATEGORIJE = new javax.swing.JTable();
        jButtonDODAJ_TAKMICARA = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Takmičari i Kategorije");

        jTableTAKMICARI.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableTAKMICARI.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableTAKMICARIMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTableTAKMICARI);

        jButton1.setText("Odustani");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Osnovni podaci o takmicarima");

        jTableKATEGORIJE.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTableKATEGORIJE);

        jButtonDODAJ_TAKMICARA.setText("Dodaj takmicara");
        jButtonDODAJ_TAKMICARA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDODAJ_TAKMICARAActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 263, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(59, 59, 59))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonDODAJ_TAKMICARA)
                            .addComponent(jLabel1))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(jButtonDODAJ_TAKMICARA)
                .addGap(31, 31, 31)
                .addComponent(jButton1)
                .addGap(34, 34, 34))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTableTAKMICARIMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableTAKMICARIMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableTAKMICARIMousePressed

    private void jButtonDODAJ_TAKMICARAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDODAJ_TAKMICARAActionPerformed

        Forma_DetaljiTakmicara formaDetalji = new Forma_DetaljiTakmicara(this, null); /////////////////////////////////////////////////////
        formaDetalji.setVisible(true);
    }//GEN-LAST:event_jButtonDODAJ_TAKMICARAActionPerformed

//    public void osveziTabelu() {
//        Model_Tabele_Takmicar_Kat mttk = new Model_Tabele_Takmicar_Kat(LIST_TAK, LIST_AMK);
//        jTableTAKMICARI.setModel(mttk);
//        
////        Model_Tabele_Takmicar_Kat mttk = (Model_Tabele_Takmicar_Kat) jTableTAKMICARI.getModel();
////        mttk.osveziPodatke(); 
//    }
//    public void osveziTabelu() {
//        
//        List<Takmicar> listaTakmicara = new ArrayList<>();
//        List<Kategorija> listaKategorija = new ArrayList<>();
//        List<AMKlub> listaAMK = new ArrayList<>();
//        
//        try {
//            // Kreiraj zahtev za preuzimanje svih potrebnih informacija
//            Klijentski_Zahtev kz = new Klijentski_Zahtev(Operacije.VRATI_SVE_POTREBNE_INFO_ZA_TAKMICARA_KAT_I_AMK, null);
//            Komunikacija.getInstance().posalji_zahtev(kz);
//
//            // Primi odgovor od servera
//            Serverski_Odgovor so = Komunikacija.getInstance().primi_odgovor();
//
//            if (so != null && so.getOdgovor() instanceof List) {
//                List<Object[]> svePotrebneINFO = (List<Object[]>) so.getOdgovor();
//
//                if (!svePotrebneINFO.isEmpty()) {
//                    listaTakmicara.clear();
//                    listaAMK.clear();
//
//                    for (Object[] info : svePotrebneINFO) {
//                        Takmicar tak = (Takmicar) info[0];
//                        AMKlub amk = (AMKlub) info[2];
//
//                        listaTakmicara.add(tak);
//                        listaAMK.add(amk);
//                    }
//
//                    // Postavi novi model sa osveženim podacima
//                    Model_Tabele_Takmicar_Kat mttk = new Model_Tabele_Takmicar_Kat(listaTakmicara, listaAMK);
//                    jTableTAKMICARI.setModel(mttk);
//
//                    // Ažuriranje prikaza
//                    jTableTAKMICARI.revalidate(); // Prisiljava da se tabela osveži
//                    jTableTAKMICARI.repaint();    // Prikazuje novi sadržaj tabele
//                } else {
//                    JOptionPane.showMessageDialog(this, "Lista podataka je prazna!", "Greška!", JOptionPane.ERROR_MESSAGE);
//                }
//            } else {
//                JOptionPane.showMessageDialog(this, "Greška prilikom preuzimanja podataka sa servera!", "Greška!", JOptionPane.ERROR_MESSAGE);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Došlo je do greške prilikom osvežavanja tabele: " + e.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
//        }
//    }
    public void osveziTabelu() {
        try {
            // 1. TAKMIČARI
            Klijentski_Zahtev kzTak = new Klijentski_Zahtev(Operacije.VRATI_SVE_TAKMICARE, null);
            Komunikacija.getInstance().posalji_zahtev(kzTak);
            Serverski_Odgovor soTak = Komunikacija.getInstance().primi_odgovor();
            List<Takmicar> listaTakmicara = (List<Takmicar>) soTak.getOdgovor();

            // 2. AM KLUBOVI
            Klijentski_Zahtev kzAmk = new Klijentski_Zahtev(Operacije.VRATI_SVE_AMK, null);
            Komunikacija.getInstance().posalji_zahtev(kzAmk);
            Serverski_Odgovor soAmk = Komunikacija.getInstance().primi_odgovor();
            List<AMKlub> listaAMK = (List<AMKlub>) soAmk.getOdgovor();

            // 3. MAPIRANJE AMK ID → objekat
            Map<Integer, AMKlub> mapaAmk = new HashMap<>();
            for (AMKlub amk : listaAMK) {
                mapaAmk.put(amk.getIdAMK(), amk);
            }

            // 4. Priprema liste AMK za tabelu (paralelno sa takmičarima)
            List<AMKlub> listaZaTabelu = new ArrayList<>();
            for (Takmicar t : listaTakmicara) {
                AMKlub amk = mapaAmk.get(t.getAMKid());
                listaZaTabelu.add(amk);
            }

            // 5. Setuj model za tabelu takmičara
            Model_Tabele_Takmicar_Kat mttk = new Model_Tabele_Takmicar_Kat(listaTakmicara, listaZaTabelu);
            jTableTAKMICARI.setModel(mttk);
            jTableTAKMICARI.revalidate();
            jTableTAKMICARI.repaint();

            bazaNedostupna = false;

        } catch (Exception e) {
            e.printStackTrace();
            if (!bazaNedostupna) {
                JOptionPane.showMessageDialog(this,
                        "Veza sa bazom je izgubljena! Aplikacija će se zatvoriti.",
                        "Greška!", JOptionPane.ERROR_MESSAGE);
                bazaNedostupna = true;
                this.dispose();
                System.exit(0); // prekini rad
            }
        }
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonDODAJ_TAKMICARA;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableKATEGORIJE;
    private javax.swing.JTable jTableTAKMICARI;
    // End of variables declaration//GEN-END:variables
}
