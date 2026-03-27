/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package forme;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.CVM;

/**
 *
 * @author Srecko
 */
public class Model_Tabele_OstaliCVM extends AbstractTableModel {

    private List<CVM> listaCVMa;
    private String[] kolone = {"Naziv CVM-a", "Broj telefona", "E-mail", "Grad"};

    public Model_Tabele_OstaliCVM(List<CVM> listaCVMa) {
        this.listaCVMa = listaCVMa;
    }

    @Override
    public int getRowCount() {
        return listaCVMa.size();
    }

    @Override
    public int getColumnCount() {
        return kolone.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        CVM cvm = listaCVMa.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return cvm.getNazivCVM();
            case 1:
                return cvm.getBr_telefona();
            case 2:
                return cvm.getEmail();
            case 3:
                return cvm.getGrad();
            default:
                return "N/A";
        }

    }

    @Override
    public String getColumnName(int column) {
        return kolone[column];
    }

}
