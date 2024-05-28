package Controller;

import java.io.*;
import java.net.*;

/**
 * Esta clase se encarga de mantener las conecciones delmcliente, compruba si un
 * archivo existe en el servidor y se lo envia al cliente 
 * @author Carlos
 * @author Marcos
 */
public class  ClientHandler implements Runnable {

    private Socket clientSocket;
    private final String FILE_DIRECTORY = "C:\\UCR\\proyecto 2\\EjemploServer\\Contenido";
    /**
     * Establ4ece cual es el socket
     * @param socket coneccion con el cliente
     */
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }
/**
 * Metodo que maneja las peticiones contantes del cliente
 */
    public void run() {
     try {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        OutputStream outputStream = clientSocket.getOutputStream();

        // Enviar mensaje de bienvenida al cliente
        sendWelcomeMessage(out);

        String fileName;
        while ((fileName = in.readLine()) != null) {
            sendFile(fileName, out, outputStream);
        }
    } catch (IOException e) {
        e.printStackTrace();
 
    }
    }

  /**
   * Mensaje de bienvenida
   * @param out La respuesta del server
   */
    private void sendWelcomeMessage(PrintWriter out) {
        out.println("bienvenido");
    }

    /**
     * Metodo que se encarga de enviar en bloqus de 1024 bytes un archivo 
     * seleccionado por el cliente 
     * @param fileNamem nombre del archivo
     * @param out respuesta del servidor
     * @param outputStream coneccion con el cliente 
     */
    private void sendFile(String fileName, PrintWriter out, OutputStream outputStream) {
         try {
        File fileToSend = new File(FILE_DIRECTORY + "/" + fileName);
        if (fileToSend.exists()) {
            out.println("EXISTE"); 
            FileInputStream fileInputStream = new FileInputStream(fileToSend);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                outputStream.flush();
            }
            System.out.println("Archivo enviado: " + fileName);
        } else {
            out.println("NO_EXISTE"); 
            System.out.println("El archivo no existe en el servidor: " + fileName);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
}
