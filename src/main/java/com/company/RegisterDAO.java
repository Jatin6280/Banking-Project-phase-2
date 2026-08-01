package com.company;
import com.company.databaseConnection; // adjust package path to match where the file actually lives
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterDAO {

    public boolean registerUser(String user_name, String user_pan, String user_email, String user_password) {
        // 1. Corrected SQL: 4 columns need 4 placeholders (?)
        String sql = "INSERT INTO users (name, pan, email, password) VALUES (?, ?, ?, ?)";

        try (
                Connection conn = databaseConnection.getConnection(); // If getConnection() is NOT static
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 2. Setting the 4 parameters
            pstmt.setString(1, user_name);
            pstmt.setString(2, user_pan);
            pstmt.setString(3, user_email);
            pstmt.setString(4, user_password);

            // 3. Execute and return
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}