
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
        String query
                = "DO\n"
                + "$do$\n"
                + "\n"
                + "DECLARE c_p_id course_professor.id%type;\n"
                + "DECLARE sl slot%rowtype;\n"
                + "\n"
                + "BEGIN\n"
                + "	INSERT INTO course_professor ( course , professor)\n"
                + " 	(SELECT id, (SELECT id FROM professor WHERE username = '"+professorUsername+"')\n"
                + "    FROM course WHERE name = '"+course+"')\n"
                + "	RETURNING id INTO c_p_id;\n"
                + "\n"
                + "	FOR sl IN select id from slot LOOP\n"
                + "		INSERT INTO reservation(course_professor,slot,state)\n"
                + "        VALUES( c_p_id , sl.id , 'free');\n"
                + "	END LOOP;\n"
                + "END;\n"
                + "$do$";
        dB.update(query);
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
        ResultSet rs = dB.query("select name\n"
                + "from course \n"
                + "except\n"
                + "select c.name\n"
                + "from course c join course_professor c_p on c.id=c_p.course\n"
                + "join professor p on c_p.professor=p.id\n"
                + "where p.username='" + professorUsername + "'");

        while (rs.next()) {
            res.put(rs.getString("name"));
        }

        dB.closeConnection();
        return res;
    }

    public JSONArray getCourseProfessor(String course) throws SQLException {
        JSONArray res = new JSONArray();
        ResultSet rs = dB.query
                ( "select p.name, p.username\n"
                + "from course c join course_professor c_p on c.id=c_p.course\n"
                + "join professor p on c_p.professor=p.id\n"
                + "where c.name='" + course + "'");

        while (rs.next()) {
            res.put(rs.getString("name") + " (" + rs.getString("username") + ")");
        }

        dB.closeConnection();
        return res;
    }
}
