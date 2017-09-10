package actors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OperatorsTableConnector {

    static final String DB_URL = "jdbc:sqlite:windbooker.db";

    /**
     * Establishes a connection to the database
     * @return Connection object to database
     */
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    ArrayList<String> selectAllAirlineOperators() {
        Connection conn = null;
        ArrayList<String> airlineOperators = new ArrayList<String>();

        String sqlStmt = "SELECT opcode FROM operators;";

        try {
            conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlStmt);

            while(rs.next()) {
                airlineOperators.add(rs.getString("opcode"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace(System.out);
            }
        }

        return airlineOperators;
    }
}
