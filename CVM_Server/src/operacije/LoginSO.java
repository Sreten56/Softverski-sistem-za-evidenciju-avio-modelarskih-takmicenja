/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import java.util.ArrayList;
import model.Apstraktni_Domenski_Objekat;
import model.CVM;

/**
 *
 * @author sreck
 */
public class LoginSO extends AbstractSystemOperation<CVM> {

    @Override
    protected void validacija(CVM object) throws Exception {
        if (object == null || object.getEmail() == null || object.getLozinka() == null) {
            throw new Exception("Email i lozinka ne smeju biti null!");
        }
    }

    @Override
    protected Object izvrsiOperaciju(CVM object) throws Exception {
        ArrayList<Apstraktni_Domenski_Objekat> lista = broker.select(new CVM());

        for (Apstraktni_Domenski_Objekat ado : lista) {
            CVM c = (CVM) ado;
            if (c.getEmail().equals(object.getEmail()) && c.getLozinka().equals(object.getLozinka())) {
                return c; // korisnik 
            }
        }
        return null; 
    }
}
