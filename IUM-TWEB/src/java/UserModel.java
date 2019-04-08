
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author george
 */
public class UserModel {

    /**
     *
     * @author lorenzo
     */
    //modello per la gestione delle attività di amministrazione 
    private final DataBase dB;

    public UserModel(DataBase dB) {
        this.dB = dB;
    }

    public JSONArray getReservation(String course, String professorUsername) throws SQLException {
        String query
                = "select r.id, c.name, p.userName, p.name, d.name as dayName, startHour, endHour, state\n"
                + "from reservation r \n"
                + "join course_professor c_p on r.course_professor = c_p.id\n"
                + "join course c on c_p.course = c.id\n"
                + "join professor p on c_p.professor = p.id\n"
                + "join slot s on r.slot = s.id\n"
                + "join day d on s.day=d.id\n"
                + "where c.name = '" + course + "' and p.username = '" + professorUsername + "'\n"
                + "order by r.id";
        ResultSet res;
        JSONArray re = new JSONArray();
        try {
            res = dB.query(query);

            while (res.next()) {
                JSONArray a = new JSONArray();
                a.put(res.getString("dayName"));
                a.put(res.getString("startHour"));
                a.put(res.getString("endHour"));
                a.put(res.getString("state"));
                a.put(res.getString("id"));
                re.put(a);
            }
        } catch (SQLException ex) {
            dB.closeConnection();
            throw ex;
        }
        return re;
    }

    public void reserve(String slotId, String userId) throws SQLException {
        String query
                = "update reservation\n"
                + "set state = 'no-free'\n"
                + "where id = " + slotId + ";"
                + "\n"
                + "insert into reservation_user(reservation, userId)\n"
                + "values(" + slotId + "," + userId + ")";
        dB.update(query);
    }

    public JSONArray getReservation(String userId) throws SQLException {
        String query
                = "select c.name as cname, p.name as pname,\n"
                + "p.username as pusername, d.name as dayName, s.startHour, s.endHour,\n"
                + "r.id as rid, r_u.id as ruid\n"
                + "from reservation r\n"
                + "join reservation_user r_u\n"
                + "	on r.id=r_u.reservation\n"
                + "join public.user u\n"
                + "	on r_u.userid=u.id\n"
                + "join course_professor c_p\n"
                + "	on r.course_professor=c_p.id\n"
                + "join professor p\n"
                + "	on c_p.professor=p.id\n"
                + "join course c\n"
                + "	on c_p.course=c.id\n"
                + "join slot s\n"
                + "	on r.slot=s.id\n"
                + "join day d\n"
                + "     on d.id=s.day\n"
                + "where u.id=" + userId;
        ResultSet res;
        JSONArray reservation = new JSONArray();
        try {
            res = dB.query(query);
            while (res.next()) {
                JSONArray a = new JSONArray();
                a.put(res.getString("cname"));
                a.put(res.getString("pname"));
                a.put(res.getString("pusername"));
                a.put(res.getString("dayName"));
                a.put(res.getString("startHour"));
                a.put(res.getString("endHour"));
                a.put(res.getString("rid"));
                a.put(res.getString("ruid"));
                reservation.put(a);
            }
        } catch (SQLException ex) {
            dB.closeConnection();
            throw ex;
        }
        return reservation;
    }

    public void deleteReservation(String rid, String ruid, String uid) throws SQLException {
        String query
                = "update reservation\n"
                + "set state = 'free'\n"
                + "from reservation_user\n"
                + "where reservation = " + rid + " and reservation_user.reservation = reservation.id and userid = " + uid + ";\n"
                + "\n"
                + "delete from reservation_user\n"
                + "where id = " + ruid + " and userid = " + uid + ";";
        dB.update(query);
    }
}
