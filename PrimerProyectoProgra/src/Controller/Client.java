package Controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import view.InterfazLogin;
import view.BetaInterfazDeUsuario;

/**
 * Esta clase es con la que el cliente va a intecatuar con el servidor, llama a
 * la intefaces graficas, recive la respuesta del server en caso de querer
 * descargar alog y crea la intefaz donde aparece el contenido del servidor
 *
 * @author carlos
 * @author marcos
 */

public class Client extends JFrame {

    private static final String SERVER_ADDRESS = "25.8.236.112";
    private static final int SERVER_PORT = 8080;
    private static final String DOWNLOAD_DIRECTORY = System.getProperty("user.dir") + File.separator + "descargas";
    private JTable tablaDeContenido;
    private DefaultTableModel modeloDeTabla;
    private String folderGuardar = "C:\\UCR\\proyecto 2\\EjemploServer\\Contenido";
    
    
    /**
     * Este constructor establece las caracteristicas del frame donde se mostrara el contenido
     */
    public Client() {
        super("Contenido de la Carpeta");
        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        modeloDeTabla = new DefaultTableModel();
        modeloDeTabla.addColumn("Nombre del Archivo");

        String[] fileNames = {"Barracuda.mp3", "Cuestionario Actualidades 2019.pdf",
            "DatosCuriosos.txt", "EIPR_Tema02.pdf", "Eva1.mp3",
            "GTA.mp4", "GTAIV.mp4", "hOLA.txt", "hosts.txt",
            "listaspilascolas.pdf", "Mirame.txt", "Perro.txt"};
        for (String fileName : fileNames) {
            modeloDeTabla.addRow(new Object[]{fileName});
        }

        tablaDeContenido = new JTable(modeloDeTabla);
        tablaDeContenido.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tablaDeContenido);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Atrás");
        JButton downloadButton = new JButton("Descargar");
        JButton refreshButton = new JButton("Actualizar");

        buttonPanel.add(backButton);
        buttonPanel.add(downloadButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(this::backButtonAction);
        downloadButton.addActionListener(this::downloadButtonAction);
        refreshButton.addActionListener(this::refreshButtonAction);
    }

    /**
     * Esta son las acciones cuando se haga clic en el boton de retroceso
     * @param event Crear y mostrar la nueva ventana de interfaz de usuario
     */
    private void backButtonAction(ActionEvent event) {
        this.dispose();

        // 
        BetaInterfazDeUsuario nuevaVentana = new BetaInterfazDeUsuario();
        nuevaVentana.setVisible(true);
    }

    /**
     * Eventos al momento de hacer clic en el boton de descarga
     * @param event toma el nombre del archivo seleccionado crea un hilo y lo 
     * envia a ClientHandler para ver si hay respuesta
     */
    private void downloadButtonAction(ActionEvent event) {
        int selectedRow = tablaDeContenido.getSelectedRow();
        if (selectedRow != -1) {
            String fileName = (String) tablaDeContenido.getValueAt(selectedRow, 0);


            Thread downloadThread = new Thread(() -> {
                try {
                    Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    out.println(fileName);

                    receiveFile(fileName, socket.getInputStream());

                    out.close();
                    in.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            downloadThread.start(); 
        } else {
            System.out.println("Por favor, selecciona un archivo para descargar.");
        }
    }

    /**
     * Evento al momento de poresionar el boton de actualizar
     * @param event borra el modelo de la tabla actual y carga uno nuevo
     */
    private void refreshButtonAction(ActionEvent event) {
        modeloDeTabla.setRowCount(0); // Limpiar el modelo
        loadFolderContent(folderGuardar); // Cargar de nuevo los archivos
        System.out.println("Contenido Actualizado");
    }

    /**
     * Se encarga de recargar el contenido disponible para la tabla
     * @param folderPath la ruta donde se guardan los archivos descargados
     */
    private void loadFolderContent(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                modeloDeTabla.addRow(new Object[]{fileName});
            }
        }
    }

/**
 * Metodo que carga y envia mensajes al usuario caundo se conecte a un servidor
 * @param args exepcciones del metodo
 */
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Conectado al servidor." + "Con la ip " + SERVER_ADDRESS + " y en el puerto " + SERVER_PORT);

            // Leer el mensaje de bienvenida del servidor
            System.out.println("Mensaje del servidor: " + in.readLine());

            InterfazLogin interfazUsuario = new InterfazLogin();
            interfazUsuario.setVisible(true);

            String userInput;
            while ((userInput = input.readLine()) != null) {
                out.println(userInput); // Enviar el nombre del archivo al servidor

                String response = in.readLine();
                if (response.equals("EXISTE")) {
                    receiveFile(userInput, socket.getInputStream());
                    System.out.println("archivo recivido");
                } else {
                    System.out.println("El archivo no existe en el servidor.");
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para recibir un archivo del servidor en bloqoes de 1024 bytes
     * @param fileName nombre del archivo a descargar 
     * @param inputStream conexion con el socket
     */
    private static void receiveFile(String fileName, InputStream inputStream) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(DOWNLOAD_DIRECTORY + "/" + fileName);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, bytesRead);
                bufferedOutputStream.flush();
            }

            System.out.println("Archivo recibido y guardado: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
