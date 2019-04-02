/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lorenzo
 */
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
public class UserModel {

    //modello per la gestione delle attività di amministrazione 
    private final DataBase dB;

    public UserModel(DataBase dB) {
        this.dB = dB;
    }

    public JSONArray getReservation(String course, String professorUsername) throws SQLException {
        String query
                = "select r.id, c.name, p.userName, p.name, day, startHour, endHour, state\n"
                + "from reservation r \n"
                + "join course_professor c_p on r.course_professor = c_p.id\n"
                + "join course c on c_p.course = c.id\n"
                + "join professor p on c_p.professor = p.id\n"
                + "join slot s on r.slot = s.id\n"
                + "where c.name = '"+course+"' and p.username = '"+professorUsername+"'\n"
                + "order by r.id";
        ResultSet res = dB.query(query);

        JSONArray re = new JSONArray();
        while (res.next()) {
            JSONArray a = new JSONArray();
            a.put(res.getString("day"));
            a.put("h"+res.getString("startHour"));
            a.put("h"+res.getString("endHour"));
            a.put(res.getString("state"));
            a.put(res.getString("id"));
            re.put(a);
        }
        return re;
    }
}
