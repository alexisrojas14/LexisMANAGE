/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lexismanage;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author alexi
 */
public class Conexion {
    private static Connection conexion;
   public static Connection conectar() {
        Connection conexion = null;
        String url = "jdbc:mysql://Localhost:3306/LexisMANAGE";
        String usuario = "Alexis";
        String contraseña = "1234567";

        try {
            
            conexion = DriverManager.getConnection(url, usuario, contraseña);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos MySQL: " + e.getMessage(), "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }

        return conexion;
    }
   
   public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
   
  
      
} 

