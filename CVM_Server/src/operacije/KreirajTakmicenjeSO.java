/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import java.util.List;
import model.Prijava;
import model.Takmicar;
import model.Takmicenje;

/**
 *
 * @author sreck
 */
public class KreirajTakmicenjeSO extends AbstractSystemOperation<Takmicenje> {

    private List<Takmicar> prijavljeni;

    public KreirajTakmicenjeSO(List<Takmicar> prijavljeni) {
        this.prijavljeni = prijavljeni;
    }

    @Override
    protected void validacija(Takmicenje takmicenje) throws Exception {
        if (takmicenje == null) {
            throw new Exception("Takmičenje ne sme biti null!");
        }
        if (prijavljeni == null || prijavljeni.isEmpty()) {
            throw new Exception("Lista prijavljenih takmičara ne sme biti prazna!");
        }
    }

    @Override
    protected Object izvrsiOperaciju(Takmicenje takmicenje) throws Exception {
        // Ubaci takmicenje i preuzmi ID
        int idTakmicenje = broker.insertReturnID(takmicenje);
        takmicenje.setIdTakmicenje(idTakmicenje);

        // Ubacivanje svi prijava za to takmicenje   TRANSAKCIJA
        for (Takmicar t : prijavljeni) {
            Prijava p = new Prijava();
            p.setTakmicenje(takmicenje);
            p.setTakmicar(t);
            broker.insert(p);
        }

        return true;
    }

}
