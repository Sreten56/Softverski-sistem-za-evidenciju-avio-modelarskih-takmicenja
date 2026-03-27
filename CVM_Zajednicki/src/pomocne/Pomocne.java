/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pomocne;

import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author Srecko
 */
public class Pomocne {

    public static String generateTemporaryPassword() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000); // Generiše broj između 100000 i 999999
        return String.valueOf(number);

    }

    public static void posalji_Email(String toEmail, String privremena_lozinka) {

        final String moj_email = "cvmsistem@gmail.com";
        final String lozinka = "mgtr atbv mteo lvhs";           //mgtr atbv mteo lvhs

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session sesija = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(moj_email, lozinka);
            }
        });

        try {
            Message poruka = new MimeMessage(sesija);
            poruka.setFrom(new InternetAddress(moj_email));
            poruka.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            poruka.setSubject("Vasa privremena lozinka!");
            poruka.setText("U ovom mejlu se nalazi vasa privremena lozinka za pristupanje sajtu. \n"
                    + "Preporucujemo da lozinku promenite nakon prvog pristupa sajtu! \n"
                    + "Vasa privremena lozinka je: " + privremena_lozinka);

            Transport.send(poruka);
            System.out.println("Mejl je poslat");

        } catch (MessagingException ex) {
            Logger.getLogger(Pomocne.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    // dyabe se mucih....
//    public static boolean posalji_Email(String toEmail, String privremena_lozinka) {
//        final String moj_email = "cvmsistem@gmail.com";
//        final String lozinka = "mgtr atbv mteo lvhs";
//
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//
//        Session sesija = Session.getInstance(props, new javax.mail.Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(moj_email, lozinka);
//            }
//        });
//
//        try {
//            Message poruka = new MimeMessage(sesija);
//            poruka.setFrom(new InternetAddress(moj_email));
//            poruka.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
//            poruka.setSubject("Vaša privremena lozinka!");
//            poruka.setText(
//                    "Poštovani,\n\n"
//                    + "U ovom mejlu se nalazi Vaša privremena lozinka za pristup aplikaciji CVM sistem.\n"
//                    + "Preporučujemo da lozinku promenite nakon prvog prijavljivanja.\n\n"
//                    + "Vaša privremena lozinka: " + privremena_lozinka + "\n\n"
//                    + "Srdačan pozdrav,\nCVM Sistem tim"
//            );
//
//            Transport.send(poruka);
//            System.out.println("✅ Mejl uspešno poslat na: " + toEmail);
//            return true;
//
//        } catch (SendFailedException ex) {
//            System.err.println("❌ Neuspešno slanje! Email adresa nije ispravna: " + toEmail);
//            return false;
//        } catch (AuthenticationFailedException ex) {
//            System.err.println("❌ Pogrešna lozinka ili Gmail blokira slanje!");
//            return false;
//        } catch (MessagingException ex) {
//            System.err.println("⚠️ Greška prilikom slanja mejla: " + ex.getMessage());
//            return false;
//        }
//    }

}
