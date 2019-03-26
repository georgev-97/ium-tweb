
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        this.dB=dB;
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

    public boolean checkLogin(String account, String password) {
            User user = retrieveUser(account, password);
            if (user != null) {
                System.out.println("Correct");
                return true;
            } else {
                System.out.println("Not very correct");
                return false;
            }
    }

    private User retrieveUser(String account, String password) {
        try {
            ResultSet rs = dB.query("Select * FROM public.user WHERE account = '" + account + "' AND password = '" + password + "'");
            if (rs.next()) {
                User user = new User(rs.getInt("id"),rs.getString("account"), rs.getInt("role"));
                dB.closeConnection();
                return user;
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            dB.closeConnection();
        }
        return null;
    }

    

    public class User {

        private int id;
        private String account;
        private int role;

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public User(int id, String account, int role) {
            this.id = id;
            this.account = account;
            this.role = role;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }
}
