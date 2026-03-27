/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

/**
 *
 * @author Srecko
 */
public class Operacije {

    // --------------------------------------------------------
    // CVM OPERACIJE [ 7 ]
    // --------------------------------------------------------
    public static final int PRIJAVA = 1;                         // Login CVM
    public static final int REGISTRACIJA = 2;                    // Registracija novog CVM-a
    public static final int AZURIRAJ_CVM = 21;                   // Izmena postojećeg CVM-a
    public static final int VRATI_SVE_CVM = 5;                   // Prikaz svih CVM-ova
    public static final int VRATI_SVE_VRSTE_CVM = 22;            // Lista vrsta CVM-a
    public static final int VRATI_CVM_DETALJE = 32;              // Detalji o konkretnom CVM-u
    public static final int VRATI_SVE_PODATKE_CVM_VRSTA = 7;     

    // --------------------------------------------------------
    // AMK OPERACIJE [ 7 ]
    // --------------------------------------------------------
    public static final int VRATI_SVE_AMK = 10;                  // Lista AM klubova
    public static final int DODAJ_AMK = 18;                      // Unos novog AM kluba
    public static final int AZURIRAJ_AMK = 19;                   // Izmena AM kluba
    public static final int OBRISI_AMK = 20;                     // Brisanje AM kluba
    public static final int VRATI_SVE_INFO_AMK = 15;             // Sve informacije o AM klubovima
    public static final int VRATI_SVA_MESTA = 16;                // Lista mesta
    public static final int VRATI_NAJVECI_ID_AMK = 17;           // Sledeći ID za AMK

    // --------------------------------------------------------
    // TAKMIČAR OPERACIJE [ 5 ]
    // --------------------------------------------------------
    public static final int DODAJ_TAKMICARA = 12;                // Dodavanje novog takmičara
    public static final int AZURIRAJ_TAKMICARA = 11;             // Ažuriranje takmičara
    public static final int OBRISI_TAKMICARA = 14;               // Brisanje takmičara
    public static final int VRATI_SVE_TAKMICARE = 24;            // Lista svih takmičara
    public static final int VRATI_NAJVECI_ID_TAKMICARA = 13;     // Sledeći ID za takmičara

    // --------------------------------------------------------
    // TAKMIČENJE OPERACIJE [ 6 ]
    // --------------------------------------------------------
    public static final int KREIRAJ_TAKMICENJE = 30;             // Kreiranje takmičenja
    public static final int AZURIRAJ_TAKMICENJE = 28;            // Izmena takmičenja
    public static final int OBRISI_TAKMICENJE = 29;              // Brisanje takmičenja
    public static final int VRATI_SVA_TAKMICENJA = 25;           // Lista svih takmičenja
    public static final int VRATI_PRIJAVLJENE_TAKMICARE = 26;    // Takmičari prijavljeni za takmičenje
    public static final int VRATI_TAKMICARE_IZ_KATEGORIJE = 27;  // Takmičari u određenoj kategoriji

    // --------------------------------------------------------
    // ZAJEDNIČKE OPERACIJE / OSTALO [ 2 ]
    // --------------------------------------------------------
    public static final int VRATI_SVE_KATEGORIJE = 9;            // Lista kategorija
    public static final int AZURIRAJ_VEZU = 23;                  // Ažuriranje veze između entiteta
}

