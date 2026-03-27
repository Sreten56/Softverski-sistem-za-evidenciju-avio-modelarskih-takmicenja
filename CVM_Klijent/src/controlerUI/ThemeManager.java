/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlerUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;

/**
 *
 * @author sreck
 */
public class ThemeManager {

    private static boolean darkMode = false;

    public static void toggleTheme() {
        darkMode = !darkMode;
        for (Window window : Window.getWindows()) {
            applyTheme(window);
        }
    }

    public static void applyTheme(Window window) {
        if (!(window instanceof JFrame) && !(window instanceof JDialog)) {
            return;
        }

        Container contentPane = (window instanceof JFrame)
                ? ((JFrame) window).getContentPane()
                : ((JDialog) window).getContentPane();

        // Boje
        Color defaultBg = UIManager.getColor("Panel.background"); // sistemska pozadina ova krem boja 
        Color bg = darkMode ? new Color(30, 30, 30) : defaultBg;
        Color fg = darkMode ? Color.WHITE : Color.BLACK;
        Color textFieldBg = darkMode ? new Color(50, 50, 50) : Color.WHITE;
        Color textFieldFg = Color.BLACK;
        Color tableHeaderBg = darkMode ? new Color(50, 50, 50) : UIManager.getColor("TableHeader.background");
        Color tableHeaderFg = darkMode ? Color.WHITE : UIManager.getColor("TableHeader.foreground");

        contentPane.setBackground(bg);

        for (Component c : contentPane.getComponents()) {
            updateComponentTheme(c, bg, fg, textFieldBg, textFieldFg, tableHeaderBg, tableHeaderFg);
        }

        window.repaint();
    }

    private static void updateComponentTheme(Component c, Color bg, Color fg,
            Color textFieldBg, Color textFieldFg,
            Color thBg, Color thFg) {
        if (c instanceof JPanel) {
            c.setBackground(bg);
            for (Component sub : ((JPanel) c).getComponents()) {
                updateComponentTheme(sub, bg, fg, textFieldBg, textFieldFg, thBg, thFg);
            }
        } else if (c instanceof JLabel || c instanceof JButton) {
            c.setForeground(fg);
            c.setBackground(bg);
        } else if (c instanceof JTextField tf) {
            tf.setBackground(textFieldBg);
            tf.setForeground(textFieldFg);
            tf.setCaretColor(Color.BLACK);
        } else if (c instanceof JTable table) {
            table.setBackground(bg);
            table.setForeground(fg);
            table.setGridColor(darkMode ? new Color(80, 80, 80) : Color.LIGHT_GRAY);
            table.setSelectionBackground(darkMode ? new Color(70, 70, 70) : new Color(184, 207, 229));
            table.setSelectionForeground(Color.WHITE);

            JTableHeader header = table.getTableHeader();
            header.setBackground(thBg);
            header.setForeground(thFg);
        } else if (c instanceof JScrollPane sp) {
            sp.getViewport().getView().setBackground(bg);
            sp.getViewport().getView().setForeground(fg);
        } else {
            c.setBackground(bg);
            c.setForeground(fg);
        }
    }

    public static boolean isDarkMode() {
        return darkMode;
    }

//    private static boolean darkMode = false;
//
//    public static void toggleTheme() {
//        darkMode = !darkMode;
//        for (Window window : Window.getWindows()) {
//            applyTheme(window);
//        }
//    }
//
//    public static void applyTheme(Window window) {
//        if (!(window instanceof JFrame) && !(window instanceof JDialog)) {
//            return;
//        }
//
//        Container contentPane;
//        if (window instanceof JFrame frame) {
//            contentPane = frame.getContentPane();
//        } else {
//            contentPane = ((JDialog) window).getContentPane();
//        }
//
//        // boje
//        Color bg = darkMode ? new Color(30, 30, 30) : Color.WHITE;
//        Color fg = darkMode ? Color.WHITE : Color.BLACK;
//        Color textFieldBg = darkMode ? new Color(50, 50, 50) : Color.WHITE;
//        Color textFieldFg = Color.BLACK; // ✅ tekst uvek crn i vidljiv
//        Color tableHeaderBg = darkMode ? new Color(50, 50, 50) : new Color(240, 240, 240);
//        Color tableHeaderFg = darkMode ? Color.WHITE : Color.BLACK;
//
//        contentPane.setBackground(bg);
//
//        for (Component c : contentPane.getComponents()) {
//            updateComponentTheme(c, bg, fg, textFieldBg, textFieldFg, tableHeaderBg, tableHeaderFg);
//        }
//
//        window.repaint();
//    }
//
//    private static void updateComponentTheme(
//            Component c,
//            Color bg, Color fg,
//            Color textFieldBg, Color textFieldFg,
//            Color thBg, Color thFg
//    ) {
//        if (c instanceof JPanel) {
//            c.setBackground(bg);
//            c.setForeground(fg);
//            for (Component sub : ((JPanel) c).getComponents()) {
//                updateComponentTheme(sub, bg, fg, textFieldBg, textFieldFg, thBg, thFg);
//            }
//        } else if (c instanceof JLabel || c instanceof JButton) {
//            c.setBackground(bg);
//            c.setForeground(fg);
//        } else if (c instanceof JTextField tf) {
//            tf.setBackground(textFieldBg);
//            tf.setForeground(textFieldFg); // ✅ uvek crn tekst
//            tf.setCaretColor(Color.BLACK); // ✅ kursor se vidi
//        } else if (c instanceof JTable table) {
//            table.setBackground(bg);
//            table.setForeground(fg);
//            table.setGridColor(darkMode ? new Color(80, 80, 80) : Color.LIGHT_GRAY);
//            table.setSelectionBackground(darkMode ? new Color(70, 70, 70) : new Color(184, 207, 229));
//            table.setSelectionForeground(Color.WHITE);
//
//            JTableHeader header = table.getTableHeader();
//            header.setBackground(thBg);
//            header.setForeground(thFg);
//        } else if (c instanceof JScrollPane sp) {
//            sp.getViewport().getView().setBackground(bg);
//            sp.getViewport().getView().setForeground(fg);
//        } else {
//            c.setBackground(bg);
//            c.setForeground(fg);
//        }
//    }
//
//    public static boolean isDarkMode() {
//        return darkMode;
//    }
//    private static boolean darkMode = false;
//
//    public static void toggleTheme() {
//        darkMode = !darkMode;
//        for (Frame frame : Frame.getFrames()) {
//            applyTheme(frame);
//        }
//    }
//
//    public static void applyTheme(Window window) {
//        if (!(window instanceof JFrame)) return;
//
//        JFrame frame = (JFrame) window;
//        Container contentPane = frame.getContentPane();
//
//        // boje
//        Color bg = darkMode ? new Color(30, 30, 30) : Color.WHITE;
//        Color fg = darkMode ? Color.WHITE : Color.BLACK;
//        Color tableHeaderBg = darkMode ? new Color(50, 50, 50) : new Color(240, 240, 240);
//        Color tableHeaderFg = darkMode ? Color.WHITE : Color.BLACK;
//
//        // pozadina cele forme
//        contentPane.setBackground(bg);
//
//        // rekurzivno prođi kroz sve komponente
//        for (Component c : contentPane.getComponents()) {
//            updateComponentTheme(c, bg, fg, tableHeaderBg, tableHeaderFg);
//        }
//
//        frame.repaint();
//    }
//
//    private static void updateComponentTheme(Component c, Color bg, Color fg, Color thBg, Color thFg) {
//        if (c instanceof JPanel) {
//            c.setBackground(bg);
//            c.setForeground(fg);
//            for (Component sub : ((JPanel) c).getComponents()) {
//                updateComponentTheme(sub, bg, fg, thBg, thFg);
//            }
//        } else if (c instanceof JLabel || c instanceof JButton || c instanceof JTextField) {
//            c.setBackground(bg);
//            c.setForeground(fg);
//        } else if (c instanceof JTable table) {
//            table.setBackground(bg);
//            table.setForeground(fg);
//            table.setGridColor(darkMode ? new Color(80, 80, 80) : Color.LIGHT_GRAY);
//            table.setSelectionBackground(darkMode ? new Color(70, 70, 70) : new Color(184, 207, 229));
//            table.setSelectionForeground(Color.WHITE);
//
//            // ✅ reši problem zaglavlja
//            JTableHeader header = table.getTableHeader();
//            header.setBackground(thBg);
//            header.setForeground(thFg);
//        } else if (c instanceof JScrollPane sp) {
//            sp.getViewport().getView().setBackground(bg);
//            sp.getViewport().getView().setForeground(fg);
//        } else {
//            c.setBackground(bg);
//            c.setForeground(fg);
//        }
//    }
//
//    public static boolean isDarkMode() {
//        return darkMode;
//    }
    ////////////////
    //
    //
    //
    ///////////////
//    private static boolean darkMode = false;
//
//    // Pozivaš ovo kad klikneš na dugme "Dark Mode"
//    public static void toggleTheme() {
//        darkMode = !darkMode;
//        applyThemeToAllFrames();
//    }
//
//    public static boolean isDarkMode() {
//        return darkMode;
//    }
//
//    // ✅ Primeni temu na sve otvorene prozore
//    public static void applyThemeToAllFrames() {
//    for (Frame frame : Frame.getFrames()) {
//        if (frame.isVisible()) {
//            if (frame instanceof JFrame jFrame) {
//                applyThemeToContainer(jFrame.getContentPane());
//                SwingUtilities.updateComponentTreeUI(jFrame);
//            } else {
//                applyThemeToContainer(frame);
//                SwingUtilities.updateComponentTreeUI(frame);
//            }
//        }
//    }
//}
//
//
//    // ✅ Primeni boje rekurzivno na sve komponente
//    private static void applyThemeToContainer(Container container) {
//        Color bg = darkMode ? new Color(45, 45, 45) : Color.WHITE;
//        Color fg = darkMode ? new Color(230, 230, 230) : Color.BLACK;
//
//        for (Component c : container.getComponents()) {
//            if (c instanceof JPanel || c instanceof JScrollPane || c instanceof JTabbedPane) {
//                c.setBackground(bg);
//                c.setForeground(fg);
//                applyThemeToContainer((Container) c);
//            } else if (c instanceof JLabel || c instanceof JButton || c instanceof JTextField || c instanceof JComboBox) {
//                c.setBackground(bg);
//                c.setForeground(fg);
//            }
//        }
//        container.setBackground(bg);
//    }
}
