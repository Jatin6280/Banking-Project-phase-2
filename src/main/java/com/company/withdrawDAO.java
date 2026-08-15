 package com.company;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class withdrawDAO {

    public BigDecimal withdraw(long accountNo, BigDecimal withdrawAmount) {

        Connection con = databaseConnection.getConnection();

        try {
            con.setAutoCommit(false);

            // Get current balance
            String sql = "SELECT balance FROM accounts WHERE account_no=?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, accountNo);

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Account invalid");
                con.rollback();
                return null;
            }

            BigDecimal currentBalance = rs.getBigDecimal("balance");

            // Validate amount
            if (withdrawAmount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Withdraw amount must be greater than zero");
                con.rollback();
                return null;
            }

            // Check balance
            if (withdrawAmount.compareTo(currentBalance) > 0) {
                System.out.println("Not enough balance :(");
                con.rollback();
                return null;
            }

            // Calculate new balance
            BigDecimal newBalance =
                    currentBalance.subtract(withdrawAmount);

            // Update balance
            String sql2 =
                    "UPDATE accounts SET balance=? WHERE account_no=?";

            PreparedStatement pstmt2 = con.prepareStatement(sql2);

            pstmt2.setBigDecimal(1, newBalance);
            pstmt2.setLong(2, accountNo);

            int rows = pstmt2.executeUpdate();

            if (rows == 0) {
                con.rollback();
                return null;
            }

            // Everything successful
            con.commit();

            return newBalance;

        } catch (SQLException e) {

            e.printStackTrace();

            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return null;

        } finally {

            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
