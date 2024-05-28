/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Reproductores;

import com.sun.glass.ui.Cursor;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javazoom.jl.player.Player;

/**
 *
 * @author mjmar
 */
public class InterfazReproductor extends javax.swing.JFrame {

    private ArrayList<File> playlist;
    private javazoom.jl.player.Player player;
    private int currentSongIndex;
    private int lastHighlightedRow = -1;
    private long pausedPosition = 0;
    private DefaultTableModel tableModel;

    /**
     * Creates new form InterfazReproductor
     */
    public InterfazReproductor() {
        initComponents();
        setLocationRelativeTo(null);

        playlist = new ArrayList<>();
        tableModel = new DefaultTableModel(new String[]{"Canción"}, 0); // Inicializa el modelo de la tabla aquí
        playlistTable.setModel(tableModel); // Establece el modelo de la tabla en la tabla de la lista de reproducción

        cargarPlaylist(new File(System.getProperty("user.dir") + File.separator + "descargas"));

    }

    private void cargarPlaylist(File directorio) {
        File[] archivos = directorio.listFiles();
        for (File archivo : archivos) {
            if (archivo.isFile() && archivo.getName().toLowerCase().endsWith(".mp3")) {
                playlist.add(archivo);
                tableModel.addRow(new Object[]{archivo.getName()});
            }
        }
    }

    private void reproducir() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    if (player != null && !player.isComplete()) {
                        player.close();
                    }
                    int selectedRow = playlistTable.getSelectedRow();
                    if (selectedRow != -1) {
                        playlistTable.removeRowSelectionInterval(
                                selectedRow, selectedRow);
                    }
                    if (lastHighlightedRow != -1) {
                        playlistTable.removeRowSelectionInterval(
                                lastHighlightedRow, lastHighlightedRow);
                    }
                    lastHighlightedRow = currentSongIndex;
                    playlistTable.addRowSelectionInterval(currentSongIndex,
                            currentSongIndex);

                    // Si hay una posición de pausa almacenada, establece
                    //la posición de reproducción antes de iniciar la reproducción
                    if (pausedPosition > 0) {
                        FileInputStream fis = new FileInputStream(playlist.get(
                                currentSongIndex));
                        fis.skip(pausedPosition);
                        player = new Player(fis);
                        player.play();
                    } else {
                        player = new Player(new FileInputStream(playlist.get(
                                currentSongIndex)));
                        player.play();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        worker.execute();
    }

    private void pausar() {
        if (player != null) {
            // Almacena la posición de reproducción actual al pausar
            pausedPosition = player.getPosition();
            player.close();
        }
    }

    private void detener() {
        pausar();
        currentSongIndex = 0;
        pausedPosition = 0; // Restablece la posición de pausa al detener la reproducción
    }

    private void reproducirAnterior() {
        if (currentSongIndex > 0) {
            currentSongIndex--;
            reproducir();
        }
    }

    private void reproducirSiguiente() {
        if (currentSongIndex < playlist.size() - 1) {
            currentSongIndex++;
            reproducir();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        Fondo = new javax.swing.JPanel();
        Playbtn = new javax.swing.JButton();
        Pausebtn = new javax.swing.JButton();
        Stopbtn = new javax.swing.JButton();
        Prevbtn = new javax.swing.JButton();
        Nextbtn = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        playlistTable = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Fondo.setBackground(new java.awt.Color(51, 51, 51));
        Fondo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Playbtn.setText("Play");
        Playbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlaybtnActionPerformed(evt);
            }
        });
        Fondo.add(Playbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        Pausebtn.setText("Pause");
        Pausebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PausebtnActionPerformed(evt);
            }
        });
        Fondo.add(Pausebtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 300, -1, -1));

        Stopbtn.setText("Stop");
        Stopbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StopbtnActionPerformed(evt);
            }
        });
        Fondo.add(Stopbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 300, -1, -1));

        Prevbtn.setText("Prev");
        Prevbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrevbtnActionPerformed(evt);
            }
        });
        Fondo.add(Prevbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 300, -1, -1));

        Nextbtn.setText("Next");
        Nextbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NextbtnActionPerformed(evt);
            }
        });
        Fondo.add(Nextbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 300, -1, -1));

        playlistTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(playlistTable);

        Fondo.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 400, 250));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Fondo, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Fondo, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PlaybtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlaybtnActionPerformed
        // TODO add your handling code here:
        reproducir();
    }//GEN-LAST:event_PlaybtnActionPerformed

    private void PausebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PausebtnActionPerformed
        // TODO add your handling code here:
        pausar();
    }//GEN-LAST:event_PausebtnActionPerformed

    private void StopbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StopbtnActionPerformed
        // TODO add your handling code here:
        detener();
    }//GEN-LAST:event_StopbtnActionPerformed

    private void PrevbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrevbtnActionPerformed
        // TODO add your handling code here:
        reproducirAnterior();
    }//GEN-LAST:event_PrevbtnActionPerformed

    private void NextbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NextbtnActionPerformed
        // TODO add your handling code here:
        reproducirSiguiente();
    }//GEN-LAST:event_NextbtnActionPerformed

    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Fondo;
    private javax.swing.JButton Nextbtn;
    private javax.swing.JButton Pausebtn;
    private javax.swing.JButton Playbtn;
    private javax.swing.JButton Prevbtn;
    private javax.swing.JButton Stopbtn;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable playlistTable;
    // End of variables declaration//GEN-END:variables
}
