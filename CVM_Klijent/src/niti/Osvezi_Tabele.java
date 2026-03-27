/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package niti;

import forme.Forma_AM_Klubovi;
import forme.Forma_TAKMICENJE;
import forme.Forma_Takmicari;
import forme.Glavna_forma;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author sreck
 */

public class Osvezi_Tabele {

    private static final int INTERVAL = 1500; // 1.5 sekund
    private static Timer timer;
    private static final List<Runnable> zadaci = new ArrayList<>();

    public static void dodajZadatak(Runnable r) {
        zadaci.add(r);
    }

    public static void pokreni() {
        if (timer == null) {
            timer = new Timer(INTERVAL, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (Runnable r : zadaci) {
                        SwingUtilities.invokeLater(r); //  za Swing
                    }
                }
            });
            timer.start();
        }
    }

    public static void zaustavi() {
        if (timer != null) {
            timer.stop();
            timer = null;
            zadaci.clear();
        }
    }


//public class Osvezi_Tabelu_Nit extends Thread{
    
//    private Glavna_forma gf;
//    private Forma_Takmicari ft;
//    private Forma_AM_Klubovi famk;
//    private Forma_TAKMICENJE ftakm;
//    private boolean kraj = false;
//    
//    
//
//    @Override
//    public void run() {
//        
//        while (!kraj) {            
//            
//            try {
//                gf.popuniFormu();
//                ft.osveziTabelu();
//                famk.PRIKAZI_PODATKE_TABELE();
//                ftakm.osveziTabele();
//                Thread.sleep(1500);
//                
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Osvezi_Tabelu_Nit.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            
//        }
//        
//    }
//    
//    private void zaustavi(){
//        kraj = true;
//    }
    
    
    
    
}
