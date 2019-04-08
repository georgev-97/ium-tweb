//                                         //
// CLASS TO STORE DATABASE PARAMITER AND   //
//              PERFORM ACTION             // 

import java.sql.*;

public class DataBase {

    private final String url;
    private final String user;
    private final String pass;
    private Connection connection = null;
    private Statement stmt = null;
    private ResultSet queryResult = null;

    public DataBase(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        try {
            Class.forName("org.postgresql.Driver");//driver
        } catch (ClassNotFoundException e) {
            //TODO HANDLE ERROR
        }
    }

    //Method used to perform sql quey.//
    //After this method is called, should be//
    //recalled closeConnection() method//
    public ResultSet query(String query) throws SQLException {
        connection = DriverManager.getConnection(url, user, pass);//connection

        stmt = (Statement) connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        //
        queryResult = stmt.executeQuery(query);                            //performing query
        return queryResult;
    }

    //Method used to perform update like://
    //row insertion, row deletion,       //
    //table creatin and table delation.  //
    //Is not needed recall closeConnection()//
    public int update(String query) throws SQLException {
        int queryResult = -1;
        try {
            connection = DriverManager.getConnection(url, user, pass);//connection

            stmt = (Statement) connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);   //
            queryResult = stmt.executeUpdate(query);                            //performing query
            closeConnection();
        } catch (SQLException ex) {
            closeConnection();
            throw ex;
        }
        return queryResult;
    }

    //break the connection to the db//
    public void closeConnection() {
        try {
            if (queryResult != null) {
                queryResult.close();  //
            }
            if (stmt != null) {
                stmt.close();                //
            }
            if (connection != null) {
                connection.close();    //closing resources
            }
        } catch (SQLException ex) {
            //TODO handle exception
        }
    }
}
