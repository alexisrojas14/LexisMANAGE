/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lexismanage;

import javax.swing.JFrame;
import lexismanage.vista.LOGIN;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author alexi
 */
public class LexisManage {

    /**
     * @param args the command line arguments
     */
    public static  int idUsuario;
    
    public static void main(String[] args) {
        // TODO code application logic here
        
       
       
            LOGIN login = new LOGIN();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
            
         
       
    
        
    }
    
}
