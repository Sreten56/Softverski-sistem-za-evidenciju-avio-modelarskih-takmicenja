/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import model.Prijava;
import model.Takmicenje;

/**
 *
 * @author sreck
 */
public class ObrisiTakmicenjeSO extends AbstractSystemOperation<Takmicenje> {

    @Override
    protected void validacija(Takmicenje takmicenje) throws Exception {
        if (takmicenje == null) {
            throw new Exception("Takmičenje ne sme biti null!");
        }
    }

    @Override
    protected Object izvrsiOperaciju(Takmicenje takmicenje) throws Exception {
        // Obrisi sve prijave vezane za takmicemje
        Prijava p = new Prijava();
        p.setTakmicenje(takmicenje);
        broker.delete(p);

        //  Obrisi takmicenje
        boolean uspesno = broker.delete(takmicenje);
        if (!uspesno) {
            throw new Exception("Takmičenje nije pronađeno za brisanje.");
        }

        return true;
    }
}
