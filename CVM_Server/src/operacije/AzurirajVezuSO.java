/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import java.util.ArrayList;
import model.Apstraktni_Domenski_Objekat;
import model.VcvmCVM;

/**
 *
 * @author sreck
 */
public class AzurirajVezuSO extends AbstractSystemOperation<VcvmCVM> {

    @Override
    protected void validacija(VcvmCVM veza) throws Exception {
        if (veza == null)
            throw new Exception("Veza ne sme biti null!");
        if (veza.getCvm() == null || veza.getVrsta() == null)
            throw new Exception("Veza mora imati i CVM i Vrstu!");
    }

    @Override
    protected Object izvrsiOperaciju(VcvmCVM veza) throws Exception {
        int idCVM = veza.getCvm().getIdCVM();

        //  Obrisi sve postojece veze za taj CVM (da ne ostanu duplikati)
        String sqlDelete = "DELETE FROM vcvm_cvm WHERE CVMid = " + idCVM;
        broker.getConnection().createStatement().executeUpdate(sqlDelete);

        //  Ubaci novu vezu
        boolean inserted = broker.insert(veza);

        //  Potvrdi transakciju
        if (inserted) {
            broker.commit();
        } else {
            broker.rollback();
        }

        return inserted;
    }

//    @Override
//    protected void validacija(VcvmCVM object) throws Exception {
//        if (object == null) {
//            throw new Exception("Veza ne sme biti null!");
//        }
//        if (object.getCvm() == null || object.getVrsta() == null) {
//            throw new Exception("Veza mora imati i CVM i VrstuCVM!");
//        }
//    }
//
//    @Override
//    protected Object izvrsiOperaciju(VcvmCVM object) throws Exception {
//        // 1️⃣ Proveri da li već postoji u bazi
//        ArrayList<Apstraktni_Domenski_Objekat> sveVeze = broker.select(object);
//        boolean postoji = false;
//
//        for (Apstraktni_Domenski_Objekat ado : sveVeze) {
//            VcvmCVM postojeca = (VcvmCVM) ado;
//            if (postojeca.getCvm().getIdCVM() == object.getCvm().getIdCVM()
//                    && postojeca.getVrsta().getIdVrstaCVM() == object.getVrsta().getIdVrstaCVM()) {
//                postoji = true;
//                break;
//            }
//        }
//
//        // 2️⃣ Ako postoji → update; ako ne → insert
//        if (postoji) {
//            return broker.update(object);
//        } else {
//            return broker.insert(object);
//        }
//    }
}
