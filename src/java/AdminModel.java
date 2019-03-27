
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lorenzo
 */
public class AdminModel {
    //modello per la gestione delle attività di amministrazione 
    private final DataBase dB;
    public AdminModel(DataBase dB){this.dB = dB;}
    
    public boolean addProfessor(String professor){
        try {
            String insertion = "insert into professors(name)"+
                                "values('"+professor+"')";
            dB.update(insertion);
        } catch (SQLException ex) {
            return false;
        }finally{
            return true;
        }
    }
    
    public boolean addCourse(String course, String description){
        try {
            String insertion = "insert into course(name,description)"+
                                "values('"+course+"' ,'"+description+"')";
            dB.update(insertion);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
    
}
