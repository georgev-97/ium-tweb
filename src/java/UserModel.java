<<<<<<< HEAD

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
=======
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
>>>>>>> f2e448dfbfddb696835adef30acd7879d6989f34

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
<<<<<<< HEAD

/**
 *
 * @author george
 */
public class UserModel {
    
=======
/**
 *
 * @author lorenzo
 */
public class UserModel {
>>>>>>> f2e448dfbfddb696835adef30acd7879d6989f34

    //modello per la gestione delle attività di amministrazione 
    private final DataBase dB;

    public UserModel(DataBase dB) {
        this.dB = dB;
    }

<<<<<<< HEAD
    ArrayList <UserReservation> getUserReservation(String user) {
        try{
            ResultSet rs1 = dB.query("Select * from public.user where account = '"+user+"'");
            int userid;
            if(rs1.next()){
                userid = rs1.getInt("id");
            }else {
                return null;
            }
            String query = "SELECT prof.name as pname, course.name as cname, slot.day, slot.startHour, slot.endHour from(" 
                    +"public.user join public.reservations_user on(public.user.id = public.reservations_user.userid) "
                    + "join public.reservation on(public.reservations_user.reservation = public.reservation.id)"
                    + "join slot on(public.reservation.slot = public.slot.id)"
                    + "join public.course_professor on(public.reservation.course_professor = public.course_professor.id) "
                    + "join public.course course on(public.course_professor.course = course.id) "
                    + "join public.professor prof on(public.course_professor.professor = prof.id)) where userid= "+userid;
            ResultSet rs = dB.query(query);
            ArrayList<UserReservation> ur = new ArrayList<>();
            int i = 0;
            while(rs.next()){
                ur.add(new UserReservation(i, rs.getString("cname"), rs.getString("pname"), rs.getString("day"), rs.getInt("startHour"), rs.getInt("endHour")));
                        i++;
            }
            ur.forEach((u) -> {
                System.out.println(u.getCourse() + "A");
            });
            return ur;
        } catch (SQLException ex) {
           System.out.println(ex);
           return null;
        }
=======
    public JSONArray getReservation(String course, String professorUsername) throws SQLException {
        String query
                = "select r.id, c.name, p.userName, p.name, day, startHour, endHour, state\n"
                + "from reservation r \n"
                + "join course_professor c_p on r.course_professor = c_p.id\n"
                + "join course c on c_p.course = c.id\n"
                + "join professor p on c_p.professor = p.id\n"
                + "join slot s on r.slot = s.id\n"
                + "where c.name = '" + course + "' and p.username = '" + professorUsername + "'\n"
                + "order by r.id";
        ResultSet res = dB.query(query);

        JSONArray re = new JSONArray();
        while (res.next()) {
            JSONArray a = new JSONArray();
            a.put(res.getString("day"));
            a.put(res.getString("startHour"));
            a.put(res.getString("endHour"));
            a.put(res.getString("state"));
            a.put(res.getString("id"));
            re.put(a);
        }
        return re;
    }

    public void reserve(String slotId, String userId) throws SQLException {
        String query
                = "update reservation\n"
                + "set state = 'no-free'\n"
                + "where id = " + slotId + ";"
                + "update reservation\n"
                + "set state = 'no-free'\n"
                + "where id = 3;\n"
                + "\n"
                + "insert into reservation_user(reservation, userId)\n"
                + "values(" + slotId + "," + userId + ")";
        dB.update(query);
    }

    public JSONArray getReservation(String userId) throws SQLException {
        String query
                = "select c.name as cname, p.name as pname,\n"
                + "p.username as pusername, s.day, s.startHour, s.endHour\n"
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
                + "where u.id=" + userId;
        ResultSet res = dB.query(query);
        JSONArray reservation = new JSONArray();
        
          
        while (res.next()) {
            JSONArray a = new JSONArray();
            a.put(res.getString("cname"));
            a.put(res.getString("pname"));
            a.put(res.getString("pusername"));
            a.put(res.getString("day"));
            a.put(res.getString("startHour"));
            a.put(res.getString("endHour"));
            reservation.put(a);
        }
        return reservation;
>>>>>>> f2e448dfbfddb696835adef30acd7879d6989f34
    }
}
