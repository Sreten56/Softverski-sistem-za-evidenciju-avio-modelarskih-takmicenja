/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlerUI;

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author sreck
 */
public class PrikazDatumaIVremena extends JFrame {

    private JLabel lblDatumVreme;
    private Timer timer;

    public PrikazDatumaIVremena() {
        setTitle("Prikaz datuma i vremena");
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        lblDatumVreme = new JLabel("", SwingConstants.CENTER);
        lblDatumVreme.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(lblDatumVreme);

        // Pokreni osvežavanje
        pokreniOsvezavanje();

        setVisible(true);
    }

    private void pokreniOsvezavanje() {
        // Format datuma i vremena
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");

        // Swing Timer (osvežava svake 1 sekunde)
        timer = new Timer(1000, e -> {
            String trenutno = sdf.format(new Date());
            lblDatumVreme.setText(trenutno);
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PrikazDatumaIVremena());
    }
}
