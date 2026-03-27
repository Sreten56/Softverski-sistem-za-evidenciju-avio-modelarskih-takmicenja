/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package forme;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;
import model.AMKlub;
import model.Takmicar;

/**
 *
 * @author Srecko
 */
public class Model_Tabele_Takmicar_Kat extends AbstractTableModel {

    // private List<Object[]> listaPodataka;
    private List<Takmicar> listaTakmicara;
    private List<AMKlub> listaAMK;
    private String[] kolone = {"Obelezje", "Datum rodjenja", "Avio Modelarski Klub"};

    public Model_Tabele_Takmicar_Kat() {
    }

    public Model_Tabele_Takmicar_Kat(List<Takmicar> listaTakmicara, List<AMKlub> listaAMK) {
//        if (listaTakmicara.size() != listaAMK.size()) {
//            throw new IllegalArgumentException("Liste takmičara i AM klubova moraju imati istu veličinu.");
//        }
        this.listaTakmicara = listaTakmicara;
        this.listaAMK = listaAMK;
    }

    @Override
    public int getRowCount() {
        return listaTakmicara.size();
    }

    @Override
    public int getColumnCount() {
        return kolone.length;
    }

//    @Override
//    public Object getValueAt(int rowIndex, int columnIndex) {
////        Object[] podaci = listaPodataka.get(rowIndex);
////        Takmicar takmicar = (Takmicar) podaci[0];
////        AMKlub amk = (AMKlub) podaci[2];
//
//        Takmicar tak = listaTakmicara.get(rowIndex);
//        AMKlub amk = listaAMK.get(rowIndex);
//        // BICE NA CIRILICI
//        //SimpleDateFormat sdf = new SimpleDateFormat("dd. MMM yyyy.", new Locale("sr", "RS"));
//
//        // Kreiranje mape meseci na latinici
//        String[] meseci = {"Jan", "Feb", "Mar", "Apr", "Maj", "Jun", "Jul", "Avg", "Sep", "Okt", "Nov", "Dec"};
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(tak.getDatumRodjenja());
//
//        int dan = cal.get(Calendar.DAY_OF_MONTH);
//        int mesec = cal.get(Calendar.MONTH);
//        int godina = cal.get(Calendar.YEAR);
//
//        // Formatiranje datuma sa mesecima na latinici
//        String formatiranDatum = String.format("%02d. %s %d.", dan, meseci[mesec], godina);
//
//        switch (columnIndex) {
//            case 0:
//                return tak.getObelezjeTakmicara();
//            case 1:
//                return formatiranDatum; // Ručno formatiran datum
//            case 2:
//                return amk.getNazivAMKluba();
//            default:
//                throw new AssertionError();
//        }
//    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Takmicar tak = listaTakmicara.get(rowIndex);
        AMKlub amk = listaAMK.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return tak.getObelezjeTakmicara();
            case 1:
                if (tak.getDatumRodjenja() != null) {
                    String[] meseci = {"Jan", "Feb", "Mar", "Apr", "Maj", "Jun", "Jul", "Avg", "Sep", "Okt", "Nov", "Dec"};
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(tak.getDatumRodjenja());
                    int dan = cal.get(Calendar.DAY_OF_MONTH);
                    int mesec = cal.get(Calendar.MONTH);
                    int godina = cal.get(Calendar.YEAR);
                    return String.format("%02d. %s %d.", dan, meseci[mesec], godina);
                } else {
                    return "Nepoznat datum";
                }
            case 2:
                return amk != null ? amk.getNazivAMKluba() : "Nepoznat klub";
            default:
                throw new AssertionError();
        }
    }

    @Override
    public String getColumnName(int column) {
        return kolone[column];
    }

    public Takmicar getTakmicarAt(int rowIndex) {
        return listaTakmicara.get(rowIndex);
    }

    public void osveziPodatke() {
        fireTableDataChanged();
    }

    public AMKlub getAmkAt(int rowIndex) {
        return listaAMK.get(rowIndex);
    }

}
