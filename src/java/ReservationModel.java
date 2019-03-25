
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public ReservationModel(DataBase dB){this.dB = dB;}
    
    public ArrayList getCourse(){
        ArrayList<String> res = new ArrayList();
        try {
            ResultSet rs = dB.query("SELECT nome FROM public.corso");
            while(rs.next()){
                res.add(rs.getString("nome"));
            }
        } catch (SQLException ex) {
            res.add("ERROR : "+ex.toString());
        } finally {
            dB.closeConnection();
        }
        return res;
    }
}
