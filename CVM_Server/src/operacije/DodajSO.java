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
public class DodajSO<T extends Apstraktni_Domenski_Objekat> extends AbstractSystemOperation<T> {

    @Override
    protected void validacija(T object) throws Exception {
        if (object == null) {
            throw new Exception("Objekat za dodavanje ne sme biti null!");
        }
    }

//    @Override
//    protected Object izvrsiOperaciju(T object) throws Exception {
//        boolean uspeh = broker.insert(object);
//        if (!uspeh) {
//            throw new Exception("Insert nije uspeo - nije pogođen nijedan red.");
//        }
//        return true;
//    }
    @Override
    protected Object izvrsiOperaciju(T object) throws Exception {
        boolean uspeh = broker.insert(object);
        if (uspeh) {
            broker.commit();
        } else {
            broker.rollback();
            throw new Exception("Insert nije uspeo - nije pogođen nijedan red.");
        }
        return true;
    }

    /*
    
     @Override
    protected void validacija(T object) throws Exception {
        if (object == null) {
            throw new Exception("Objekat za dodavanje ne sme biti null!");
        }
    }

    @Override
    protected Object izvrsiOperaciju(T object) throws Exception {
        System.out.println("🧩 Dodavanje objekta u bazu...");
        boolean uspeh = broker.insert(object);
        if (uspeh) {
            System.out.println("✅ Objekat uspešno dodat!");
        } else {
            System.out.println("⚠️ Dodavanje nije uspelo.");
        }
        return uspeh;
    }
    
     */
}
