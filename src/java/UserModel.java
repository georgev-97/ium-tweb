
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    

    //modello per la gestione delle attività di amministrazione 
    private final DataBase dB;

    public UserModel(DataBase dB) {
        this.dB = dB;
    }

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
    }
}
