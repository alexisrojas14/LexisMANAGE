/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package lexismanage.vista;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lexismanage.Conexion;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alexi
 */
public class MenuUsuarios extends javax.swing.JPanel {

    /**
     * Creates new form usuarios
     */
    
        Conexion con=new Conexion();
        
       
    public MenuUsuarios() {
        initComponents();
        con.conectar();
        mostrarDatosEnTabla();
        actualizarMembresiasVencidas();
    }
    
    private void actualizarMembresiasVencidas() {
    // El bloque try-with-resources garantiza que la conexión se cierre automáticamente
    try (Connection conexion = Conexion.conectar()) {
        // Consulta para actualizar las membresías vencidas
        String sql = "UPDATE Usuario u " +
                     "JOIN Pago p ON u.id_usuario = p.id_usuario " +
                     "SET u.id_estado = (SELECT id_estado FROM EstadoMembresia WHERE estado = 'Vencida') " +
                     "WHERE p.fecha_fin < NOW() AND u.id_estado != (SELECT id_estado FROM EstadoMembresia WHERE estado = 'Vencida')";

        PreparedStatement pst = conexion.prepareStatement(sql);

        // Ejecutar la actualización
        int filasActualizadas = pst.executeUpdate();
        System.out.println("Membresías vencidas actualizadas: " + filasActualizadas);

    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

     
    public void mostrarDatosEnTablaPorIdentificacion(String identificacion) {
    // Consulta SQL para obtener la información del usuario, membresía y estado
    String sql = "SELECT u.identificacion AS ID, u.nombre AS Nombre, u.apellido AS Apellido, " +
                 "u.telefono AS Telefono, u.fecha_ingreso AS FechaRegistro, " +
                 "COALESCE(m.nombre, 'Sin Membresía') AS Membresia, " +
                 "p.fecha_inicio AS InicioMembresia, " +
                 "p.fecha_fin AS FinMembresia, " +
                 "COALESCE(e.estado, 'Sin Estado') AS EstadoMembresia " +
                 "FROM Usuario u " +
                 "LEFT JOIN Pago p ON u.id_usuario = p.id_usuario " +
                 "LEFT JOIN Membresia m ON p.id_membresia = m.id_membresia " +
                 "LEFT JOIN EstadoMembresia e ON u.id_estado = e.id_estado " +
                 "LEFT JOIN (SELECT id_usuario, MAX(fecha_fin) AS max_fecha FROM Pago GROUP BY id_usuario) p2 " +
                 "ON u.id_usuario = p2.id_usuario AND p.fecha_fin = p2.max_fecha " +
                 "WHERE u.identificacion = ?";  // Filtro por identificación
    
    // Inicializa la conexión
    Conexion conexion = new Conexion();
    Connection con = conexion.conectar();

    // Crea el modelo para la tabla
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID");
    model.addColumn("Nombre");
    model.addColumn("Apellido");
    model.addColumn("Telefono");
    model.addColumn("Fecha Registro");
    model.addColumn("Membresia");
    model.addColumn("Inicio Membresia");
    model.addColumn("Fin Membresia");
    model.addColumn("Estado Membresia");

    // Asigna el modelo al JTable
    TABLAUSUARIOS.setModel(model);

    try (PreparedStatement pt = con.prepareStatement(sql)) {
        // Configura el parámetro de búsqueda por identificación
        pt.setString(1, identificacion);
        
        // Ejecuta la consulta
        try (ResultSet rs = pt.executeQuery()) {
            // Limpia el modelo antes de agregar nuevos datos
            model.setRowCount(0);

            // Itera sobre los resultados de la consulta
            while (rs.next()) {
                Object[] fila = {
                    rs.getString("ID"),
                    rs.getString("Nombre"),
                    rs.getString("Apellido"),
                    rs.getString("Telefono"),
                    rs.getDate("FechaRegistro"),
                    rs.getString("Membresia"),
                    rs.getDate("InicioMembresia"),
                    rs.getDate("FinMembresia"),
                    rs.getString("EstadoMembresia")
                };
                // Agrega la fila al modelo de la tabla
                model.addRow(fila);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
    }
}


   public void mostrarDatosEnTabla() {
    // Consulta SQL para obtener la información del usuario, membresía y estado
    String sql = "SELECT u.identificacion AS ID, u.nombre AS Nombre, u.apellido AS Apellido, " +
                 "u.telefono AS Telefono, u.fecha_ingreso AS FechaRegistro, " +
                 "COALESCE(m.nombre, 'Sin Membresía') AS Membresia, " + // Si no tiene membresía, muestra 'Sin Membresía'
                 "p.fecha_inicio AS InicioMembresia, " +
                 "p.fecha_fin AS FinMembresia, " +
                 "COALESCE(e.estado, 'Sin Estado') AS EstadoMembresia " + // Si no tiene estado, muestra 'Sin Estado'
                 "FROM Usuario u " +
                 "LEFT JOIN Pago p ON u.id_usuario = p.id_usuario " +
                 "LEFT JOIN Membresia m ON p.id_membresia = m.id_membresia " +
                 "LEFT JOIN EstadoMembresia e ON u.id_estado = e.id_estado " +
                 "LEFT JOIN (SELECT id_usuario, MAX(fecha_fin) AS max_fecha FROM Pago GROUP BY id_usuario) p2 " +
                 "ON u.id_usuario = p2.id_usuario AND p.fecha_fin = p2.max_fecha"; // Solo trae la última membresía si existe
    
    // Inicializa la conexión
    Conexion conexion = new Conexion();
    Connection con = conexion.conectar();

    // Crea el modelo para la tabla
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID");
    model.addColumn("Nombre");
    model.addColumn("Apellido");
    model.addColumn("Telefono");
    model.addColumn("Fecha Registro");
    model.addColumn("Membresia");
    model.addColumn("Inicio Membresia");
    model.addColumn("Fin Membresia");
    model.addColumn("Estado Membresia");

    // Asigna el modelo al JTable
    TABLAUSUARIOS.setModel(model);

    try (PreparedStatement pt = con.prepareStatement(sql);
         ResultSet rs = pt.executeQuery()) {

        // Limpia el modelo antes de agregar nuevos datos
        model.setRowCount(0);

        // Itera sobre los resultados de la consulta
        while (rs.next()) {
            Object[] fila = {
                rs.getString("ID"),
                rs.getString("Nombre"),
                rs.getString("Apellido"),
                rs.getString("Telefono"),
                rs.getDate("FechaRegistro"),
                rs.getString("Membresia"),
                rs.getDate("InicioMembresia"),
                rs.getDate("FinMembresia"),
                rs.getString("EstadoMembresia")
            };
            // Agrega la fila al modelo de la tabla
            model.addRow(fila);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
    }
}

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TABLAUSUARIOS = new javax.swing.JTable();
        BOTONBUSCAR = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        BuscarUSERCAMPO = new javax.swing.JTextField();
        Actualizar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        AGREGARUSER = new javax.swing.JButton();
        EDITARUSER = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        IDENTIFICACIONUSERCAMPO = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        NOMBREUSERCAMPO = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        APELLIDOUSERCAMPO = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        TELEFONOUSERCAMPO = new javax.swing.JTextField();

        setBackground(new java.awt.Color(10, 16, 30));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 3, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("LISTA DE USUARIOS");

        TABLAUSUARIOS.setBackground(new java.awt.Color(10, 16, 30));
        TABLAUSUARIOS.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TABLAUSUARIOS.setForeground(new java.awt.Color(255, 255, 255));
        TABLAUSUARIOS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        TABLAUSUARIOS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TABLAUSUARIOSMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TABLAUSUARIOS);

        BOTONBUSCAR.setBackground(new java.awt.Color(255, 77, 88));
        BOTONBUSCAR.setFont(new java.awt.Font("Yu Gothic UI", 3, 18)); // NOI18N
        BOTONBUSCAR.setForeground(new java.awt.Color(242, 242, 242));
        BOTONBUSCAR.setText("Buscar");
        BOTONBUSCAR.setBorder(null);
        BOTONBUSCAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BOTONBUSCARActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 3, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Buscar Usuario :");

        BuscarUSERCAMPO.setBackground(new java.awt.Color(242, 242, 242));
        BuscarUSERCAMPO.setFont(new java.awt.Font("Yu Gothic UI", 3, 14)); // NOI18N
        BuscarUSERCAMPO.setForeground(new java.awt.Color(153, 153, 153));
        BuscarUSERCAMPO.setText(" Ingrese ID Usuario");
        BuscarUSERCAMPO.setToolTipText("");
        BuscarUSERCAMPO.setBorder(null);
        BuscarUSERCAMPO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                BuscarUSERCAMPOMousePressed(evt);
            }
        });
        BuscarUSERCAMPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscarUSERCAMPOActionPerformed(evt);
            }
        });

        Actualizar.setFont(new java.awt.Font("Yu Gothic UI", 3, 18)); // NOI18N
        Actualizar.setForeground(new java.awt.Color(255, 77, 88));
        Actualizar.setText("Actualizar & Limpiar");
        Actualizar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Actualizar.setBorderPainted(false);
        Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 3, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Gestion Usuarios");

        AGREGARUSER.setBackground(new java.awt.Color(255, 77, 88));
        AGREGARUSER.setFont(new java.awt.Font("Yu Gothic UI", 3, 18)); // NOI18N
        AGREGARUSER.setForeground(new java.awt.Color(242, 242, 242));
        AGREGARUSER.setText(" Agregar Usuario");
        AGREGARUSER.setBorder(null);
        AGREGARUSER.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AGREGARUSERActionPerformed(evt);
            }
        });

        EDITARUSER.setFont(new java.awt.Font("Yu Gothic UI", 3, 18)); // NOI18N
        EDITARUSER.setForeground(new java.awt.Color(255, 77, 88));
        EDITARUSER.setText("Editar Usuario");
        EDITARUSER.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        EDITARUSER.setBorderPainted(false);
        EDITARUSER.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EDITARUSERActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 3, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("   ID :");

        IDENTIFICACIONUSERCAMPO.setBackground(new java.awt.Color(242, 242, 242));
        IDENTIFICACIONUSERCAMPO.setFont(new java.awt.Font("Yu Gothic UI", 3, 14)); // NOI18N
        IDENTIFICACIONUSERCAMPO.setForeground(new java.awt.Color(153, 153, 153));
        IDENTIFICACIONUSERCAMPO.setText(" Ingrese ID Usuario");
        IDENTIFICACIONUSERCAMPO.setToolTipText("");
        IDENTIFICACIONUSERCAMPO.setBorder(null);
        IDENTIFICACIONUSERCAMPO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                IDENTIFICACIONUSERCAMPOMousePressed(evt);
            }
        });
        IDENTIFICACIONUSERCAMPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDENTIFICACIONUSERCAMPOActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 3, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Nombre :");

        NOMBREUSERCAMPO.setBackground(new java.awt.Color(242, 242, 242));
        NOMBREUSERCAMPO.setFont(new java.awt.Font("Yu Gothic UI", 3, 14)); // NOI18N
        NOMBREUSERCAMPO.setForeground(new java.awt.Color(153, 153, 153));
        NOMBREUSERCAMPO.setText(" Ingrese Nombre Usuario");
        NOMBREUSERCAMPO.setToolTipText("");
        NOMBREUSERCAMPO.setBorder(null);
        NOMBREUSERCAMPO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                NOMBREUSERCAMPOMousePressed(evt);
            }
        });
        NOMBREUSERCAMPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NOMBREUSERCAMPOActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI Semibold", 3, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Apellido :");

        APELLIDOUSERCAMPO.setBackground(new java.awt.Color(242, 242, 242));
        APELLIDOUSERCAMPO.setFont(new java.awt.Font("Yu Gothic UI", 3, 14)); // NOI18N
        APELLIDOUSERCAMPO.setForeground(new java.awt.Color(153, 153, 153));
        APELLIDOUSERCAMPO.setText(" Ingrese Apellido Usuario");
        APELLIDOUSERCAMPO.setToolTipText("");
        APELLIDOUSERCAMPO.setBorder(null);
        APELLIDOUSERCAMPO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                APELLIDOUSERCAMPOMousePressed(evt);
            }
        });
        APELLIDOUSERCAMPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                APELLIDOUSERCAMPOActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 3, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Telefono :");

        TELEFONOUSERCAMPO.setBackground(new java.awt.Color(242, 242, 242));
        TELEFONOUSERCAMPO.setFont(new java.awt.Font("Yu Gothic UI", 3, 14)); // NOI18N
        TELEFONOUSERCAMPO.setForeground(new java.awt.Color(153, 153, 153));
        TELEFONOUSERCAMPO.setText(" Ingrese Telefono Usuario");
        TELEFONOUSERCAMPO.setToolTipText("");
        TELEFONOUSERCAMPO.setBorder(null);
        TELEFONOUSERCAMPO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TELEFONOUSERCAMPOMousePressed(evt);
            }
        });
        TELEFONOUSERCAMPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TELEFONOUSERCAMPOActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(IDENTIFICACIONUSERCAMPO, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(NOMBREUSERCAMPO, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(APELLIDOUSERCAMPO, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TELEFONOUSERCAMPO, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(AGREGARUSER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(EDITARUSER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 38, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(BuscarUSERCAMPO, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(38, 38, 38))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(BOTONBUSCAR, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(38, 38, 38))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(132, 132, 132)))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabel2))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(BuscarUSERCAMPO, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BOTONBUSCAR)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Actualizar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(IDENTIFICACIONUSERCAMPO)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(NOMBREUSERCAMPO)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(APELLIDOUSERCAMPO)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TELEFONOUSERCAMPO)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(AGREGARUSER, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EDITARUSER, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void BOTONBUSCARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BOTONBUSCARActionPerformed
        // TODO add your handling code here:
        mostrarDatosEnTablaPorIdentificacion(BuscarUSERCAMPO.getText());
    }//GEN-LAST:event_BOTONBUSCARActionPerformed

    private void BuscarUSERCAMPOMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarUSERCAMPOMousePressed
        // TODO add your handling code here:
        if(BuscarUSERCAMPO.getText().equals(" Ingrese ID Usuario")){
            BuscarUSERCAMPO.setText("");
            BuscarUSERCAMPO.setForeground(Color.black);
        }
    }//GEN-LAST:event_BuscarUSERCAMPOMousePressed

    private void BuscarUSERCAMPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscarUSERCAMPOActionPerformed
        // TODO add your handling code here:
      
    }//GEN-LAST:event_BuscarUSERCAMPOActionPerformed

    private void ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActualizarActionPerformed
        // TODO add your handling code here:
        mostrarDatosEnTabla();
        
            IDENTIFICACIONUSERCAMPO.setText(" Ingrese ID Usuario");
            IDENTIFICACIONUSERCAMPO.setForeground(Color.gray);
            NOMBREUSERCAMPO.setText(" Ingrese Nombre Usuario");
            NOMBREUSERCAMPO.setForeground(Color.gray);
            APELLIDOUSERCAMPO.setText(" Ingrese Apellido Usuario");
            APELLIDOUSERCAMPO.setForeground(Color.gray);
            BuscarUSERCAMPO.setText(" Ingrese ID Usuario");
            BuscarUSERCAMPO.setForeground(Color.gray);
             TELEFONOUSERCAMPO.setText(" Ingrese Telefono Usuario");
            TELEFONOUSERCAMPO.setForeground(Color.gray);
        
    }//GEN-LAST:event_ActualizarActionPerformed

    private void AGREGARUSERActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AGREGARUSERActionPerformed
        // TODO add your handling code here:
        
       String sql = "INSERT INTO Usuario (identificacion, nombre, apellido, telefono, fecha_ingreso) "
           + "VALUES (?, ?, ?, ?, ?)";

    // Validar que los campos no estén vacíos y no contengan el texto por defecto
    if(!IDENTIFICACIONUSERCAMPO.getText().isEmpty() && 
       !IDENTIFICACIONUSERCAMPO.getText().equals(" Ingrese ID Usuario") &&
       !NOMBREUSERCAMPO.getText().isEmpty() && 
       !NOMBREUSERCAMPO.getText().equals(" Ingrese Nombre Usuario") &&
       !APELLIDOUSERCAMPO.getText().isEmpty() && 
       !APELLIDOUSERCAMPO.getText().equals(" Ingrese Apellido Usuario") &&
       !TELEFONOUSERCAMPO.getText().isEmpty() && 
       !TELEFONOUSERCAMPO.getText().equals(" Ingrese Telefono Usuario")) {

        Connection conexion = con.conectar();
        try {
            PreparedStatement pt = conexion.prepareStatement(sql);
            pt.setString(1, IDENTIFICACIONUSERCAMPO.getText());
            pt.setString(2, NOMBREUSERCAMPO.getText());
            pt.setString(3, APELLIDOUSERCAMPO.getText());
            pt.setString(4, TELEFONOUSERCAMPO.getText());
            pt.setDate(5, java.sql.Date.valueOf(java.time.LocalDate.now()));

            int rs = pt.executeUpdate();

            if(rs > 0){
                JOptionPane.showMessageDialog(null, "Usuario agregado correctamente");
                
                // Reiniciar los campos de texto con el texto y color por defecto
                IDENTIFICACIONUSERCAMPO.setText(" Ingrese ID Usuario");
                IDENTIFICACIONUSERCAMPO.setForeground(Color.gray);
                NOMBREUSERCAMPO.setText(" Ingrese Nombre Usuario");
                NOMBREUSERCAMPO.setForeground(Color.gray);
                APELLIDOUSERCAMPO.setText(" Ingrese Apellido Usuario");
                APELLIDOUSERCAMPO.setForeground(Color.gray);
                TELEFONOUSERCAMPO.setText(" Ingrese Telefono Usuario");
                TELEFONOUSERCAMPO.setForeground(Color.gray);
                con.cerrarConexion();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al agregar el usuario: " + e.getMessage());
        }
    } else {
        JOptionPane.showMessageDialog(null, "Rellena todos los campos correctamente");
    }

    }//GEN-LAST:event_AGREGARUSERActionPerformed

    private void EDITARUSERActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EDITARUSERActionPerformed
        // TODO add your handling code here:
        String sql = "UPDATE Usuario SET nombre = ?, apellido = ?, telefono = ? WHERE identificacion = ?";
    Connection con = Conexion.conectar();

    // Asegúrate de tener el ID del usuario para la actualización
    if (!IDENTIFICACIONUSERCAMPO.getText().trim().isEmpty()) {
        try {
            // Verificar que los campos no estén vacíos
            if (NOMBREUSERCAMPO.getText().trim().isEmpty() || NOMBREUSERCAMPO.getText().equals(" Ingrese Nombre Usuario")) {
                JOptionPane.showMessageDialog(null, "El campo Nombre es obligatorio.");
                return;
            }

            if (APELLIDOUSERCAMPO.getText().trim().isEmpty() || APELLIDOUSERCAMPO.getText().equals(" Ingrese Apellido Usuario")) {
                JOptionPane.showMessageDialog(null, "El campo Apellido es obligatorio.");
                return;
            }

            if (TELEFONOUSERCAMPO.getText().trim().isEmpty() || TELEFONOUSERCAMPO.getText().equals(" Ingrese Telefono Usuario")) {
                JOptionPane.showMessageDialog(null, "El campo Teléfono es obligatorio.");
                return;
            }

            // Preparar la consulta SQL con los nuevos valores
            PreparedStatement pt = con.prepareStatement(sql);
            pt.setString(1, NOMBREUSERCAMPO.getText());   // Nombre
            pt.setString(2, APELLIDOUSERCAMPO.getText()); // Apellido
            pt.setString(3, TELEFONOUSERCAMPO.getText()); // Teléfono
            pt.setString(4, IDENTIFICACIONUSERCAMPO.getText()); // Identificación (ID)

            // Ejecutar la consulta y verificar si se realizó la actualización
            int fila = pt.executeUpdate();

            if (fila > 0) {
                JOptionPane.showMessageDialog(null, "Usuario " + NOMBREUSERCAMPO.getText() + " actualizado correctamente!");

                // Restablecer los campos después de la actualización
                NOMBREUSERCAMPO.setText(" Ingrese ID Usuario");
                APELLIDOUSERCAMPO.setText(" Ingrese Nombre Usuario");
                TELEFONOUSERCAMPO.setText(" Ingrese Apellido Usuario");
                IDENTIFICACIONUSERCAMPO.setText(" Ingrese ID Usuario");

                // Cambiar el color del texto de los campos
                NOMBREUSERCAMPO.setForeground(Color.gray);
                APELLIDOUSERCAMPO.setForeground(Color.gray);
                TELEFONOUSERCAMPO.setForeground(Color.gray);
                IDENTIFICACIONUSERCAMPO.setForeground(Color.gray);
                con.close();
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el usuario, asegúrate de que la identificación sea correcta.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar los datos: " + e.getMessage());
            System.out.println("Error: " + e);
        }
    } else {
        JOptionPane.showMessageDialog(null, "El campo Identificación es obligatorio.");
    }

    }//GEN-LAST:event_EDITARUSERActionPerformed

    private void IDENTIFICACIONUSERCAMPOMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IDENTIFICACIONUSERCAMPOMousePressed
        // TODO add your handling code here:
        if (IDENTIFICACIONUSERCAMPO.getText().equals(" Ingrese ID Usuario")) {
            IDENTIFICACIONUSERCAMPO.setText("");
            IDENTIFICACIONUSERCAMPO.setForeground(Color.black);
        }
        
        if(NOMBREUSERCAMPO.getText().isEmpty()){
            NOMBREUSERCAMPO.setText(" Ingrese Nombre Usuario");
            NOMBREUSERCAMPO.setForeground(Color.gray);
        }
        if(APELLIDOUSERCAMPO.getText().isEmpty()){
            APELLIDOUSERCAMPO.setText(" Ingrese Apellido Usuario");
            APELLIDOUSERCAMPO.setForeground(Color.gray);
        }
        if(TELEFONOUSERCAMPO.getText().isEmpty()){
            TELEFONOUSERCAMPO.setText(" Ingrese Telefono Usuario");
            TELEFONOUSERCAMPO.setForeground(Color.gray);
        }
        
    }//GEN-LAST:event_IDENTIFICACIONUSERCAMPOMousePressed

    private void IDENTIFICACIONUSERCAMPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDENTIFICACIONUSERCAMPOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDENTIFICACIONUSERCAMPOActionPerformed

    private void NOMBREUSERCAMPOMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NOMBREUSERCAMPOMousePressed
        // TODO add your handling code here:
        if (NOMBREUSERCAMPO.getText().equals(" Ingrese Nombre Usuario")) {
            NOMBREUSERCAMPO.setText("");
            NOMBREUSERCAMPO.setForeground(Color.black);
        }
        
        if(IDENTIFICACIONUSERCAMPO.getText().isEmpty()){
            IDENTIFICACIONUSERCAMPO.setText(" Ingrese ID Usuario");
            IDENTIFICACIONUSERCAMPO.setForeground(Color.gray);
        }
        if(APELLIDOUSERCAMPO.getText().isEmpty()){
            APELLIDOUSERCAMPO.setText(" Ingrese Apellido Usuario");
            APELLIDOUSERCAMPO.setForeground(Color.gray);
        }
        if(TELEFONOUSERCAMPO.getText().isEmpty()){
            TELEFONOUSERCAMPO.setText(" Ingrese Telefono Usuario");
            TELEFONOUSERCAMPO.setForeground(Color.gray);
        }
    }//GEN-LAST:event_NOMBREUSERCAMPOMousePressed

    private void NOMBREUSERCAMPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NOMBREUSERCAMPOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NOMBREUSERCAMPOActionPerformed

    private void APELLIDOUSERCAMPOMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_APELLIDOUSERCAMPOMousePressed
        // TODO add your handling code here:
        if (APELLIDOUSERCAMPO.getText().equals(" Ingrese Apellido Usuario")){
            APELLIDOUSERCAMPO.setText("");
            APELLIDOUSERCAMPO.setForeground(Color.black);
        }
        
        if(IDENTIFICACIONUSERCAMPO.getText().isEmpty()){
            IDENTIFICACIONUSERCAMPO.setText(" Ingrese ID Usuario");
            IDENTIFICACIONUSERCAMPO.setForeground(Color.gray);
        }
        if(NOMBREUSERCAMPO.getText().isEmpty()){
            NOMBREUSERCAMPO.setText(" Ingrese Nombre Usuario");
            NOMBREUSERCAMPO.setForeground(Color.gray);
        }
        if(TELEFONOUSERCAMPO.getText().isEmpty()){
            TELEFONOUSERCAMPO.setText(" Ingrese Telefono Usuario");
            TELEFONOUSERCAMPO.setForeground(Color.gray);
        }
    }//GEN-LAST:event_APELLIDOUSERCAMPOMousePressed

    private void APELLIDOUSERCAMPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_APELLIDOUSERCAMPOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_APELLIDOUSERCAMPOActionPerformed

    private void TELEFONOUSERCAMPOMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TELEFONOUSERCAMPOMousePressed
        // TODO add your handling code here:
        if (TELEFONOUSERCAMPO.getText().equals(" Ingrese Telefono Usuario")){
            TELEFONOUSERCAMPO.setText("");
            TELEFONOUSERCAMPO.setForeground(Color.black);
        }
        
        if(IDENTIFICACIONUSERCAMPO.getText().isEmpty()){
            IDENTIFICACIONUSERCAMPO.setText(" Ingrese ID Usuario");
            IDENTIFICACIONUSERCAMPO.setForeground(Color.gray);
        }
        if(NOMBREUSERCAMPO.getText().isEmpty()){
            NOMBREUSERCAMPO.setText(" Ingrese Nombre Usuario");
            NOMBREUSERCAMPO.setForeground(Color.gray);
        }
        if(APELLIDOUSERCAMPO.getText().isEmpty()){
            APELLIDOUSERCAMPO.setText(" Ingrese Apellido Usuario");
            APELLIDOUSERCAMPO.setForeground(Color.gray);
        }
    }//GEN-LAST:event_TELEFONOUSERCAMPOMousePressed

    private void TELEFONOUSERCAMPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TELEFONOUSERCAMPOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TELEFONOUSERCAMPOActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        if(BuscarUSERCAMPO.getText().isEmpty()){
            BuscarUSERCAMPO.setText(" Ingrese ID Usuario");
            BuscarUSERCAMPO.setForeground(Color.gray);
        }
    }//GEN-LAST:event_formMouseClicked

    private void TABLAUSUARIOSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TABLAUSUARIOSMouseClicked
        // TODO add your handling code here:
         // Obtener la fila seleccionada en la tabla
    int seleccion = TABLAUSUARIOS.rowAtPoint(evt.getPoint());

  
   

    // Llenar los campos con los valores de la fila seleccionada
    IDENTIFICACIONUSERCAMPO.setText(String.valueOf(TABLAUSUARIOS.getValueAt(seleccion, 0)));
    NOMBREUSERCAMPO.setText(String.valueOf(TABLAUSUARIOS.getValueAt(seleccion, 1)));
    APELLIDOUSERCAMPO.setText(String.valueOf(TABLAUSUARIOS.getValueAt(seleccion, 2)));
   TELEFONOUSERCAMPO.setText(String.valueOf(TABLAUSUARIOS.getValueAt(seleccion, 3)));
    

 
    IDENTIFICACIONUSERCAMPO.setForeground(Color.black);
   NOMBREUSERCAMPO.setForeground(Color.black);
    APELLIDOUSERCAMPO.setForeground(Color.black);
    TELEFONOUSERCAMPO.setForeground(Color.black);
 
    }//GEN-LAST:event_TABLAUSUARIOSMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AGREGARUSER;
    private javax.swing.JTextField APELLIDOUSERCAMPO;
    private javax.swing.JButton Actualizar;
    private javax.swing.JButton BOTONBUSCAR;
    private javax.swing.JTextField BuscarUSERCAMPO;
    private javax.swing.JButton EDITARUSER;
    private javax.swing.JTextField IDENTIFICACIONUSERCAMPO;
    private javax.swing.JTextField NOMBREUSERCAMPO;
    private javax.swing.JTable TABLAUSUARIOS;
    private javax.swing.JTextField TELEFONOUSERCAMPO;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
