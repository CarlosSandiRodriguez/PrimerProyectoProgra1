/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 * Esta clase es la encargada de cifrar y decifrar las contraseñas de los
 * usuarios, a su vez comprueba si existen y envia una respuesta para que su
 * ingreso sea exitoso
 *
 * @author Damian Monge
 */
import java.io.*;
import java.util.Scanner;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class CifradoCesar {

    private static final String FILE_PATH = "Lista encriptada.";

    /**
     * Este constructor solo se encarga de establecer la carpeta que se creara
     * para el archivo con los usuarios permitidos
     */
    public CifradoCesar() {
        File file = new File(FILE_PATH);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating user file: " + e.getMessage());
        }
    }

    /**
     * Cifra un texto con una clave dada respetando UTF-8
     *
     * @param text Es la contraseña del usuario
     * @param key es la llave con la que se descifra la contraseña
     * @return devuelve la contraseña encryptada
     */
    public static String encrypt(String text, int key) {
        StringBuilder encryptedText = new StringBuilder();

        for (char c : text.toCharArray()) {
            c = (char) (c + key);
            encryptedText.append(c);
        }

        return encryptedText.toString();
    }

    /**
     * Descifra un texto con una clave dada respetando UTF-8
     *
     * @param text Contraseña del usuario
     * @param key Esa la llave para decifrar la contraseña
     * @return contraseña y usuarios decifrados
     */
    public static String decrypt(String text, int key) {
        return encrypt(text, -key);
    }

    /**
     * Registra un nuevo usuario con una contraseña cifrada
     *
     * @param username el nombre ingresado por el usuario
     * @param password la contraseña a ensiptar
     * @param key la llave para desencriptar la contraseña a futuro
     * @return si es usuario existe no lo vuelve a crear y si el ususario no existe lo crea
     * @throws IOException Error a lo hora del proceso de encryptacion
     */
    public boolean registerUser(String username, String password, int key) throws IOException {

        String encryptedPassword = encrypt(password, key);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(FILE_PATH, true), StandardCharsets.UTF_8))) {
            if (!userExists(username)) {
                writer.write(username + ":" + encryptedPassword);
                writer.newLine();
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Cliente ya exite");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "El cliente no fue registrado con exito");

        }
        return false;
    }

    /**
     * Verifica si un usuario y una contraseña son válidos
     * @param username Usuario ingresado
     * @param password contraseña ingresada
     * @param key llave del cliente
     * @return verdadero  o falso dependiedo si cumple las condiciones
     * @throws IOException error a la hora de la comprobación
     */
    public static boolean loginUser(String username, String password, int key) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(FILE_PATH), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(username)) {
                    String decryptedPassword = decrypt(parts[1], key);
                    return decryptedPassword.equals(password);
                }
            }
        }
        return false;
    }
/**
 * Verifica si el login es correcto o no
 * @param username nombre del usuario
 * @return verdadero o falso dependiendo si existe o no
 * @throws IOException error en la comprobación
 */
    public static boolean userExists(String username) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(FILE_PATH), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }

}
