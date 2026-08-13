package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class transactionDAO {

    public List<Transaction> shortHistory(int account_no) {

        Connection conn = null;
        List<Transaction> transactions = new ArrayList<>();

        try {
            conn = databaseConnection.getConnection();

            String sql = "SELECT * FROM transactions " +
                    "WHERE account_no = ? " +
                    "ORDER BY transaction_time DESC LIMIT 5";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, account_no);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Transaction transaction = new Transaction();

                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setAccountNo(rs.getInt("account_no"));
                transaction.setTransactionType(rs.getString("transaction_type"));
                transaction.setAmount(rs.getInt("amount"));
                transaction.setTransactionTime(rs.getTimestamp("transaction_time"));

                transactions.add(transaction);
            }

            return transactions;

        } catch (SQLException e) {
            e.printStackTrace();
            return transactions;

        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}