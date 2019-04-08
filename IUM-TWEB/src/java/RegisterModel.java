
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author george
 */
public class RegisterModel {
    private final DataBase dB;

    public RegisterModel(DataBase dB) {
        this.dB=dB;
    }

    public void addUser(String account, String password) throws SQLException {
        try {
            String insertion = "INSERT INTO public.user(account,password,role)"
                    + "VALUES('"+account+"','"+password+"',1)";
            
            dB.update(insertion);
        } catch (SQLException ex) {
            throw ex;
        }
    }
}
