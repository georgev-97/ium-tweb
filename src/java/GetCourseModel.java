
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class GetCourseModel {
    private final DataBase dB;
    public GetCourseModel(DataBase dB){this.dB = dB;}
    
    public JSONObject retrive(){
        JSONObject res = new JSONObject();
        try {
            ResultSet rs = dB.query("SELECT nome FROM public.corso");
            while(rs.next()){
                res.put("course",rs.getString("nome"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetCourseModel.class.getName()).log(Level.SEVERE, null, ex);
            res.put("error", ex.toString());
        } finally {
            dB.closeConnection();
        }
        return res;
    }
}
