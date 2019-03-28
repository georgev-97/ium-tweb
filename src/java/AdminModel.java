import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public boolean addProfessor(String professor, String username, String email){
        try {
            String insertion = "insert into professor(name, username, email)"+
                                "values('"+professor+"','"+username+"','"+email+"')";
            dB.update(insertion);
        } catch (SQLException ex) {
            return false;
        }
        return true;
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
    
    public boolean courseProfessor(String course, String professorUsername){
        try {
            ResultSet r1 = dB.query("SELECT id FROM course WHERE name = '"+course+"'");
            r1.next();
            String courseId = r1.getString("id");
            
            ResultSet r2 = dB.query("SELECT id FROM professor WHERE username = '"+professorUsername+"'");
            r2.next();
            String professorId = r2.getString("id");
            dB.update("INSERT INTO course_professor(course,professor)"
                    + "VALUES('"+courseId+"','"+professorId+"')");
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        }
        return true;
    }
}
