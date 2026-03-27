/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlerUI;

import java.awt.Frame;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import komunikacija.Komunikacija;

/**
 *
 * @author sreck
 */
public class DatabaseConnectionMonitor extends Thread {

    private static boolean running = true;
    private static final int INTERVAL_MS = 1000; // proverava svaki sekundi

    @Override
    public void run() {
        while (running) {
            try {
                // proveri da li je veza aktivna
                if (!Komunikacija.getInstance().isConnected() || Komunikacija.getInstance().getSoket().isClosed()) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null,
                                "Veza sa bazom podataka je prekinuta!\n"
                                + "Molimo vas da restartujete aplikaciju.",
                                "Greška u konekciji", JOptionPane.ERROR_MESSAGE);
                        for (Frame frame : Frame.getFrames()) {
                            frame.dispose();
                        }
                        System.exit(0);
                    });

                }

                Thread.sleep(INTERVAL_MS);

            } catch (InterruptedException ex) {
                break; // prekid niti
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "️ Neočekivana greška u proveri konekcije!\n" + ex.getMessage(),
                        "Greška", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    }

    public static void stopMonitor() {
        running = false;
    }
}
