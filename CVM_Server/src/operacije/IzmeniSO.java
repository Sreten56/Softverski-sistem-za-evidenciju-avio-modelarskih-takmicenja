/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import model.Apstraktni_Domenski_Objekat;

/**
 *
 * @author sreck
 */
public class IzmeniSO<T extends Apstraktni_Domenski_Objekat> extends AbstractSystemOperation<T> {

    @Override
    protected void validacija(T object) throws Exception {
        if (object == null) {
            throw new Exception("Objekat za izmenu ne sme biti null!");
        }
    }


    @Override
    protected Object izvrsiOperaciju(T object) throws Exception {
        try {
            boolean uspesno = broker.update(object);

            if (uspesno) {
                broker.commit(); 
                return true;
            } else {
                broker.rollback(); 
                throw new Exception("Ažuriranje nije uspelo — nije pogođen nijedan red.");
            }

        } catch (Exception e) {
            broker.rollback();
            throw e;
        }
    }

//    @Override
//    protected Object izvrsiOperaciju(T object) throws Exception {
//        System.out.println("✏️ Ažuriranje objekta u bazi...");
//        boolean uspeh = broker.update(object);
//        if (uspeh) {
//            System.out.println("✅ Izmena uspešno završena!");
//        } else {
//            System.out.println("⚠️ Nije izmenjen nijedan red u bazi.");
//        }
//        return uspeh;
//    }
}


//    @Override
//    protected Object izvrsiOperaciju(T object) throws Exception {
//        return broker.update(object);
//    }
//    @Override
//    protected Object izvrsiOperaciju(T object) throws Exception {
//        boolean uspeh = broker.update(object);
//        if (uspeh) {
//            broker.commit();
//        } else {
//            broker.rollback();
//            throw new Exception("Ažuriranje nije uspelo - nije pogođen nijedan red u bazi.");
//        }
//        return true;
//    }