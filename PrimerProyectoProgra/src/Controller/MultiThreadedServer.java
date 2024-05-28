/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.io.*;
import java.net.*;

import java.io.*;
import java.net.*;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Clase que solo ayuda a establecer el servidor y el puerto, 
 * con un limite de 10 hilos simultaneamente
 * @author sandi
 * @author marcos
 */
public class MultiThreadedServer {

    private static final int PORT = 8080;
    private static ExecutorService pool = Executors.newFixedThreadPool(10); // Pool de hilos para manejar múltiples clientes
    /**
     * Metodo que solo establece la conexion cliente servidor
     * @param args exepccion del metodo
     * @throws IOException eeror a la hora del que se pierde la conexion
     */

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        System.out.println("Servidor escuchando en el puerto " + PORT);

        try {
            while (true) {
                Socket clientSocket = listener.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                pool.execute(clientHandler);
            }
        } finally {
            listener.close();
        }
    }
}