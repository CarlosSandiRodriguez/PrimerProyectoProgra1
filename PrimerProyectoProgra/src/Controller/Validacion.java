/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Clase que se encarga de hacer las valiudaciones, decifrar, registrar y
 * permitir el acceso
 *
 * @author Damian
 */
public class Validacion {

    private static final String FILE_PATH = "Lista encriptada.";

    /**
     * solo establece donde se crea el archivo
     */
    public Validacion() {
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
     * Registra un usuario
     *
     * @param username nombre
     * @param password comtraseña
     * @return verdadero o falso
     */
    public boolean registerUser(String username, String password) {

        Persona person = new Persona(cifrar(username, 3), cifrar(password, 3));
        if (isUserExists(person.getUsername())) {
            return false; // Usuario ya existe
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(person.toString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error registering user: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Perimte la el acceso al servidor si existe en el registro
     *
     * @param username nombre
     * @param password contraseña
     * @return verdadero o falso
     */
    public boolean loginUser(String username, String password) {
        String name = cifrar(username, 3);

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(username)) {
                    String decryptedPassword = descifrar(parts[1], 3);
                    return decryptedPassword.equals(password);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Validacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Revisa y decifra la contraseña con la llave creada
     *
     * @param username nombre
     * @param password contraseña
     * @param key llave para decifrar
     * @return verdadero o falso
     * @throws IOException error al momento de decifrar el contenido
     */
    public static boolean loginUser(String username, String password, int key) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(descifrar(username, 3))) {
                    String decryptedPassword = descifrar(parts[1], key);
                    return decryptedPassword.equals(password);
                }
            }
        }
        return false;
    }

    /**
     * Revisa la existencia de un usuario
     *
     * @param username nombre
     * @return verdadero o falso
     */
    private boolean isUserExists(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                Persona persona = Persona.fromString(line);
                if (persona != null && persona.getUsername().equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking user existence: " + e.getMessage());
        }
        return false;
    }

    /**
     * Decifra el mensaje que se crea para cada usuario
     *
     * @param mensajeCifrado mensaje en clave
     * @param clave clave que indica como decifrar
     * @return el nombre y contraseña de la persona ya decifrada
     */
    public static String descifrar(String mensajeCifrado, int clave) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < mensajeCifrado.length(); i++) {
            char caracter = mensajeCifrado.charAt(i);
            if (Character.isLetter(caracter)) {
                char base = Character.isUpperCase(caracter) ? 'A' : 'a';
                caracter = (char) (((caracter - base - clave + 26) % 26) + base);
            }
            resultado.append(caracter);
        }
        return resultado.toString();
    }

    /**
     * cifra para efectos de registrarce
     *
     * @param mensaje el mensaje que se crea con el nombre y la contraseña
     * @param clave la clave que ayudara para un futuro
     * @return el contenido cifrado
     */
    public static String cifrar(String mensaje, int clave) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < mensaje.length(); i++) {
            char caracter = mensaje.charAt(i);
            if (Character.isLetter(caracter)) {
                char base = Character.isUpperCase(caracter) ? 'A' : 'a';
                caracter = (char) (((caracter - base + clave) % 26) + base);
            }
            resultado.append(caracter);
        }
        return resultado.toString();
    }

    /**
     * Revisa si existe el usuario
     *
     * @param username nombre
     * @return verdadero o falso
     * @throws IOException error en la comprobacion
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
