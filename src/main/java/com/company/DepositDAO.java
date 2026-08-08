package com.company;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepositDAO {

    public BigDecimal deposit(long accountNumber, BigDecimal depositAmount) {

        // Validate deposit amount
        if (depositAmount == null || depositAmount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Deposit amount must be greater than 0.");
            return null;
        }

        Connection con = null;

        try {
            con = databaseConnection.getConnection();
            con.setAutoCommit(false);

            // Get current balance
            String sql1 = "SELECT balance FROM accounts WHERE account_no = ?";
            PreparedStatement pstmt1 = con.prepareStatement(sql1);
            pstmt1.setLong(1, accountNumber);

            System.out.println("DAO Account Number: " + accountNumber);

            ResultSet rs = pstmt1.executeQuery();

            boolean found = rs.next();
            System.out.println("Account found in DAO: " + found);

            if (!found) {
                System.out.println("Account not found!");
                con.rollback();
                return null;
            }

            BigDecimal currentBalance = rs.getBigDecimal("balance");
            BigDecimal newBalance = currentBalance.add(depositAmount);

            System.out.println("Current Balance: " + currentBalance);
            System.out.println("Deposit Amount: " + depositAmount);
            System.out.println("New Balance: " + newBalance);

            // Update balance
            String sql2 = "UPDATE accounts SET balance = ? WHERE account_no = ?";
            PreparedStatement pstmt2 = con.prepareStatement(sql2);

            pstmt2.setBigDecimal(1, newBalance);
            pstmt2.setLong(2, accountNumber);

            int rows = pstmt2.executeUpdate();

            System.out.println("Rows updated: " + rows);

            if (rows == 0) {
                con.rollback();
                return null;
            }

            // Insert transaction history
            String sql3 = "INSERT INTO transactions(account_no, transaction_type, amount) VALUES (?, ?, ?)";
            PreparedStatement pstmt3 = con.prepareStatement(sql3);

            pstmt3.setLong(1, accountNumber);
            pstmt3.setString(2, "DEPOSIT");
            pstmt3.setBigDecimal(3, depositAmount);

            int transactionRows = pstmt3.executeUpdate();

            System.out.println("Transaction rows inserted: " + transactionRows);

            if (transactionRows == 0) {
                con.rollback();
                return null;
            }

            // Commit both operations
            con.commit();

            System.out.println("Deposit Successful!");
            System.out.println("New Balance: " + newBalance);

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