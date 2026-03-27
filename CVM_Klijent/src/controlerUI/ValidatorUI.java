/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlerUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.SwingUtilities;

/**
 *
 * @author sreck
 */
public class ValidatorUI {

    // ===================== BOJE =====================
    private static final Color ERROR_COLOR = new Color(255, 170, 170);
    private static final Color NORMAL_COLOR = Color.white;
    private static final Color TEXT_ERROR_COLOR = new Color(180, 0, 0);

    // ===================== DATUMI (DocumentListener verzija) =====================
    public static void validateDatumRodjenja(JTextField field, JLabel label) {
        addDateValidation(field, label, dat -> {
            Calendar min = Calendar.getInstance();
            min.set(1930, Calendar.JANUARY, 1);
            Date danas = new Date();
            return !dat.before(min.getTime()) && !dat.after(danas);
        }, "<html>Datum rođenja mora biti<br>"
                + " između 1930. i današnjeg datuma!</html>");
    }

    public static void validateDatumOsnivanja(JTextField field, JLabel label) {
        addDateValidation(field, label, dat -> {
            Calendar min = Calendar.getInstance();
            min.set(1800, Calendar.JANUARY, 1);
            Date danas = new Date();
            return !dat.before(min.getTime()) && !dat.after(danas);
        }, "<html>Datum osnivanja mora biti<br>"
                + " između 1800. i današnjeg datuma!</html>");
    }

//    public static void validateDatumTakmicenja(JTextField field, JLabel label) {
//        addDateValidation(field, label, dat -> {
//            Date danas = new Date();
//            return !dat.before(danas);
//        }, "Datum takmičenja ne može biti pre današnjeg datuma!");
//    }
    public static void validateDatumTakmicenja(JTextField field, JLabel label) {
        addDateValidation(field, label, dat -> {
            Date danas = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(danas);
            cal.add(Calendar.YEAR, 10);
            Date maxDozvoljeni = cal.getTime();

            //buducnost, al ne više od 10 godina 
            return !dat.before(danas) && !dat.after(maxDozvoljeni);
        }, "<html>Datum takmičenja mora biti u budućnosti<br>"
                + "i ne više od 10 godina!</html>");
    }

    public static void validateDatumLicence(JTextField field, JLabel label) {
        addDateValidation(field, label, dat -> {
            Date danas = new Date();
            return !dat.before(danas);
        }, "<html>Datum isteka licence<br>"
                + " ne može biti pre današnjeg datuma!</html>");
    }

    private static void addDateValidation(JTextField field, JLabel label,
            java.util.function.Predicate<Date> logicalCheck,
            String logicalErrorMsg) {

        field.getDocument().addDocumentListener(new DocumentListener() {

            void update() {
                String unos = field.getText().trim();

                if (unos.isEmpty()) {
                    clearError(field, label);
                    return;
                }

                try {
                    Date dat = parseDatum(unos);

                    if (!logicalCheck.test(dat)) {
                        showError(field, label, logicalErrorMsg);
                    } else {
                        // ako je sve ok — formatiraj datum i očisti boju/labelu
                        String formatiran = formatDatum(dat);
                        if (!field.getText().equals(formatiran)) {
                            // spreči rekurzivno ažuriranje DocumentListener-a
                            SwingUtilities.invokeLater(() -> field.setText(formatiran));
                        }
                        field.setBackground(NORMAL_COLOR);
                        if (label != null) {
                            label.setText("");
                        }
                    }

                } catch (ParseException ex) {
                    field.setBackground(ERROR_COLOR);
                    if (label != null) {
                        label.setForeground(TEXT_ERROR_COLOR);
                        label.setText("Neispravan format datuma! (npr. 12.03.2020)");
                    }
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        });
    }

    // ===================== OSTALA POLJA (DocumentListener) =====================
    private static void addLiveValidation(JTextField field, JLabel label,
            java.util.function.Predicate<String> validator,
            String errorMsg) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            void update() {
                String text = field.getText().trim();
                if (validator.test(text)) {
                    field.setBackground(NORMAL_COLOR);
                    if (label != null) {
                        label.setText("");
                    }
                } else {
                    field.setBackground(ERROR_COLOR);
                    if (label != null) {
                        label.setForeground(TEXT_ERROR_COLOR);
                        label.setText(errorMsg);
                    }
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        });
    }

//    public static void validateNameField(JTextField field, JLabel label) {
//        addLiveValidation(field, label,
//                text -> text.isEmpty() || text.matches("^[A-ZŠĐČĆŽ][a-zšđčćžA-ZŠĐČĆŽ\\s]*$"),
//                "Ime/Prezime mora početi\n"
//                + " velikim slovom i sadržati samo slova!");
//    }
    public static void validateNameField(JTextField field, JLabel label) {
        addLiveValidation(field, label,
                text -> text.isEmpty() || text.matches("^[A-ZŠĐČĆŽ][a-zšđčćžA-ZŠĐČĆŽ\\s]*$"),
                "<html>Ime/Prezime mora početi<br>"
                + "velikim slovom i sadržati samo slova!</html>");
    }

    public static void validateTelefon(JTextField field, JLabel label) {
        addLiveValidation(field, label,
                text -> text.isEmpty() || text.matches("^06[0-9]{6,}$"),
                "<html>Broj mora početi sa 06<br>"
                + "i imati najmanje 8 cifara!</html>");
    }

    public static void validateGrad(JTextField field, JLabel label) {
        addLiveValidation(field, label,
                text -> text.isEmpty() || text.matches("^[A-ZŠĐČĆŽ][a-zšđčćžA-ZŠĐČĆŽ\\s\\-_,()]*$"),
                "<html>Grad mora početi velikim slovom<br>"
                + "i ne sme sadržati brojeve!</html>");
    }

    public static void validateNaziv(JTextField field, JLabel label) {
        addLiveValidation(field, label,
                text -> text.isEmpty() || text.matches("^[A-ZŠĐČĆŽ][A-Za-zŠĐČĆŽšđčćž0-9\\s\\-_,()]*$"),
                "<html>Naziv mora početi velikim slovom!</html>");
    }

    public static void validateLozinka(JTextField field, JLabel label) {
        addLiveValidation(field, label,
                text -> text.isEmpty() || text.length() >= 6,
                "Lozinka mora imati najmanje 6 karaktera!");
    }

    public static void validateLicenca(JTextField field, JLabel label) {
        addLiveValidation(field, label,
                text -> text.isEmpty() || text.matches("^[A-Z]{2}[0-9]{4,6}$"),
                "Licenca mora imati 2 velika slova i 4–6 cifara!");
    }

    // ===================== VALIDACIJA ZA PRIJAVU =====================
    public static void validateEmail(JTextField field, JLabel label) {
        addLiveValidation(field, label,
                text -> text.isEmpty() || text.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(com|rs)$"),
                "Unesite ispravan email (npr. ime@domen.com ili .rs)");
    }

    public static void validateBrojZaposlenih(JTextField field, JLabel label) {
        addLiveValidation(field, label,
                text -> text.isEmpty() || text.matches("^[1-9][0-9]{0,2}$"),
                "Broj zaposlenih mora biti između 1 i 999!");
    }

    // ===================== POMOCNE METODE =====================
    private static Date parseDatum(String dateStr) throws ParseException {
        String[] formati = {"dd.MM.yyyy", "dd.MM.yyyy.", "dd/MM/yyyy", "yyyy-MM-dd", "d.M.yyyy"};
        for (String f : formati) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(f);
                sdf.setLenient(false);
                return sdf.parse(dateStr);
            } catch (ParseException ignored) {
            }
        }
        throw new ParseException("Nepoznat format datuma: " + dateStr, 0);
    }

    private static String formatDatum(Date dat) {
        return new SimpleDateFormat("dd. MM. yyyy.").format(dat);
    }

    // === POMOCNE METODE ZA PRIKAZ / CISCENJE GREŠKE ===
    private static void showError(JTextField field, JLabel label, String message) {
        field.setBackground(ERROR_COLOR);
        if (label != null) {
            label.setForeground(TEXT_ERROR_COLOR);
            label.setText(message);
        }
    }

    private static void clearError(JTextField field, JLabel label) {
        field.setBackground(NORMAL_COLOR);
        if (label != null) {
            label.setText("");
        }
    }

    public static void updateButtonState(JButton button, JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getBackground().equals(ERROR_COLOR) || field.getText().trim().isEmpty()) {
                button.setEnabled(false);
                return;
            }
        }
        button.setEnabled(true);
    }

    public static void enableAutoButtonControl(
            JButton button,
            JTextField[] textFields,
            JComboBox<?>[] comboBoxes
    ) {
        updateButtonState(button, textFields, comboBoxes);

        DocumentListener docListener = new DocumentListener() {
            void update() {
                SwingUtilities.invokeLater(() -> updateButtonState(button, textFields, comboBoxes));
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        };

        for (JTextField field : textFields) {
            field.getDocument().addDocumentListener(docListener);
        }

        if (comboBoxes != null) {
            for (JComboBox<?> combo : comboBoxes) {
                if (combo != null && combo.isEnabled()) {
                    combo.addActionListener(e -> updateButtonState(button, textFields, comboBoxes));
                    combo.addItemListener(e -> SwingUtilities.invokeLater(
                            () -> updateButtonState(button, textFields, comboBoxes)
                    ));
                }
            }
        }
    }

    public static void updateButtonState(
            JButton button,
            JTextField[] textFields,
            JComboBox<?>[] comboBoxes
    ) {
        // proveri tekstualna polja
        for (JTextField field : textFields) {
            if (field.getBackground().equals(ERROR_COLOR) || field.getText().trim().isEmpty()) {
                button.setEnabled(false);
                return;
            }
        }

        // proveri comboBox-e samo ako postoje
        if (comboBoxes != null) {
            for (JComboBox<?> combo : comboBoxes) {
                if (combo == null) {
                    continue; // zaštita od null
                }
                if (combo.isEnabled() && (combo.getSelectedIndex() == -1 || combo.getSelectedItem() == null)) {
                    button.setEnabled(false);
                    return;
                }
            }
        }

        button.setEnabled(true);
    }

    public static boolean isFormValid(JTextField[] fields, JComboBox<?>[] combos) {
        for (JTextField f : fields) {
            if (f.getBackground().equals(ERROR_COLOR) || f.getText().trim().isEmpty()) {
                return false;
            }
        }
        for (JComboBox<?> c : combos) {
            if (c.getSelectedIndex() == -1 || c.getSelectedItem() == null) {
                return false;
            }
        }
        return true;
    }

    public static boolean waitAndValidateForm(Component parent, JTextField[] fields, JComboBox<?>[] combos) {
        try {
            Thread.sleep(100); // sačekaj 0.1s da Swing odradi focusLost
        } catch (InterruptedException ignored) {
        }
        return isFormValid(fields, combos);
    }

    //    // ===================== DATUMI =====================
//    public static void validateDatumRodjenja(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    return;
//                }
//
//                try {
//                    Date dat = parseDatum(unos);
//                    Calendar min = Calendar.getInstance();
//                    min.set(1930, Calendar.JANUARY, 1);
//                    Date danas = new Date();
//
//                    if (dat.before(min.getTime()) || dat.after(danas)) {
//                        field.setBackground(ERROR_COLOR);
//                        JOptionPane.showMessageDialog(field.getParent(),
//                                "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd\n"
//                                + "Datum rođenja mora biti između 1930. i današnjeg datuma.",
//                                "Greška", JOptionPane.ERROR_MESSAGE);
//                    } else {
//                        field.setText(formatDatum(dat));
//                        field.setBackground(NORMAL_COLOR);
//                    }
//                } catch (ParseException ex) {
//                    field.setBackground(ERROR_COLOR);
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Neispravan format datuma!\nDozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                            "Greška", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//    }
//
//    public static void validateDatumOsnivanja(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    return;
//                }
//
//                try {
//                    Date dat = parseDatum(unos);
//                    Date danas = new Date();
//                    if (dat.after(danas)) {
//                        field.setBackground(ERROR_COLOR);
//                        JOptionPane.showMessageDialog(field.getParent(),
//                                "Datum osnivanja ne može biti posle današnjeg datuma!\n"
//                                + "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                                "Greška", JOptionPane.ERROR_MESSAGE);
//                    } else {
//                        field.setText(formatDatum(dat));
//                        field.setBackground(NORMAL_COLOR);
//                    }
//                } catch (ParseException ex) {
//                    field.setBackground(ERROR_COLOR);
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Neispravan format datuma!\nDozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                            "Greška", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//    }
//
//    public static void validateDatumTakmicenja(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    return;
//                }
//
//                try {
//                    Date dat = parseDatum(unos);
//                    Date danas = new Date();
//                    if (dat.before(danas)) {
//                        field.setBackground(ERROR_COLOR);
//                        JOptionPane.showMessageDialog(field.getParent(),
//                                "Datum takmičenja ne može biti pre današnjeg datuma!\n"
//                                + "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                                "Greška", JOptionPane.ERROR_MESSAGE);
//                    } else {
//                        field.setText(formatDatum(dat));
//                        field.setBackground(NORMAL_COLOR);
//                    }
//                } catch (ParseException ex) {
//                    field.setBackground(ERROR_COLOR);
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Neispravan format datuma!\nDozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                            "Greška", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//    }
//
//    public static void validateDatumLicence(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    return;
//                }
//
//                try {
//                    Date dat = parseDatum(unos);
//                    Date danas = new Date();
//                    if (dat.before(danas)) {
//                        field.setBackground(ERROR_COLOR);
//                        JOptionPane.showMessageDialog(field.getParent(),
//                                "Datum isteka licence ne može biti pre današnjeg datuma!\n"
//                                + "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                                "Greška", JOptionPane.ERROR_MESSAGE);
//                    } else {
//                        field.setText(formatDatum(dat));
//                        field.setBackground(NORMAL_COLOR);
//                    }
//                } catch (ParseException ex) {
//                    field.setBackground(ERROR_COLOR);
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Neispravan format datuma!\nDozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                            "Greška", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//    }
    //// DELIMICNO OKEJ
    //
    //
    //
//    private static final Color ERROR_COLOR = new Color(255, 170, 170);
//    private static final Color NORMAL_COLOR = Color.white;
//    private static final Color TEXT_ERROR_COLOR = new Color(180, 0, 0);
//
//    // ===================== DATUMI =====================
//    public static void validateDatumRodjenja(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    return;
//                }
//
//                try {
//                    Date dat = parseDatum(unos);
//                    Calendar min = Calendar.getInstance();
//                    min.set(1930, Calendar.JANUARY, 1);
//                    Date danas = new Date();
//
//                    if (dat.before(min.getTime()) || dat.after(danas)) {
//                        field.setBackground(ERROR_COLOR);
//                        JOptionPane.showMessageDialog(field.getParent(),
//                                "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd\n"
//                                + "Datum rođenja mora biti između 1930. i današnjeg datuma.",
//                                "Greška", JOptionPane.ERROR_MESSAGE);
//                    } else {
//                        field.setText(formatDatum(dat));
//                        field.setBackground(NORMAL_COLOR);
//                    }
//                } catch (ParseException ex) {
//                    field.setBackground(ERROR_COLOR);
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Neispravan format datuma!\nDozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                            "Greška", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//    }
//
//    public static void validateDatumOsnivanja(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    return;
//                }
//
//                try {
//                    Date dat = parseDatum(unos);
//                    Date danas = new Date();
//                    if (dat.after(danas)) {
//                        field.setBackground(ERROR_COLOR);
//                        JOptionPane.showMessageDialog(field.getParent(),
//                                "Datum osnivanja ne može biti posle današnjeg datuma!\n"
//                                + "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                                "Greška", JOptionPane.ERROR_MESSAGE);
//                    } else {
//                        field.setText(formatDatum(dat));
//                        field.setBackground(NORMAL_COLOR);
//                    }
//                } catch (ParseException ex) {
//                    field.setBackground(ERROR_COLOR);
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Neispravan format datuma!\nDozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                            "Greška", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//    }
//
//    public static void validateDatumTakmicenja(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    return;
//                }
//
//                try {
//                    Date dat = parseDatum(unos);
//                    Date danas = new Date();
//                    if (dat.before(danas)) {
//                        field.setBackground(ERROR_COLOR);
//                        JOptionPane.showMessageDialog(field.getParent(),
//                                "Datum takmičenja ne može biti pre današnjeg datuma!\n"
//                                + "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                                "Greška", JOptionPane.ERROR_MESSAGE);
//                    } else {
//                        field.setText(formatDatum(dat));
//                        field.setBackground(NORMAL_COLOR);
//                    }
//                } catch (ParseException ex) {
//                    field.setBackground(ERROR_COLOR);
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Neispravan format datuma!\nDozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                            "Greška", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//    }
//
//    public static void validateDatumLicence(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    return;
//                }
//
//                try {
//                    Date dat = parseDatum(unos);
//                    Date danas = new Date();
//                    if (dat.before(danas)) {
//                        field.setBackground(ERROR_COLOR);
//                        JOptionPane.showMessageDialog(field.getParent(),
//                                "Datum isteka licence ne može biti pre današnjeg datuma!\n"
//                                + "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                                "Greška", JOptionPane.ERROR_MESSAGE);
//                    } else {
//                        field.setText(formatDatum(dat));
//                        field.setBackground(NORMAL_COLOR);
//                    }
//                } catch (ParseException ex) {
//                    field.setBackground(ERROR_COLOR);
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Neispravan format datuma!\nDozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                            "Greška", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//    }
//
//    // ===================== OSTALA POLJA (inline prikaz) =====================
//    public static void validateNameField(JTextField field, JLabel label) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                clearError(field, label);
//                String text = field.getText().trim();
//
//                if (text.isEmpty()) {
//                    showError(field, label, "Ovo polje je obavezno!");
//                } else if (!text.matches("^[A-ZŠĐČĆŽ][a-zšđčćžA-ZŠĐČĆŽ\\s]*$")) {
//                    showError(field, label, "Dozvoljena su samo slova i prvo mora biti veliko!");
//                }
//            }
//        });
//    }
//
//    public static void validateTelefon(JTextField field, JLabel label) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                clearError(field, label);
//                String broj = field.getText().trim().replaceAll("\\s+", "");
//                if (broj.isEmpty()) {
//                    return;
//                }
//                if (!broj.matches("^06[0-9]{6,}$")) {
//                    showError(field, label, "Broj mora početi sa 06 i imati najmanje 8 cifara!");
//                }
//            }
//        });
//    }
//
//    public static void validateGrad(JTextField field, JLabel label) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                clearError(field, label);
//                String grad = field.getText().trim();
//                if (!grad.matches("^[A-ZŠĐČĆŽ][a-zšđčćžA-ZŠĐČĆŽ\\s]*$")) {
//                    showError(field, label, "Grad mora početi velikim slovom i sadržati samo slova!");
//                }
//            }
//        });
//    }
//
//    public static void validateNaziv(JTextField field, JLabel label) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                clearError(field, label);
//                String naziv = field.getText().trim();
//                if (!naziv.matches("^[A-ZŠĐČĆŽ][a-zšđčćžA-ZŠĐČĆŽ\\s]*$")) {
//                    showError(field, label, "Naziv mora početi velikim slovom i sadržati samo slova!");
//                }
//            }
//        });
//    }
//
//    public static void validateLicenca(JTextField field, JLabel label) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                clearError(field, label);
//                String lic = field.getText().trim();
//                if (!lic.matches("^[A-Z]{2}[0-9]{4,6}$")) {
//                    showError(field, label, "Licenca mora imati 2 velika slova i 4–6 cifara!");
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Primer: AB1234 ili XY567890\n(2 velika slova + 4 do 6 brojeva)",
//                            "Greška", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//    }
//
//    public static void validateLozinka(JTextField field, JLabel label) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                clearError(field, label);
//                String lozinka = field.getText().trim();
//                if (lozinka.length() < 6) {
//                    showError(field, label, "Lozinka mora imati najmanje 6 karaktera!");
//                }
//            }
//        });
//    }
//
//    // ===================== POMOĆNE METODE =====================
//    private static void showError(JTextField field, JLabel label, String message) {
//        field.setBackground(ERROR_COLOR);
//        if (label != null) {
//            label.setForeground(TEXT_ERROR_COLOR);
//            label.setText(message);
//        }
//    }
//
//    private static void clearError(JTextField field, JLabel label) {
//        field.setBackground(NORMAL_COLOR);
//        if (label != null) {
//            label.setText("");
//        }
//    }
//
//    private static Date parseDatum(String dateStr) throws ParseException {
//        String[] formati = {"dd.MM.yyyy", "dd.MM.yyyy.", "dd/MM/yyyy", "yyyy-MM-dd", "d.M.yyyy"};
//        for (String f : formati) {
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat(f);
//                sdf.setLenient(false);
//                return sdf.parse(dateStr);
//            } catch (ParseException ignored) {
//            }
//        }
//        throw new ParseException("Nepoznat format datuma: " + dateStr, 0);
//    }
//
//    private static String formatDatum(Date dat) {
//        return new SimpleDateFormat("dd. MM. yyyy.").format(dat);
//    }
//
//    public static void updateButtonState(javax.swing.JButton button, JTextField... fields) {
//        for (JTextField field : fields) {
//            if (field.getBackground().equals(new Color(255, 150, 150)) || field.getText().trim().isEmpty()) {
//                button.setEnabled(false);
//                return;
//            }
//        }
//        button.setEnabled(true);
//    }
//
//    public static void enableAutoButtonControl(
//            JButton button,
//            JTextField[] textFields,
//            JComboBox<?>[] comboBoxes
//    ) {
//        // 1. inicijalno proveri sve
//        updateButtonState(button, textFields, comboBoxes);
//
//        // 2. dodaj listener na sva polja
//        FocusAdapter focusListener = new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                updateButtonState(button, textFields, comboBoxes);
//            }
//        };
//
//        for (JTextField field : textFields) {
//            field.addFocusListener(focusListener);
//        }
//
//        // 3. dodaj listener i na sve ComboBox-e
//        for (JComboBox<?> combo : comboBoxes) {
//            combo.addActionListener(e -> updateButtonState(button, textFields, comboBoxes));
//        }
//    }
//
//    public static void updateButtonState(
//            JButton button,
//            JTextField[] textFields,
//            JComboBox<?>[] comboBoxes
//    ) {
//        // proveri sva tekstualna polja
//        for (JTextField field : textFields) {
//            if (field.getBackground().equals(new Color(255, 150, 150)) // crveno polje
//                    || field.getText().trim().isEmpty()) {
//                button.setEnabled(false);
//                return;
//            }
//        }
//
//        // proveri sve combo boxeve
//        for (JComboBox<?> combo : comboBoxes) {
//            if (combo.getSelectedIndex() == -1 || combo.getSelectedItem() == null) {
//                button.setEnabled(false);
//                return;
//            }
//        }
//
//        // ako je sve u redu, dugme se aktivira
//        button.setEnabled(true);
//    }
//
//    public static boolean isFormValid(JTextField[] fields, JComboBox<?>[] combos) {
//        for (JTextField f : fields) {
//            if (f.getBackground().equals(new Color(255, 150, 150))
//                    || f.getText().trim().isEmpty()) {
//                return false;
//            }
//        }
//        for (JComboBox<?> c : combos) {
//            if (c.getSelectedIndex() == -1 || c.getSelectedItem() == null) {
//                return false;
//            }
//        }
//        return true;
//    }
//    private static final Color ERROR_COLOR = new Color(255, 150, 150);
//    private static final Color NORMAL_COLOR = Color.white;
//    private static final Color TEXT_ERROR_COLOR = new Color(180, 0, 0);
//
//    // ===================== DATUMI =====================
//    public static void validateDatumRodjenja(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    return;
//                }
//
//                try {
//                    Date dat = parseDatum(unos);
//                    Calendar cal = Calendar.getInstance();
//
//                    Calendar min = Calendar.getInstance();
//                    min.set(1930, Calendar.JANUARY, 1);
//
//                    if (dat.before(min.getTime()) || dat.after(cal.getTime())) {
//                        showError(field, "Datum rođenja mora biti između 1930. i današnjeg datuma!");
//                        JOptionPane.showMessageDialog(field.getParent(),
//                                "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd\n"
//                                + "Datum rođenja mora biti između 1930. i današnjeg datuma.",
//                                "Greška",
//                                JOptionPane.ERROR_MESSAGE);
//                        SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
//                    } else {
//                        field.setText(formatDatum(dat));
//                        clearError(field);
//                    }
//                } catch (ParseException ex) {
//                    showError(field, "Neispravan format datuma!");
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                            "Greška",
//                            JOptionPane.ERROR_MESSAGE);
//                    SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
//                }
//            }
//        });
//    }
//
//    public static void validateDatumOsnivanja(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    return;
//                }
//
//                try {
//                    Date dat = parseDatum(unos);
//                    Date danas = new Date();
//
//                    if (dat.after(danas)) {
//                        showError(field, "Datum osnivanja ne može biti u budućnosti!");
//                        JOptionPane.showMessageDialog(field.getParent(),
//                                "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd\n"
//                                + "Datum osnivanja ne može biti posle današnjeg datuma.",
//                                "Greška",
//                                JOptionPane.ERROR_MESSAGE);
//                        SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
//                    } else {
//                        field.setText(formatDatum(dat));
//                        clearError(field);
//                    }
//                } catch (ParseException ex) {
//                    showError(field, "Neispravan format datuma!");
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                            "Greška",
//                            JOptionPane.ERROR_MESSAGE);
//                    SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
//                }
//            }
//        });
//    }
//
//    public static void validateDatumTakmicenja(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    return;
//                }
//
//                try {
//                    Date dat = parseDatum(unos);
//                    Date danas = new Date();
//
//                    if (dat.before(danas)) {
//                        showError(field, "Datum takmičenja ne može biti u prošlosti!");
//                        JOptionPane.showMessageDialog(field.getParent(),
//                                "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd\n"
//                                + "Datum takmičenja ne može biti pre današnjeg datuma.",
//                                "Greška",
//                                JOptionPane.ERROR_MESSAGE);
//                        SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
//                    } else {
//                        field.setText(formatDatum(dat));
//                        clearError(field);
//                    }
//                } catch (ParseException ex) {
//                    showError(field, "Neispravan format datuma!");
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                            "Greška",
//                            JOptionPane.ERROR_MESSAGE);
//                    SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
//                }
//            }
//        });
//    }
//
//    public static void validateDatumLicence(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    return;
//                }
//
//                try {
//                    Date dat = parseDatum(unos);
//                    Date danas = new Date();
//
//                    if (dat.before(danas)) {
//                        showError(field, "Datum isteka licence ne može biti u prošlosti!");
//                        JOptionPane.showMessageDialog(field.getParent(),
//                                "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd\n"
//                                + "Datum isteka licence ne može biti pre današnjeg datuma.",
//                                "Greška",
//                                JOptionPane.ERROR_MESSAGE);
//                        SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
//                    } else {
//                        field.setText(formatDatum(dat));
//                        clearError(field);
//                    }
//                } catch (ParseException ex) {
//                    showError(field, "Neispravan format datuma!");
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                            "Greška",
//                            JOptionPane.ERROR_MESSAGE);
//                    SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
//                }
//            }
//        });
//    }
//
//    // ===================== OSTALE VALIDACIJE =====================
//    public static void validateTelefon(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String broj = field.getText().trim().replaceAll("\\s+", "");
//                if (broj.isEmpty()) {
//                    return;
//                }
//
//                if (!broj.matches("^06[0-9]{6,}$")) {
//                    showError(field, "Broj mora početi sa 06 i imati najmanje 8 cifara!");
//                    SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
//                } else {
//                    clearError(field);
//                }
//            }
//        });
//    }
//
//    public static void validateLozinka(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String lozinka = field.getText().trim();
//                if (lozinka.length() < 6) {
//                    showError(field, "Lozinka mora imati najmanje 6 karaktera!");
//                    SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
//                } else {
//                    clearError(field);
//                }
//            }
//        });
//    }
//
//    public static void validateGrad(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String grad = field.getText().trim();
//                if (!grad.matches("^[A-ZŠĐČĆŽ][a-zšđčćžA-ZŠĐČĆŽ\\s]*$")) {
//                    showError(field, "Naziv grada mora početi velikim slovom i sadržati samo slova!");
//                    SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
//                } else {
//                    clearError(field);
//                }
//            }
//        });
//    }
//
//    public static void validateNaziv(JTextField field) { // AMK / CVM
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String naziv = field.getText().trim();
//                if (!naziv.matches("^[A-ZŠĐČĆŽ][a-zšđčćžA-ZŠĐČĆŽ\\s]*$")) {
//                    showError(field, "Naziv mora početi velikim slovom i sadržati samo slova!");
//                    SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
//                } else {
//                    clearError(field);
//                }
//            }
//        });
//    }
//
//    public static void validateLicenca(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String lic = field.getText().trim();
//                if (!lic.matches("^[A-Z]{2}[0-9]{4,6}$")) {
//                    showError(field, "Licenca mora imati 2 velika slova i 4–6 cifara!");
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Primer ispravne licence: AB1234 ili XY567890\n(2 velika slova + 4 do 6 brojeva)",
//                            "Greška",
//                            JOptionPane.ERROR_MESSAGE);
//                    SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
//                } else {
//                    clearError(field);
//                }
//            }
//        });
//    }
//
//    public static void validateNameField(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//
//                if (unos.isEmpty()) {
//                    showError(field, "Ovo polje je obavezno!");
//                    SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
//                    return;
//                }
//
//                if (!unos.matches("^[A-ZŠĐČĆŽ][a-zšđčćžA-ZŠĐČĆŽ\\s]*$")) {
//                    showError(field, "Dozvoljena su samo slova i prvo mora biti veliko!");
//                    SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
//                } else {
//                    clearError(field);
//                }
//            }
//        });
//    }
//
//    // ===================== POMOĆNE METODE =====================
//    private static void showError(JTextField field, String poruka) {
//        field.setBackground(ERROR_COLOR);
//        field.setToolTipText(poruka);
//    }
//
//    private static void clearError(JTextField field) {
//        field.setBackground(NORMAL_COLOR);
//        field.setToolTipText(null);
//    }
//
//    private static Date parseDatum(String dateStr) throws ParseException {
//        String[] formati = {"dd.MM.yyyy", "dd.MM.yyyy.", "dd/MM/yyyy", "yyyy-MM-dd", "d.M.yyyy"};
//        for (String f : formati) {
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat(f);
//                sdf.setLenient(false);
//                return sdf.parse(dateStr);
//            } catch (ParseException ignored) {
//            }
//        }
//        throw new ParseException("Nepoznat format datuma: " + dateStr, 0);
//    }
//
//    private static String formatDatum(Date dat) {
//        return new SimpleDateFormat("dd. MM. yyyy.").format(dat);
//    }
//    private static final Color ERROR_COLOR = new Color(255, 150, 150);
//    private static final Color NORMAL_COLOR = Color.white;
//
//    // ===================== DATUMI =====================
//    public static void validateDatumRodjenja(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    return;
//                }
//
//                try {
//                    Date dat = parseDatum(unos);
//                    Calendar cal = Calendar.getInstance();
//
//                    // ne pre 1930
//                    Calendar min = Calendar.getInstance();
//                    min.set(1930, Calendar.JANUARY, 1);
//
//                    if (dat.before(min.getTime()) || dat.after(cal.getTime())) {
//                        showError(field, "Datum rođenja mora biti između 1930. i današnjeg datuma!");
//                        JOptionPane.showMessageDialog(field.getParent(),
//                                "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd\n"
//                                + "Datum rođenja mora biti između 1930. i današnjeg datuma.",
//                                "Greška",
//                                JOptionPane.ERROR_MESSAGE);
//                        field.requestFocus();
//                    } else {
//                        field.setText(formatDatum(dat));
//                        clearError(field);
//                    }
//                } catch (ParseException ex) {
//                    showError(field, "Neispravan format datuma!");
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                            "Greška",
//                            JOptionPane.ERROR_MESSAGE);
//                    field.requestFocus();
//                }
//            }
//        });
//    }
//
//    public static void validateDatumOsnivanja(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                try {
//                    String unos = field.getText().trim();
//                    if (unos.isEmpty()) {
//                        return;
//                    }
//
//                    Date dat = parseDatum(unos);
//                    Date danas = new Date();
//
//                    if (dat.after(danas)) {
//                        showError(field, "Datum osnivanja ne može biti u budućnosti!");
//                        JOptionPane.showMessageDialog(field.getParent(),
//                                "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd\n"
//                                + "Datum osnivanja ne može biti posle današnjeg datuma.",
//                                "Greška",
//                                JOptionPane.ERROR_MESSAGE);
//                        field.requestFocus();
//                    } else {
//                        field.setText(formatDatum(dat));
//                        clearError(field);
//                    }
//                } catch (ParseException ex) {
//                    showError(field, "Neispravan format datuma!");
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                            "Greška",
//                            JOptionPane.ERROR_MESSAGE);
//                    field.requestFocus();
//                }
//            }
//        });
//    }
//
//    public static void validateDatumTakmicenja(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                try {
//                    String unos = field.getText().trim();
//                    if (unos.isEmpty()) {
//                        return;
//                    }
//
//                    Date dat = parseDatum(unos);
//                    Date danas = new Date();
//
//                    if (dat.before(danas)) {
//                        showError(field, "Datum takmičenja ne može biti u prošlosti!");
//                        JOptionPane.showMessageDialog(field.getParent(),
//                                "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd\n"
//                                + "Datum takmičenja ne može biti pre današnjeg datuma.",
//                                "Greška",
//                                JOptionPane.ERROR_MESSAGE);
//                        field.requestFocus();
//                    } else {
//                        field.setText(formatDatum(dat));
//                        clearError(field);
//                    }
//                } catch (ParseException ex) {
//                    showError(field, "Neispravan format datuma!");
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                            "Greška",
//                            JOptionPane.ERROR_MESSAGE);
//                    field.requestFocus();
//                }
//            }
//        });
//    }
//
//    public static void validateDatumLicence(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                try {
//                    String unos = field.getText().trim();
//                    if (unos.isEmpty()) {
//                        return;
//                    }
//
//                    Date dat = parseDatum(unos);
//                    Date danas = new Date();
//
//                    if (dat.before(danas)) {
//                        showError(field, "Datum isteka licence ne može biti pre današnjeg!");
//                        JOptionPane.showMessageDialog(field.getParent(),
//                                "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd\n"
//                                + "Datum isteka licence ne može biti u prošlosti.",
//                                "Greška",
//                                JOptionPane.ERROR_MESSAGE);
//                        field.requestFocus();
//                    } else {
//                        field.setText(formatDatum(dat));
//                        clearError(field);
//                    }
//                } catch (ParseException ex) {
//                    showError(field, "Neispravan format datuma!");
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Dozvoljeni formati: dd.MM.yyyy, dd/MM/yyyy, yyyy-MM-dd",
//                            "Greška",
//                            JOptionPane.ERROR_MESSAGE);
//                    field.requestFocus();
//                }
//            }
//        });
//    }
//
//    // ===================== OSTALE VALIDACIJE =====================
//    // === Validacija imena i prezimena (početno veliko slovo, samo slova) ===
//    public static void validateNameField(JTextField field) {
//        field.addFocusListener(new java.awt.event.FocusAdapter() {
//            @Override
//            public void focusLost(java.awt.event.FocusEvent e) {
//                String unos = field.getText().trim();
//
//                if (unos.isEmpty()) {
//                    showError(field, "Ovo polje je obavezno!");
//                    field.requestFocus();
//                    return;
//                }
//
//                // dozvoljena samo slova (srpska i engleska), razmak i veliko prvo slovo
//                if (!unos.matches("^[A-ZŠĐČĆŽ][a-zšđčćžA-ZŠĐČĆŽ\\s]*$")) {
//                    showError(field, "Dozvoljena su samo slova i prvo mora biti veliko!");
//                    field.requestFocus();
//                } else {
//                    clearError(field);
//                }
//            }
//        });
//    }
//
//    public static void validateTelefon(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String broj = field.getText().trim().replaceAll("\\s+", "");
//                if (broj.isEmpty()) {
//                    return;
//                }
//
//                if (!broj.matches("^06[0-9]{6,}$")) {
//                    showError(field, "Broj mora početi sa 06 i imati minimum 8 cifara!");
//                    field.requestFocus();
//                } else {
//                    clearError(field);
//                }
//            }
//        });
//    }
//
//    public static void validateLozinka(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String lozinka = field.getText().trim();
//                if (lozinka.length() < 6) {
//                    showError(field, "Lozinka mora imati najmanje 6 karaktera!");
//                    field.requestFocus();
//                } else {
//                    clearError(field);
//                }
//            }
//        });
//    }
//
//    public static void validateGrad(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String grad = field.getText().trim();
//                if (!grad.matches("^[A-ZŠĐČĆŽ][a-zšđčćžA-ZŠĐČĆŽ\\s]*$")) {
//                    showError(field, "Naziv grada mora početi velikim slovom i sadržati samo slova!");
//                    field.requestFocus();
//                } else {
//                    clearError(field);
//                }
//            }
//        });
//    }
//
//    public static void validateNaziv(JTextField field) { // za AMK / CVM
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String naziv = field.getText().trim();
//                if (!naziv.matches("^[A-ZŠĐČĆŽ][a-zšđčćžA-ZŠĐČĆŽ\\s]*$")) {
//                    showError(field, "Naziv mora početi velikim slovom i sadržati samo slova!");
//                    field.requestFocus();
//                } else {
//                    clearError(field);
//                }
//            }
//        });
//    }
//
//    public static void validateLicenca(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String lic = field.getText().trim();
//                if (!lic.matches("^[A-Z]{2}[0-9]{4,6}$")) {
//                    showError(field, "Licenca mora imati 2 velika slova i 4–6 cifara!");
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Primer ispravne licence: AB1234 ili XY567890\n(2 velika slova + 4 do 6 brojeva)",
//                            "Greška",
//                            JOptionPane.ERROR_MESSAGE);
//                    field.requestFocus();
//                } else {
//                    clearError(field);
//                }
//            }
//        });
//    }
//
//    // ===================== POMOĆNE METODE =====================
//    private static void showError(JTextField field, String poruka) {
//        field.setBackground(ERROR_COLOR);
//        field.setToolTipText(poruka);
//    }
//
//    private static void clearError(JTextField field) {
//        field.setBackground(NORMAL_COLOR);
//        field.setToolTipText(null);
//    }
//
//    private static Date parseDatum(String dateStr) throws ParseException {
//        String[] formati = {"dd.MM.yyyy", "dd.MM.yyyy.", "dd/MM/yyyy", "yyyy-MM-dd", "d.M.yyyy"};
//        for (String f : formati) {
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat(f);
//                sdf.setLenient(false);
//                return sdf.parse(dateStr);
//            } catch (ParseException ignored) {
//            }
//        }
//        throw new ParseException("Nepoznat format datuma: " + dateStr, 0);
//    }
//
//    private static String formatDatum(Date dat) {
//        return new SimpleDateFormat("dd. MM. yyyy.").format(dat);
//    }
    //////
    // cetvrti
    ///////////
//    private static final Color ERROR_COLOR = new Color(255, 150, 150);
//    private static final Color NORMAL_COLOR = Color.white;
//    private static final Color TEXT_ERROR_COLOR = new Color(180, 0, 0);
//
//    // === 1. Validacija imena i prezimena ===
//    public static void validateNameField(JTextField field, JLabel errorLabel, JButton button, JTextField... allFields) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//
//                if (unos.isEmpty()) {
//                    showError(field, errorLabel, "Ovo polje je obavezno!");
//                } else if (!unos.matches("^[A-ZŠĐČĆŽ][a-zšđčćžA-ZŠĐČĆŽ\\s]*$")) {
//                    showError(field, errorLabel, "Dozvoljena su samo slova, prvo veliko!");
//                } else {
//                    clearError(field, errorLabel);
//                }
//
//                updateButtonState(button, allFields);
//            }
//        });
//    }
//
//    // === 2. Validacija datuma sa automatskim formatiranjem ===
//    public static void validateDateField(JTextField field, JLabel errorLabel, JButton button, JTextField... allFields) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    showError(field, errorLabel, "Datum ne sme biti prazan!");
//                    updateButtonState(button, allFields);
//                    return;
//                }
//
//                try {
//                    Date dat = parseDatum(unos);
//                    SimpleDateFormat outFormat = new SimpleDateFormat("dd. MM. yyyy.");
//                    field.setText(outFormat.format(dat));
//                    clearError(field, errorLabel);
//                } catch (ParseException ex) {
//                    showError(field, errorLabel, "Neispravan format datuma!");
//                    field.requestFocus();
//                }
//                updateButtonState(button, allFields);
//            }
//        });
//    }
//
//    // === 3. Validacija broja telefona sa standardizacijom ===
//    public static void validatePhoneField(JTextField field, JLabel errorLabel, JButton button, JTextField... allFields) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim().replaceAll("\\s+", "");
//                if (unos.isEmpty()) {
//                    showError(field, errorLabel, "Telefon ne sme biti prazan!");
//                    updateButtonState(button, allFields);
//                    return;
//                }
//
//                try {
//                    String standardizovan = standardizujTelefon(unos);
//                    field.setText(standardizovan);
//                    clearError(field, errorLabel);
//                } catch (Exception ex) {
//                    showError(field, errorLabel, "Neispravan format! Broj mora početi sa 06 ili +3816.");
//                    field.requestFocus();
//                }
//                updateButtonState(button, allFields);
//            }
//        });
//    }
//
//    // === 4. Validacija e-maila ===
//    public static void validateEmailField(JTextField field, JLabel errorLabel, JButton button, JTextField... allFields) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    showError(field, errorLabel, "E-mail je obavezan!");
//                } else if (!unos.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
//                    showError(field, errorLabel, "Neispravan e-mail format!");
//                } else {
//                    clearError(field, errorLabel);
//                }
//                updateButtonState(button, allFields);
//            }
//        });
//    }
//
//    // === 5. Pomoćne metode ===
//    private static void showError(JTextField field, JLabel label, String message) {
//        field.setBackground(ERROR_COLOR);
//        field.setToolTipText(message);
//        if (label != null) {
//            label.setForeground(TEXT_ERROR_COLOR);
//            label.setText(message);
//        }
//    }
//
//    private static void clearError(JTextField field, JLabel label) {
//        field.setBackground(NORMAL_COLOR);
//        field.setToolTipText(null);
//        if (label != null) {
//            label.setText("");
//        }
//    }
//
//    // === 6. Kontrola dugmeta ===
//    private static void updateButtonState(JButton button, JTextField... fields) {
//        for (JTextField f : fields) {
//            if (f.getBackground().equals(ERROR_COLOR) || f.getText().trim().isEmpty()) {
//                button.setEnabled(false);
//                return;
//            }
//        }
//        button.setEnabled(true);
//    }
//
//    // === 7. Parsiranje datuma iz više formata ===
//    private static Date parseDatum(String dateStr) throws ParseException {
//        String[] formati = {
//            "dd.MM.yyyy", "dd.MM.yyyy.", "dd/MM/yyyy",
//            "yyyy-MM-dd", "d.M.yyyy", "dd. MM. yyyy."
//        };
//        for (String f : formati) {
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat(f);
//                sdf.setLenient(false);
//                return sdf.parse(dateStr);
//            } catch (ParseException ignored) {}
//        }
//        throw new ParseException("Nepoznat format datuma: " + dateStr, 0);
//    }
//
//    // === 8. Standardizacija telefona ===
//    private static String standardizujTelefon(String unos) throws Exception {
//        String broj = unos.trim().replaceAll("\\s+", "");
//
//        if (broj.matches("^\\+3816[0-9]{5,}$")) {
//            broj = "0" + broj.substring(3);
//        }
//
//        if (broj.matches("^06[0-9]{5,}$")) {
//            return broj;
//        } else {
//            throw new Exception("Broj mora početi sa 06 ili +3816");
//        }
//    }
//    
// --------------- treci
//    private static final Color ERROR_COLOR = new Color(255, 150, 150);
//    private static final Color NORMAL_COLOR = Color.white;
//
//    // === 1. Validacija imena i prezimena ===
//    public static void validateNameField(JTextField field, JButton button, JTextField... allFields) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//
//                // dozvoljena samo slova (i srpska), razmak i mora početi velikim slovom
//                if (unos.isEmpty() || !unos.matches("^[A-ZŠĐČĆŽ][a-zšđčćžA-ZŠĐČĆŽ\\s]*$")) {
//                    showError(field, "Dozvoljena su samo slova, prvo veliko!");
//                } else {
//                    clearError(field);
//                }
//
//                updateButtonState(button, allFields);
//            }
//        });
//    }
//
//    // === 2. Validacija datuma sa automatskim formatiranjem ===
//    public static void validateDateField(JTextField field, JButton button, JTextField... allFields) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    showError(field, "Datum ne sme biti prazan!");
//                    updateButtonState(button, allFields);
//                    return;
//                }
//
//                try {
//                    Date dat = parseDatum(unos);
//                    SimpleDateFormat outFormat = new SimpleDateFormat("dd. MM. yyyy.");
//                    field.setText(outFormat.format(dat));
//                    clearError(field);
//                } catch (ParseException ex) {
//                    showError(field, "Neispravan format datuma!");
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Uneli ste datum u neispravnom formatu!",
//                            "Greška",
//                            JOptionPane.ERROR_MESSAGE);
//                    field.requestFocus();
//                }
//                updateButtonState(button, allFields);
//            }
//        });
//    }
//
//    // === 3. Validacija broja telefona sa standardizacijom ===
//    public static void validatePhoneField(JTextField field, JButton button, JTextField... allFields) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim().replaceAll("\\s+", "");
//                if (unos.isEmpty()) {
//                    showError(field, "Telefon ne sme biti prazan!");
//                    updateButtonState(button, allFields);
//                    return;
//                }
//
//                try {
//                    String standardizovan = standardizujTelefon(unos);
//                    field.setText(standardizovan);
//                    clearError(field);
//                } catch (Exception ex) {
//                    showError(field, "Neispravan format telefona!");
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Neispravan format telefona! Broj mora početi sa 06 ili +3816.",
//                            "Greška",
//                            JOptionPane.ERROR_MESSAGE);
//                    field.requestFocus();
//                }
//                updateButtonState(button, allFields);
//            }
//        });
//    }
//
//    // === 4. Validacija e-maila ===
//    public static void validateEmailField(JTextField field, JButton button, JTextField... allFields) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty() || !unos.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
//                    showError(field, "Neispravan e-mail format!");
//                } else {
//                    clearError(field);
//                }
//                updateButtonState(button, allFields);
//            }
//        });
//    }
//
//    // === 5. Pomoćne metode ===
//    private static void showError(JTextField field, String message) {
//        field.setBackground(ERROR_COLOR);
//        field.setToolTipText(message);
//    }
//
//    private static void clearError(JTextField field) {
//        field.setBackground(NORMAL_COLOR);
//        field.setToolTipText(null);
//    }
//
//    // === 6. Omogućavanje / onemogućavanje dugmeta ===
//    private static void updateButtonState(JButton button, JTextField... fields) {
//        for (JTextField f : fields) {
//            if (f.getBackground().equals(ERROR_COLOR) || f.getText().trim().isEmpty()) {
//                button.setEnabled(false);
//                return;
//            }
//        }
//        button.setEnabled(true);
//    }
//
//    // === 7. Parsiranje datuma iz više formata ===
//    private static Date parseDatum(String dateStr) throws ParseException {
//        String[] formati = {
//            "dd.MM.yyyy", "dd.MM.yyyy.", "dd/MM/yyyy",
//            "yyyy-MM-dd", "d.M.yyyy", "dd. MM. yyyy."
//        };
//        for (String f : formati) {
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat(f);
//                sdf.setLenient(false);
//                return sdf.parse(dateStr);
//            } catch (ParseException ignored) {
//            }
//        }
//        throw new ParseException("Nepoznat format datuma: " + dateStr, 0);
//    }
//
//    // === 8. Standardizacija telefona (ispravljena verzija) ===
//    private static String standardizujTelefon(String unos) throws Exception {
//        String broj = unos.trim().replaceAll("\\s+", "");
//
//        // dozvoli +3816... i +38160... varijante
//        if (broj.matches("^\\+3816[0-9]{5,}$")) {
//            broj = "0" + broj.substring(3);
//        }
//
//        if (broj.matches("^06[0-9]{5,}$")) {
//            return broj;
//        } else {
//            throw new Exception("Broj mora početi sa 06 ili +3816");
//        }
//    }
////////
    //
    //
    /////////////////
//    private static final Color ERROR_COLOR = new Color(255, 150, 150);
//    private static final Color NORMAL_COLOR = Color.white;
//
//    // === 1. Validacija datuma sa automatskim formatiranjem ===
//    public static void validateDateField(JTextField field, JButton button, JTextField... allFields) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty()) {
//                    showError(field, "Datum ne sme biti prazan!");
//                    updateButtonState(button, allFields);
//                    return;
//                }
//                try {
//                    Date dat = parseDatum(unos);
//                    SimpleDateFormat outFormat = new SimpleDateFormat("dd. MM. yyyy.");
//                    field.setText(outFormat.format(dat));
//                    clearError(field);
//                } catch (ParseException ex) {
//                    showError(field, "Neispravan format datuma!");
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Uneli ste datum u neispravnom formatu!",
//                            "Greška",
//                            JOptionPane.ERROR_MESSAGE);
//                    field.requestFocus();
//                }
//                updateButtonState(button, allFields);
//            }
//        });
//    }
//
//    // === 2. Validacija broja telefona sa standardizacijom ===
//    public static void validatePhoneField(JTextField field, JButton button, JTextField... allFields) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim().replaceAll("\\s+", "");
//                if (unos.isEmpty()) {
//                    showError(field, "Telefon ne sme biti prazan!");
//                    updateButtonState(button, allFields);
//                    return;
//                }
//                try {
//                    String standardizovan = standardizujTelefon(unos);
//                    field.setText(standardizovan);
//                    clearError(field);
//                } catch (Exception ex) {
//                    showError(field, "Neispravan format telefona!");
//                    JOptionPane.showMessageDialog(field.getParent(),
//                            "Neispravan format telefona! Broj mora početi sa 06_ ili +381.",
//                            "Greška",
//                            JOptionPane.ERROR_MESSAGE);
//                    field.requestFocus();
//                }
//                updateButtonState(button, allFields);
//            }
//        });
//    }
//
//    // === 3. Validacija e-maila ===
//    public static void validateEmailField(JTextField field, JButton button, JTextField... allFields) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String unos = field.getText().trim();
//                if (unos.isEmpty() || !unos.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
//                    showError(field, "Neispravan e-mail format!");
//                    updateButtonState(button, allFields);
//                    return;
//                }
//                clearError(field);
//                updateButtonState(button, allFields);
//            }
//        });
//    }
//
//    // === 4. Pomoćne metode ===
//    private static void showError(JTextField field, String message) {
//        field.setBackground(ERROR_COLOR);
//        field.setToolTipText(message);
//    }
//
//    private static void clearError(JTextField field) {
//        field.setBackground(NORMAL_COLOR);
//        field.setToolTipText(null);
//    }
//
//    // === 5. Omogućavanje / onemogućavanje dugmeta ===
//    private static void updateButtonState(JButton button, JTextField... fields) {
//        for (JTextField f : fields) {
//            if (f.getBackground().equals(ERROR_COLOR) || f.getText().trim().isEmpty()) {
//                button.setEnabled(false);
//                return;
//            }
//        }
//        button.setEnabled(true);
//    }
//
//    // === 6. Parsiranje datuma iz više formata ===
//    private static Date parseDatum(String dateStr) throws ParseException {
//        String[] formati = {
//            "dd.MM.yyyy", "dd.MM.yyyy.", "dd/MM/yyyy",
//            "yyyy-MM-dd", "d.M.yyyy", "dd. MM. yyyy."
//        };
//        for (String f : formati) {
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat(f);
//                sdf.setLenient(false);
//                return sdf.parse(dateStr);
//            } catch (ParseException ignored) {}
//        }
//        throw new ParseException("Nepoznat format datuma: " + dateStr, 0);
//    }
//
//    // === 7. Standardizacija telefona ===
//    private static String standardizujTelefon(String unos) throws Exception {
//        String broj = unos.trim().replaceAll("\\s+", "");
//        if (broj.startsWith("+381")) broj = "0" + broj.substring(3);
//        if (broj.startsWith("06")) return broj;
//        throw new Exception("Broj mora početi sa 06 ili +381");
//    }
//    private static final Color ERROR_COLOR = new Color(255, 150, 150);
//    private static final Color NORMAL_COLOR = Color.white;
//
//    // === 1. Opšta tekstualna validacija (regex + poruka) ===
//    public static void validateTextField(JTextField field, String regex, String message) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String text = field.getText().trim();
//                if (!Pattern.matches(regex, text)) {
//                    showError(field, message);
//                } else {
//                    clearError(field);
//                }
//            }
//        });
//    }
//
//    // === 2. Validacija datuma ===
//    public static void validateDateField(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String text = field.getText().trim();
//                if (isValidDate(text)) {
//                    clearError(field);
//                } else {
//                    showError(field, "Unesite datum u formatu dd.MM.yyyy ili yyyy-MM-dd.");
//                }
//            }
//        });
//    }
//
//    private static boolean isValidDate(String text) {
//        if (text.isEmpty()) return false;
//        String[] formats = {"dd.MM.yyyy", "dd.MM.yyyy.", "yyyy-MM-dd", "dd/MM/yyyy", "d.M.yyyy"};
//        for (String f : formats) {
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat(f);
//                sdf.setLenient(false);
//                sdf.parse(text);
//                return true;
//            } catch (ParseException ignored) {}
//        }
//        return false;
//    }
//
//    // === 3. Validacija telefona ===
//    public static void validatePhoneField(JTextField field) {
//        field.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                String text = field.getText().trim().replaceAll("\\s+", "");
//                if (text.isEmpty()) return;
//
//                if (text.startsWith("+381")) text = "0" + text.substring(3);
//
//                if (!text.startsWith("06") || text.length() < 9 || text.length() > 12) {
//                    showError(field, "Broj mora početi sa 06 ili +381.");
//                } else {
//                    clearError(field);
//                    field.setText(text);
//                }
//            }
//        });
//    }
//
//    // === 4. Pomoćne metode ===
//    private static void showError(JTextField field, String message) {
//        field.setBackground(ERROR_COLOR);
//        field.setToolTipText(message);
//    }
//
//    private static void clearError(JTextField field) {
//        field.setBackground(NORMAL_COLOR);
//        field.setToolTipText(null);
//    }
//
//    // === 5. Provera svih polja pre slanja forme ===
//    public static boolean allFieldsValid(JTextField... fields) {
//        for (JTextField f : fields) {
//            if (f.getBackground().equals(ERROR_COLOR)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    // === 6. (Opcionalno) blokiraj dugme ako ima grešaka ===
//    public static void bindValidationToButton(JButton button, JTextField... fields) {
//        for (JTextField f : fields) {
//            f.addFocusListener(new FocusAdapter() {
//                @Override
//                public void focusLost(FocusEvent e) {
//                    button.setEnabled(allFieldsValid(fields));
//                }
//            });
//        }
//    }
}
