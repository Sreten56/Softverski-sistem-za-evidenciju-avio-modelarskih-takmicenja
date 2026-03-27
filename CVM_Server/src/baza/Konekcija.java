/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baza;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Srecko
 */
public class Konekcija {

    private static Konekcija instance;
    private Connection connection;

    private Konekcija() {
        try {
            Properties properties = new Properties();

            // ucitavanje iz classpath-a (resources folder)
            InputStream input = getClass().getClassLoader().getResourceAsStream("resources/dbconfig.properties");
            if (input == null) {
                throw new FileNotFoundException("Nije pronađen dbconfig.properties u resources folderu!");
            }

            properties.load(input);
            String url = properties.getProperty("url");
            String user = properties.getProperty("username");
            String pass = properties.getProperty("password");

            connection = DriverManager.getConnection(url, user, pass);
            connection.setAutoCommit(false);

            System.out.println("Konekcija sa bazom uspešno uspostavljena!");

        } catch (IOException | SQLException ex) {
            Logger.getLogger(Konekcija.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Konekcija getInstance() {
        if (instance == null) {
            instance = new Konekcija();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
//    private static Konekcija instance;
//    private Connection connection;
//
//    private Konekcija() {
//        
//        try {
//            String url = "jdbc:mysql://localhost:3306/ps_projekat";
//            connection = DriverManager.getConnection(url, "root", "");
//            connection.setAutoCommit(false);
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(Konekcija.class.getName()).log(Level.SEVERE, null, ex);
//        }     
//    }
//
//    public static Konekcija getInstance() {
//        if (instance == null) {
//            instance = new Konekcija();
//        }
//        return instance;
//    }
//
//    public Connection getConnection() {
//        return connection;
//    }

}
