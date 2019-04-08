
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lorenzo
 */
public class ReservationModel {

    //modello in cui è implemenmtata la gestione e la visualizzazione delle prenotazioni
    private final DataBase dB;

    public ReservationModel(DataBase dB) {
        this.dB = dB;
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
}
