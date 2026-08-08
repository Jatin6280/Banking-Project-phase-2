package com.company;
import com.company.databaseConnection; // adjust package path to match where the file actually lives
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.SecureRandom;
public class RegisterDAO {

    public boolean registerUser(String user_name, String user_pan,
                                String user_email, String user_password,
                                int user_accountNo) {

            Connection conn = null;

            String sql = "INSERT INTO users (name, pan, email, password, account_no) VALUES (?, ?, ?, ?, ?)";
            String sql1 = "INSERT INTO accounts (account_no) VALUES (?)";

            try {

                conn = databaseConnection.getConnection();
                conn.setAutoCommit(false);

                // Insert into users table
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, user_name);
                pstmt.setString(2, user_pan);
                pstmt.setString(3, user_email);
                pstmt.setString(4, user_password);
                pstmt.setInt(5, user_accountNo);

            int rowsAffected = pstmt.executeUpdate();

            // Insert into accounts table
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);

            pstmt1.setLong(1, user_accountNo);

            int rowsAffected2 = pstmt1.executeUpdate();

            // Commit transaction
            conn.commit();

            return rowsAffected > 0 && rowsAffected2 > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return false;

        } finally {

            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}