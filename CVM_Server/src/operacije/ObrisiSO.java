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
public class ObrisiSO<T extends Apstraktni_Domenski_Objekat> extends AbstractSystemOperation<T> {

    @Override
    protected void validacija(T object) throws Exception {
        if (object == null) {
            throw new Exception("Objekat za brisanje ne sme biti null!");
        }
    }

//    @Override
//    protected Object izvrsiOperaciju(T object) throws Exception {
//        System.out.println("🗑️ Brisanje objekta iz baze...");
//        boolean uspeh = broker.delete(object);
//        if (uspeh) {
//            System.out.println("✅ Objekat uspešno obrisan!");
//        } else {
//            System.out.println("⚠️ Objekat nije pronađen u bazi.");
//        }
//        return uspeh;
//    } 
    @Override
    protected Object izvrsiOperaciju(T object) throws Exception {
        boolean uspeh = broker.delete(object);
        if (uspeh) {
            broker.commit();
        } else {
            broker.rollback();
            throw new Exception("Brisanje nije uspelo - objekat ne postoji u bazi.");
        }
        return true;
    }
}
