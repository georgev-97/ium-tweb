
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

    public int checkLogin(String account, String password) {
            String p = Hash.md5(password);
            System.out.println(p);
            System.out.println(password);
            User user = retrieveUser(account, p);
            if (user != null) {
                System.out.println("Correct");
                if(user.getRole() == 0){
                    return 0;
                }
                else {
                    return 1;
                }
            } else {
                System.out.println("Not very correct");
                return -1;
            }
    }

    private User retrieveUser(String account, String password) {
        try {
            String query = "select * from public.user where account = '"+account+"' and password = '"+password+"'";
            ResultSet rs = dB.query(query);
            if (rs.next()) {
                User user = new User(rs.getString("id"),rs.getString("account"), rs.getInt("role"));
                System.out.println(user.getAccount());
                dB.closeConnection();
                return user;
            }else{System.out.println("Errore password");}
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        } finally {
            dB.closeConnection();
        }
        return null;
    }

    

    public class User {

        private String id;
        private String account;
        private int role;

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public User(String id, String account, int role) {
            this.id = id;
            this.account = account;
            this.role = role;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
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
