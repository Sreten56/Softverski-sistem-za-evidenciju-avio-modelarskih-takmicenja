/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package forme;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Kategorija;

/**
 *
 * @author Srecko
 */
public class Model_Tabele_Kategorija extends AbstractTableModel {

    List<Kategorija> listaKategorija;
    private String[] kolone = {"ID", "Naziv", "Opis"};

    public Model_Tabele_Kategorija(List<Kategorija> listaKategorija) {
        this.listaKategorija = listaKategorija;
    }

    @Override
    public int getRowCount() {
        return listaKategorija.size();
    }

    @Override
    public int getColumnCount() {
        return kolone.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Kategorija kat = listaKategorija.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return kat.getIdKategorija();
            case 1:
                return kat.getNazivKategorija();
            case 2:
                return kat.getOpisKategorija();

            default:
                return "N/A";
        }
    }

    @Override
    public String getColumnName(int column) {
        return kolone[column];
    }
    
    

}
