package actors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

class BookingActorJDBCConnector {

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

    /**
     * Returns all rows containing location information for the specified username
     * @return An arraylist of all booked trips
     */
    ArrayList<String> selectAllBookedTrips() {
        Connection conn = null;
        ArrayList<String> bookedTrips = new ArrayList<String>();

        String sqlStmt = "SELECT tripID FROM bookedtrips;";

        try {
            conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlStmt);

            while(rs.next()) {
                bookedTrips.add(rs.getString("tripID"));
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

        return bookedTrips;
    }

    String selectTripSegmentsForTrip(String tripId) {
        Connection conn = null;
        String tripSegments = null;

        String sqlStmt = "SELECT segments FROM bookedtrips WHERE tripID=?;";

        try {
            conn = connect();
            PreparedStatement pstmt  = conn.prepareStatement(sqlStmt);

            pstmt.setString(1, tripId);

            ResultSet rs  = pstmt.executeQuery();

            while(rs.next()) {
                tripSegments = rs.getString("segments");
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

        return tripSegments;
    }
}
