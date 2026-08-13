 package com.company;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class transferDAO {
    public boolean transfer(int senderAccount, int receiverAccount, BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO)<=0){
            System.out.println("enter positive value");
            return false;

        }
        Connection con=null;
        try{
            con=databaseConnection.getConnection();
            con.setAutoCommit(false);

            String sql="select account_no,balance from accounts where account_no= ?";
            PreparedStatement pstmt=con.prepareStatement(sql);

            pstmt.setInt(1,senderAccount);

            ResultSet rs= pstmt.executeQuery();
            while (rs.next()){
                long accountNo = rs.getLong("account_no");
                BigDecimal balance = rs.getBigDecimal("balance");

                System.out.println(accountNo + " : " + balance);
                if (balance.compareTo(amount) < 0) {
                    System.out.println("Insufficient balance!");
                    con.rollback();
                    return false;
                }
            }

            String sql2="update accounts set balance  = balance -? where account_no=?";
            PreparedStatement pstmt2=con.prepareStatement(sql2);
            pstmt2.setBigDecimal(1,amount);
            pstmt2.setInt(2,senderAccount);
            int rowsUpdated= pstmt2.executeUpdate();
            if(rowsUpdated<=0){
                con.rollback();
                return false;
            }

            String sql3="update accounts set balance = balance + ? where account_no=?";
            PreparedStatement pstmt3=con.prepareStatement(sql3);
            pstmt3.setBigDecimal(1,amount);
            pstmt3.setInt(2,receiverAccount);

            int rowsUpdated3=pstmt3.executeUpdate();
            if(rowsUpdated3<=0){
                con.rollback();
                return false;
            }
            con.commit();
            System.out.println("Success");

            return true;
        }
        catch (SQLException e) {

            e.printStackTrace();

            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return false;

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
