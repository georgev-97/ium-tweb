
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

    boolean addUser(String account, String password) {
        try {
            String insertion = "INSERT INTO public.user(account,password,role)"
                    + "VALUES('"+account+"','"+password+"',1)";
            /*INSERT INTO public."user"(
	id, account, password, role)
	VALUES (?, ?, ?, ?);*/
            dB.update(insertion);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
}
