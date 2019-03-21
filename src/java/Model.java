
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
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
public class Model {

    private final String url;
    private final String usr;
    private final String password;

    public Model(String url, String usr, String password) {
        this.url = url;
        this.usr = usr;
        this.password = password;
    }

    public boolean checkAccountExistance(String account) {
        return retrieveUser(account) != null;
    }

    public boolean checkLogin(String account, String password) {
            User user = retrieveUser(account);
            if (user != null) {
                System.out.println("Correct");
                return user.getPassword().equals(password);
            } else {
                System.out.println("Not very correct");
                return false;
            }
    }

    private User retrieveUser(String account) {
        Connection conn = connect();
        try {
            Statement st = conn.createStatement();
            st.execute("Select * FROM public.user WHERE account = '" + account + "'");
            ResultSet rs = st.getResultSet();
            if (rs.next()) {
                User user = new User(rs.getInt("id"),rs.getString("account"), rs.getString("password"), rs.getInt("role"));
                System.out.println(user.getPassword());
                st.close();
                rs.close();
                conn.close();
                return user;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            disconnect(conn);
        }
        return null;
    }

    private Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
        }
        Properties props = new Properties();
        props.setProperty("user", this.usr);
        props.setProperty("password", this.password);
        try {
            Connection conn = DriverManager.getConnection(this.url, props);
            return conn;
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static void disconnect(Connection conn) {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class User {

        private int id;
        private String account;
        private String password;
        private int role;

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public User(int id, String account, String password, int role) {
            this.id = id;
            this.account = account;
            this.password = password;
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

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
