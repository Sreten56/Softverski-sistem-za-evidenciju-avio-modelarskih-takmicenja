/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import java.util.ArrayList;
import model.Apstraktni_Domenski_Objekat;

/**
 *
 * @author sreck
 */
public class VratiSveSO<T extends Apstraktni_Domenski_Objekat> extends AbstractSystemOperation<T> {

    @Override
    protected void validacija(T object) throws Exception {
        if (object == null) {
            throw new Exception("Objekat za selekciju ne sme biti null!");
        }
    }

    @Override
    protected Object izvrsiOperaciju(T object) throws Exception {
        System.out.println("Ucitavaju se svo objekte iz baze...");
        ArrayList<Apstraktni_Domenski_Objekat> rezultat = broker.select(object);
        System.out.println("Ucitavanje zavrxeno. Broj redova: " + rezultat.size());
        return rezultat;
    }
}
