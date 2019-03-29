
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;

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

    public AdminModel(DataBase dB) {
        this.dB = dB;
    }

    public boolean addProfessor(String professor, String username, String email) {
        try {
            String insertion = "insert into professor(name, username, email)"
                    + "values('" + professor + "','" + username + "','" + email + "')";
            dB.update(insertion);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    public boolean addCourse(String course, String description) {
        try {
            String insertion = "insert into course(name,description)"
                    + "values('" + course + "' ,'" + description + "')";
            dB.update(insertion);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public void courseProfessor(String course, String professorUsername) throws SQLException {
        ResultSet r1 = dB.query("SELECT id FROM course WHERE name = '" + course + "'");
        r1.next();
        String courseId = r1.getString("id");

        ResultSet r2 = dB.query("SELECT id FROM professor WHERE username = '" + professorUsername + "'");
        r2.next();
        String professorId = r2.getString("id");
        
        dB.update("INSERT INTO course_professor(course,professor)"
                + "VALUES('" + courseId + "','" + professorId + "')");
    }
    
    public JSONArray getCourse() throws SQLException {
        JSONArray res = new JSONArray();
        ResultSet rs = dB.query("SELECT name FROM public.course");
        
        while (rs.next()) {
            res.put(rs.getString("name"));
        }

        dB.closeConnection();
        return res;
    }

    public JSONArray getProfessor() throws SQLException {
        JSONArray res = new JSONArray();
        ResultSet rs = dB.query("select name, username from professor");
        
        while (rs.next()) {
            res.put(rs.getString("name") + " (" + rs.getString("username") + ")");
        }
        
        dB.closeConnection();
        return res;
    }
    
    public JSONArray getFreeCourse(String professorUsername) throws SQLException {
        JSONArray res = new JSONArray();
        ResultSet rs = dB.query
        ("select name\n" +
         "from course \n" +
            "except\n" +
         "select c.name\n" +
         "from course c join course_professor c_p on c.id=c_p.course\n" +
            "join professor p on c_p.professor=p.id\n" +
          "where p.username='"+professorUsername+"'");
        
        while (rs.next()) {
            res.put(rs.getString("name"));
        }

        dB.closeConnection();
        return res;
    }
    
    public JSONArray getFreeProfessor(String course) throws SQLException {
        JSONArray res = new JSONArray();
        ResultSet rs = dB.query
        ("select name, username\n" +
         "from professor\n" +
            "except\n" +
         "select p.name, p.username\n" +
         "from course c join course_professor c_p on c.id=c_p.course\n" +
            "join professor p on c_p.professor=p.id\n" +
         "where c.name='"+course+"'");
        
        while (rs.next()) {
            res.put(rs.getString("name") + " (" + rs.getString("username") + ")");
        }

        dB.closeConnection();
        return res;
    }
}
