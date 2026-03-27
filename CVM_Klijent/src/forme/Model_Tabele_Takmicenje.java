/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package forme;

import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Takmicenje;

/**
 *
 * @author sreck
 */
public class Model_Tabele_Takmicenje extends AbstractTableModel {

    private List<Takmicenje> lista;
    // private String[] kolone = {"Naziv takmičenja", "Organizator (CVM)", "Kategorija", "Lokacija", "Datum"};
    private String[] kolone = {"Naziv takmicenja", "Organizator (CVM)", "Kategorija", "Lokacija", "Datum"};

    public Model_Tabele_Takmicenje(List<Takmicenje> lista) {
        this.lista = lista;
    }

    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public int getColumnCount() {
        return kolone.length;
    }

    @Override
    public String getColumnName(int column) {
        return kolone[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Takmicenje t = lista.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return t.getNazivTakmicenja();
            case 1:
                return t.getCvm().getNazivCVM();
            case 2:
                return t.getKat().getNazivKategorija();
            case 3:
                return t.getLokacija(); 
            case 4:
                return new SimpleDateFormat("dd. MM. yyyy.").format(t.getDatum());
            default:
                return null;
        }
    }

    public Takmicenje getTakmicenjeAt(int rowIndex) {
        return lista.get(rowIndex);
    }
}
