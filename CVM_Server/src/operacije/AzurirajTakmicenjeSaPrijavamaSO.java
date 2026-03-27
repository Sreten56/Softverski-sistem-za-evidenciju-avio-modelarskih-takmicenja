/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import baza.DB_Broker;
import java.util.List;
import model.Prijava;
import model.Takmicar;
import model.Takmicenje;

/**
 *
 * @author sreck
 */
public class AzurirajTakmicenjeSaPrijavamaSO extends AbstractSystemOperation<Takmicenje> {

    private final List<Takmicar> prijavljeni;

    public AzurirajTakmicenjeSaPrijavamaSO(List<Takmicar> prijavljeni) {
        this.prijavljeni = prijavljeni;
    }

    @Override
    protected void validacija(Takmicenje tak) throws Exception {
        if (tak == null) {
            throw new Exception("Takmičenje ne sme biti null!");
        }
        if (prijavljeni == null) {
            throw new Exception("Lista prijavljenih ne sme biti null!");
        }
    }

    @Override
    protected Object izvrsiOperaciju(Takmicenje tak) throws Exception {
        try {
            DB_Broker broker = DB_Broker.getInstance();

            //  Ayuriraj osnovne podatke o takmicemju
            broker.update(tak);

            //  Obrisi postojece prijave
            Prijava p = new Prijava();
            p.setTakmicenje(tak);
            broker.delete(p);

            //  Dodaj nove prijave     pd TRANSAKCIJOM
            for (Takmicar t : prijavljeni) {
                Prijava nova = new Prijava();
                nova.setTakmicenje(tak);
                nova.setTakmicar(t);
                broker.insert(nova);
            }

            broker.commit();
            return true;

        } catch (Exception e) {
            DB_Broker.getInstance().rollback();
            throw new Exception("Greška pri ažuriranju takmičenja: " + e.getMessage());
        }
    }
}
