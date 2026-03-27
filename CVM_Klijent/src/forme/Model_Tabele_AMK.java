/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package forme;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.AMKlub;
import model.Mesto;

/**
 *
 * @author Srecko
 */
public class Model_Tabele_AMK extends AbstractTableModel {

    List<AMKlub> listaAMK = new ArrayList<>();
    List<Mesto> listaMesta = new ArrayList<>();
    String[] kolone = {"Naziv", "Broj telefona", "Email", "Mesto"};

    public Model_Tabele_AMK(List<AMKlub> listaAMK, List<Mesto> listaMesta) {
        this.listaAMK = listaAMK;
        this.listaMesta = listaMesta;
    }


    @Override
    public int getRowCount() {
        return listaAMK.size();
    }

    @Override
    public int getColumnCount() {
        return kolone.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        AMKlub amk = listaAMK.get(rowIndex);
        Mesto mesto = listaMesta.get(rowIndex);

        switch (columnIndex) {

            case 0:
                return amk.getNazivAMKluba();
            case 1:
                return amk.getBrojTelefona();
            case 2:
                return amk.getEmailAMK();
            case 3:
                return mesto.getNazivMesto();

            default:
                return "N/A";
        }
        
    }

    @Override
    public String getColumnName(int column) {
        return kolone[column];
    }

    public AMKlub getAMKlubAt(int rowIndex) {
        return listaAMK.get(rowIndex);
    }

    public void osvezi_tabelu() {
        fireTableDataChanged();
    }
}
