/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package lexismanage.vista;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;
import lexismanage.Conexion;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;




/**
 *
 * @author alexi
 */
public class MenuEstadisticas extends javax.swing.JPanel {

    /**
     * Creates new form usuarios
     */
    public MenuEstadisticas() {
        initComponents();
    }

public String obtenerEstadisticas(Date fechaInicio, Date fechaFin) {
    StringBuilder contenidoPDF = new StringBuilder();
    try {
        Conexion conn = new Conexion();
        Connection connection = conn.conectar(); 

        // 1. Total de ingresos
        String sqlTotalIngresos = "SELECT SUM(monto) FROM Ingresos WHERE fecha BETWEEN ? AND ?";
        PreparedStatement psTotalIngresos = connection.prepareStatement(sqlTotalIngresos);
        psTotalIngresos.setDate(1, new java.sql.Date(fechaInicio.getTime()));
        psTotalIngresos.setDate(2, new java.sql.Date(fechaFin.getTime()));
        ResultSet rsTotalIngresos = psTotalIngresos.executeQuery();
        if (rsTotalIngresos.next()) {
            double totalIngresos = rsTotalIngresos.getDouble(1);
            lblTotalIngresos.setText("Total de Ingresos: \n$" + totalIngresos);
            contenidoPDF.append("Total de Ingresos: $").append(totalIngresos).append("\n");
        }

        // 2. Ingresos por membresía
        String sqlIngresosPorMembresia = "SELECT nombre, SUM(Ingresos.monto) FROM Membresia " +
                                         "JOIN Pago ON Membresia.id_membresia = Pago.id_membresia " +
                                         "JOIN Ingresos ON Pago.id_pago = Ingresos.id_pago " +
                                         "WHERE Ingresos.fecha BETWEEN ? AND ? GROUP BY nombre";
        PreparedStatement psIngresosPorMembresia = connection.prepareStatement(sqlIngresosPorMembresia);
        psIngresosPorMembresia.setDate(1, new java.sql.Date(fechaInicio.getTime()));
        psIngresosPorMembresia.setDate(2, new java.sql.Date(fechaFin.getTime()));
        ResultSet rsIngresosPorMembresia = psIngresosPorMembresia.executeQuery();
        StringBuilder sbMembresia = new StringBuilder();
        while (rsIngresosPorMembresia.next()) {
            String nombreMembresia = rsIngresosPorMembresia.getString(1);
            double ingresos = rsIngresosPorMembresia.getDouble(2);
            sbMembresia.append(nombreMembresia).append(": $").append(ingresos).append("\n");
        }
        lblIngresosPorMembresia.setText("Ingresos por Membresía:\n" + sbMembresia.toString());
        contenidoPDF.append("Ingresos por Membresía:\n").append(sbMembresia.toString()).append("\n");

        // 3. Asistencia total de usuarios
        String sqlTotalAsistencias = "SELECT COUNT(*) FROM Acceso WHERE fecha_acceso BETWEEN ? AND ?";
        PreparedStatement psTotalAsistencias = connection.prepareStatement(sqlTotalAsistencias);
        psTotalAsistencias.setDate(1, new java.sql.Date(fechaInicio.getTime()));
        psTotalAsistencias.setDate(2, new java.sql.Date(fechaFin.getTime()));
        ResultSet rsTotalAsistencias = psTotalAsistencias.executeQuery();
        if (rsTotalAsistencias.next()) {
            int totalAsistencias = rsTotalAsistencias.getInt(1);
            lblTotalAsistencias.setText("Total de Asistencias: \n" + totalAsistencias);
            contenidoPDF.append("Total de Asistencias: ").append(totalAsistencias).append("\n");
        }

        // 4. Promedio de asistencia diaria
        String sqlPromedioAsistencia = "SELECT AVG(cuenta_diaria) FROM (SELECT COUNT(*) AS cuenta_diaria " +
                                       "FROM Acceso WHERE fecha_acceso BETWEEN ? AND ? " +
                                       "GROUP BY DATE(fecha_acceso)) AS AsistenciasPorDia";
        PreparedStatement psPromedioAsistencia = connection.prepareStatement(sqlPromedioAsistencia);
        psPromedioAsistencia.setDate(1, new java.sql.Date(fechaInicio.getTime()));
        psPromedioAsistencia.setDate(2, new java.sql.Date(fechaFin.getTime()));
        ResultSet rsPromedioAsistencia = psPromedioAsistencia.executeQuery();
        if (rsPromedioAsistencia.next()) {
            double promedioAsistencia = rsPromedioAsistencia.getDouble(1);
            lblPromedioAsistencia.setText("Promedio de Asistencia Diaria: \n" + promedioAsistencia);
            contenidoPDF.append("Promedio de Asistencia Diaria: ").append(promedioAsistencia).append("\n");
        }

        // 5. Total de pagos realizados
        String sqlTotalPagos = "SELECT COUNT(*) FROM Pago WHERE fecha_inicio BETWEEN ? AND ?";
        PreparedStatement psTotalPagos = connection.prepareStatement(sqlTotalPagos);
        psTotalPagos.setDate(1, new java.sql.Date(fechaInicio.getTime()));
        psTotalPagos.setDate(2, new java.sql.Date(fechaFin.getTime()));
        ResultSet rsTotalPagos = psTotalPagos.executeQuery();
        if (rsTotalPagos.next()) {
            int totalPagos = rsTotalPagos.getInt(1);
            lblTotalPagos.setText("Total de Pagos: \n" + totalPagos);
            contenidoPDF.append("Total de Pagos: ").append(totalPagos).append("\n");
        }

        // 6. Ingresos por método de pago
        String sqlIngresosPorMetodoPago = "SELECT MetodoPago.metodo, SUM(Ingresos.monto) " +
                                          "FROM MetodoPago JOIN Pago ON MetodoPago.id_metodo_pago = Pago.id_metodo_pago " +
                                          "JOIN Ingresos ON Pago.id_pago = Ingresos.id_pago " +
                                          "WHERE Ingresos.fecha BETWEEN ? AND ? " +
                                          "GROUP BY MetodoPago.metodo";
        PreparedStatement psIngresosPorMetodoPago = connection.prepareStatement(sqlIngresosPorMetodoPago);
        psIngresosPorMetodoPago.setDate(1, new java.sql.Date(fechaInicio.getTime()));
        psIngresosPorMetodoPago.setDate(2, new java.sql.Date(fechaFin.getTime()));
        ResultSet rsIngresosPorMetodoPago = psIngresosPorMetodoPago.executeQuery();
        StringBuilder sbMetodosPago = new StringBuilder();
        while (rsIngresosPorMetodoPago.next()) {
            String metodoPago = rsIngresosPorMetodoPago.getString(1);
            double ingresos = rsIngresosPorMetodoPago.getDouble(2);
            sbMetodosPago.append(metodoPago).append(": $").append(ingresos).append("\n");
        }
        lblIngresosPorMetodoPago.setText("Ingresos por Método de Pago:\n" + sbMetodosPago.toString());
        contenidoPDF.append("Ingresos por Método de Pago:\n").append(sbMetodosPago.toString()).append("\n");

        // Cerrar conexiones y consultas
        psTotalIngresos.close();
        psIngresosPorMembresia.close();
        psTotalAsistencias.close();
        psPromedioAsistencia.close();
        psTotalPagos.close();
        psIngresosPorMetodoPago.close();
        conn.cerrarConexion();

    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return contenidoPDF.toString(); // Retorna el contenido formateado para el PDF
}

private void crearPDF(String contenido, String ruta) {
    Document document = new Document();
    try {
        // Crear el archivo PDF
        PdfWriter.getInstance(document, new FileOutputStream(ruta));
        document.open();
        document.add(new Paragraph(contenido)); // Agregar el contenido
        JOptionPane.showMessageDialog(null, "PDF generado exitosamente.");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al generar el PDF: " + e.getMessage());
    } finally {
        // Asegurarse de que el documento se cierre incluso si hay un error
        if (document.isOpen()) {
            document.close();
        }
    }
}



private String generarContenidoPDF(Date fechaInicio, Date fechaFin) {
    StringBuilder contenido = new StringBuilder();
    
    contenido.append("Estadísticas desde ").append(fechaInicio).append(" hasta ").append(fechaFin).append("\n");
    contenido.append(lblTotalIngresos.getText()).append("\n");
    contenido.append(lblIngresosPorMembresia.getText()).append("\n");
    contenido.append(lblTotalAsistencias.getText()).append("\n");
    contenido.append(lblPromedioAsistencia.getText()).append("\n");
    contenido.append(lblTotalPagos.getText()).append("\n");
    contenido.append(lblIngresosPorMetodoPago.getText()).append("\n");
    return contenido.toString();
}



/**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        fechainicio = new com.toedter.calendar.JDateChooser();
        fechafin = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblPromedioAsistencia = new javax.swing.JLabel();
        lblTotalAsistencias = new javax.swing.JLabel();
        lblTotalIngresos = new javax.swing.JLabel();
        lblIngresosPorMembresia = new javax.swing.JLabel();
        verREPORTE = new javax.swing.JButton();
        descargarreporte = new javax.swing.JButton();
        lblIngresosPorMetodoPago = new javax.swing.JLabel();
        lblTotalPagos = new javax.swing.JLabel();

        setBackground(new java.awt.Color(10, 16, 30));

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 3, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("A :");

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 3, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Desde :");

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 3, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Selecciona un Rango de Tiempo");

        jLabel6.setFont(new java.awt.Font("Segoe UI Semibold", 3, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Asistencia");

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 3, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Ingresos");

        lblPromedioAsistencia.setFont(new java.awt.Font("Segoe UI Semibold", 3, 24)); // NOI18N
        lblPromedioAsistencia.setForeground(new java.awt.Color(255, 255, 255));
        lblPromedioAsistencia.setText("Promedio Asistencia");

        lblTotalAsistencias.setFont(new java.awt.Font("Segoe UI Semibold", 3, 24)); // NOI18N
        lblTotalAsistencias.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalAsistencias.setText("Total de Asistencias: ");

        lblTotalIngresos.setFont(new java.awt.Font("Segoe UI Semibold", 3, 24)); // NOI18N
        lblTotalIngresos.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalIngresos.setText("Total Ingresos:");

        lblIngresosPorMembresia.setFont(new java.awt.Font("Segoe UI Semibold", 3, 24)); // NOI18N
        lblIngresosPorMembresia.setForeground(new java.awt.Color(255, 255, 255));
        lblIngresosPorMembresia.setText("Ingresos Por Tipo de Membresia");

        verREPORTE.setBackground(new java.awt.Color(255, 77, 88));
        verREPORTE.setFont(new java.awt.Font("Yu Gothic UI", 3, 18)); // NOI18N
        verREPORTE.setForeground(new java.awt.Color(242, 242, 242));
        verREPORTE.setText("Ver Estadisticas");
        verREPORTE.setBorder(null);
        verREPORTE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verREPORTEActionPerformed(evt);
            }
        });

        descargarreporte.setFont(new java.awt.Font("Yu Gothic UI", 3, 18)); // NOI18N
        descargarreporte.setForeground(new java.awt.Color(255, 77, 88));
        descargarreporte.setText("Descargar Reporte");
        descargarreporte.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        descargarreporte.setBorderPainted(false);
        descargarreporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descargarreporteActionPerformed(evt);
            }
        });

        lblIngresosPorMetodoPago.setFont(new java.awt.Font("Segoe UI Semibold", 3, 24)); // NOI18N
        lblIngresosPorMetodoPago.setForeground(new java.awt.Color(255, 255, 255));
        lblIngresosPorMetodoPago.setText("Ingresos Por Metodo de Pago");

        lblTotalPagos.setFont(new java.awt.Font("Segoe UI Semibold", 3, 24)); // NOI18N
        lblTotalPagos.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalPagos.setText("Total Ingresos:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(246, 246, 246))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPromedioAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalAsistencias, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblTotalIngresos, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(107, 107, 107))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(85, 85, 85)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblIngresosPorMetodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblIngresosPorMembresia, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fechainicio, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(fechafin, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(verREPORTE, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(descargarreporte, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57))
            .addGroup(layout.createSequentialGroup()
                .addGap(504, 504, 504)
                .addComponent(lblTotalPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fechainicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(fechafin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(verREPORTE, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(descargarreporte, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(56, 56, 56)
                                .addComponent(lblPromedioAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45)
                                .addComponent(lblTotalAsistencias, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTotalIngresos, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblIngresosPorMembresia, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(17, 17, 17)
                        .addComponent(lblIngresosPorMetodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(74, 74, 74)
                .addComponent(lblTotalPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(107, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void verREPORTEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verREPORTEActionPerformed
      
         Date fechaInicio = fechainicio.getDate();
         Date fechaFin = fechafin.getDate();
        obtenerEstadisticas(fechaInicio, fechaFin);
    }//GEN-LAST:event_verREPORTEActionPerformed

    private void descargarreporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descargarreporteActionPerformed
        // TODO add your handling code here:
        Date fechaInicio = fechainicio.getDate();
         Date fechaFin = fechafin.getDate();
         String contenidoPDF = generarContenidoPDF(fechaInicio, fechaFin); // Generar el contenido que deseas en el PDF
        JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Guardar PDF");
                fileChooser.setSelectedFile(new File("estadisticas.pdf")); 

                int userSelection = fileChooser.showSaveDialog(null);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    crearPDF(contenidoPDF, fileToSave.getAbsolutePath());
                    JOptionPane.showMessageDialog(null, "PDF guardado exitosamente en: " + fileToSave.getAbsolutePath());
                }
    }//GEN-LAST:event_descargarreporteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton descargarreporte;
    private com.toedter.calendar.JDateChooser fechafin;
    private com.toedter.calendar.JDateChooser fechainicio;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel lblIngresosPorMembresia;
    private javax.swing.JLabel lblIngresosPorMetodoPago;
    private javax.swing.JLabel lblPromedioAsistencia;
    private javax.swing.JLabel lblTotalAsistencias;
    private javax.swing.JLabel lblTotalIngresos;
    private javax.swing.JLabel lblTotalPagos;
    private javax.swing.JButton verREPORTE;
    // End of variables declaration//GEN-END:variables
}
