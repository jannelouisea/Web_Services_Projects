package actors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

public class FlightsTableConnector {

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

    ArrayList<String> getAllFlightsForOperator(String opcode) {
        Connection conn = null;
        ArrayList<String> flights = new ArrayList<String>();

        String sqlStmt = "SELECT flightcode FROM flights WHERE opcode=?;";

        try {
            conn = connect();
            PreparedStatement pstmt  = conn.prepareStatement(sqlStmt);

            pstmt.setString(1, opcode);

            ResultSet rs  = pstmt.executeQuery();

            while(rs.next()) {
                flights.add(rs.getString("flightcode"));
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }

        return flights;
    }

    Integer getAvailableSeatsForFlight(String flightcode) {
        Connection conn = null;
        int seats = -1;

        String sqlStmt = "SELECT seats FROM flights WHERE flightcode=?;";

        try {
            conn = connect();
            PreparedStatement pstmt  = conn.prepareStatement(sqlStmt);

            pstmt.setString(1, flightcode);

            ResultSet rs  = pstmt.executeQuery();

            while(rs.next()) {
                seats = rs.getInt("seats");
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }

        return seats;

    }
}
