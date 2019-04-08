
import java.sql.ResultSet;
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
public class LoginModel {

    private final DataBase dB;

    public LoginModel(DataBase dB) {
        this.dB = dB;
    }

    public boolean checkAccountExistance(String account) {
        try {
            ResultSet rs = dB.query("Select * FROM public.user WHERE account = '" + account + "'");
            return rs.first();

        } catch (SQLException ex) {
            return false;
        } finally {
            dB.closeConnection();
        }
    }

    public User login(String account, String password) {
        if (account == null || password == null) return null;
        return retrieveUser(account, Hash.md5(password));
    }

    private User retrieveUser(String account, String password) {
        try {
            String query = "select * from public.user where account = '" + account + "' and password = '" + password + "'";
            ResultSet rs = dB.query(query);
            if (rs.next()) {
                User user = new User(rs.getString("id"), rs.getString("account"), rs.getInt("role"));
                System.out.println(user.getAccount());
                return user;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        } finally {
            dB.closeConnection();
        }
        return null;
    }
}
