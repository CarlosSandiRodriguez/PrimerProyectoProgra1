package Reproductores;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import javafx.scene.control.Slider;
import view.BetaInterfazDeUsuario;

/**
 * Clase que representa el reproductor de video con biblioteca.
 * Permite al usuario seleccionar y reproducir videos de una lista predefinida.
 */
public class VideoPlayerConBiblioteca extends Application {

    private MediaPlayer reproductor;
    private MediaView vistaReproductor;
    private Scene escenaSeleccion;
    private VBox contenedorSeleccion;
    private Scene escenaReproductor;
    private StackPane contenedorReproductor;
    private Slider barraProgreso;
    private Stage escenarioPrincipal;
    private TableView<File> tablaVideos; // Tabla para mostrar los videos disponibles

    @Override
    public void start(Stage primaryStage) {
        this.escenarioPrincipal = primaryStage;

        // Configuración de la tabla de videos
        tablaVideos = new TableView<>();
        configurarTabla();

        // Carga los videos en la tabla desde la carpeta 'videos'
        cargarVideos(System.getProperty("user.dir") + File.separator + "descargas");

        
        Button botonHola = new Button("Volver");
        botonHola.setOnAction(e -> {escenarioPrincipal.close();
        BetaInterfazDeUsuario betaInterfazDeUsuario = new BetaInterfazDeUsuario();
        betaInterfazDeUsuario.setVisible(true);
                }
        );
        // Configuración inicial de la escena de selección
        contenedorSeleccion = new VBox(20, tablaVideos, botonHola);
        contenedorSeleccion.setAlignment(Pos.CENTER);
        contenedorSeleccion.setPadding(new Insets(10));
        escenaSeleccion = new Scene(contenedorSeleccion, 640, 480);

        primaryStage.setTitle("Reproductor de Video con Biblioteca");
        primaryStage.setScene(escenaSeleccion);
        primaryStage.show();
    }

    private void configurarTabla() {
        TableColumn<File, String> columnaNombre = new TableColumn<>("Nombre del Video");
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        tablaVideos.getColumns().add(columnaNombre);
        tablaVideos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                reproducirVideo(newSelection);
            }
        });
    }

    private void cargarVideos(String directorio) {
        File dir = new File(directorio);
        File[] archivos = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".mp4"));
        if (archivos != null) {
            ObservableList<File> listaVideos = FXCollections.observableArrayList(archivos);
            tablaVideos.setItems(listaVideos);
        }
    }

    private void reproducirVideo(File archivoVideo) {
        if (reproductor != null) {
            reproductor.stop();
        }
        Media media = new Media(archivoVideo.toURI().toString());
        reproductor = new MediaPlayer(media);
        vistaReproductor = new MediaView(reproductor);

        configurarReproductor(); // Asegúrate de configurar el reproductor como antes

        // Configuración del layout del reproductor con controles
        contenedorReproductor = new StackPane(vistaReproductor);
        configurarControlesReproductor(); // Añade todos los controles como antes

        escenaReproductor = new Scene(contenedorReproductor, 640, 480);
        escenarioPrincipal.setScene(escenaReproductor);
        reproductor.play();
    }

    private void configurarReproductor() {
        barraProgreso = new Slider();
        reproductor.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!barraProgreso.isValueChanging()) {
                double total = reproductor.getTotalDuration().toSeconds();
                double actual = newTime.toSeconds();
                barraProgreso.setValue((actual / total) * 100);
            }
        });

        reproductor.setOnReady(() -> {
            barraProgreso.setMin(0);
            barraProgreso.setMax(100);
            barraProgreso.setValue(0);
        });

        barraProgreso.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                reproductor.seek(reproductor.getTotalDuration().multiply(barraProgreso.getValue() / 100.0));
            }
        });

        reproductor.setOnEndOfMedia(() -> {
            reproductor.stop();
            escenarioPrincipal.setScene(escenaSeleccion);
        });
    }

    private void configurarControlesReproductor() {
        Button botonPausarReproducir = new Button("Play");
        botonPausarReproducir.setOnAction(ev -> alternarPausaReproduccion(botonPausarReproducir));

        Button botonAdelantar = new Button(">> 10s");
        botonAdelantar.setOnAction(ev -> reproductor.seek(reproductor.getCurrentTime().add(javafx.util.Duration.seconds(10))));

        Button botonRetroceder = new Button("<< 10s");
        botonRetroceder.setOnAction(ev -> reproductor.seek(reproductor.getCurrentTime().subtract(javafx.util.Duration.seconds(10))));
        
        Button botonVolver = new Button("Volver");
        botonVolver.setOnAction(ev -> {
            reproductor.stop();
            escenarioPrincipal.setScene(escenaSeleccion);
            // Aquí deberías llamar a BetaInterfazDeUsuario.setVisible(true) si ese método está disponible
        });
        

        StackPane.setAlignment(botonPausarReproducir, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(botonRetroceder, Pos.BOTTOM_LEFT);
        StackPane.setAlignment(botonAdelantar, Pos.BOTTOM_RIGHT);
        StackPane.setAlignment(barraProgreso, Pos.BOTTOM_CENTER);
        StackPane.setMargin(barraProgreso, new Insets(0, 0, 50, 0)); // Ajuste de margen para subir la barra
        StackPane.setAlignment(botonVolver, Pos.TOP_LEFT);
        contenedorReproductor.getChildren().addAll(botonPausarReproducir, botonRetroceder, botonAdelantar,botonVolver, barraProgreso);
    }

    private void alternarPausaReproduccion(Button boton) {
        if (reproductor.getStatus() == MediaPlayer.Status.PLAYING) {
            reproductor.pause();
            boton.setText("Play");
        } else {
            reproductor.play();
            boton.setText("Pausa");
        }
    }

    public void ejecutar() {
        launch();
    }
}
