/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 * Clase que solo es un modelo para generaizar
 *
 * @author damian
 */
public class Persona {

    private String username;
    private String password;

    /**
     * Establece los datos
     *
     * @param username nombre del usuario
     * @param password contraseña
     */
    public Persona(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * FSFDFS
     *
     * @return DEASD
     */
    public String getUsername() {
        return username;
    }

    /**
     * ADASD A
     *
     * @return ADASDAETRA
     */
    public String getPassword() {
        return password;
    }

    /**
     * ADASD A
     *
     * @return ADSADASD
     */
    @Override
    public String toString() {
        return username + ":" + password;
    }

    /**
     * Metodo para crear un nuevo usuario en caso se registrarce
     *
     * @param userString nombre y contraseña
     * @return falso o una nueva persona para crear
     */
    public static Persona fromString(String userString) {
        String[] parts = userString.split(":");
        if (parts.length == 2) {
            return new Persona(parts[0], parts[1]);
        }
        return null;
    }
}
