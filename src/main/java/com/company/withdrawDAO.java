 package com.company;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class withdrawDAO {
    public BigDecimal withdraw(long accountNo, BigDecimal withdrawAmount){
        Connection con=null;

        try{
            String sql="select balance from accounts where account_no=?";
            PreparedStatement pstmt=con.prepareStatement(sql);
            pstmt.setLong(1,accountNo);
            pstmt.setBigDecimal(2,withdrawAmount);

            ResultSet rs= pstmt.executeQuery();
            if(!rs.next()){
                System.out.println("Account invalid");
                con.rollback();
                return null;
            }
            BigDecimal  currentBalance=rs.getBigDecimal("balance");
            if(withdrawAmount.compareTo(BigDecimal.ZERO)<=0){
                System.out.println("Withdraw  amount must be greater than zero");
                con.rollback();
                return  null;
            }
            if(withdrawAmount.compareTo(currentBalance)>0){
                System.out.println("Not enough balance :(");
                con.rollback();
                return null;
            }
            //taking balance  from  sql
           BigDecimal newBalance = currentBalance.subtract(withdrawAmount);
            String sql1="Select balance from accounts where accout_no=?";
            PreparedStatement pstmt1= con.prepareStatement(sql1);
            pstmt1.setLong(1,accountNo);
            ResultSet rs1=pstmt1.executeQuery();
            if(rs.next()){
                currentBalance=rs.getBigDecimal("balance");
            }

            //updating balance
            String sql2="update accounts set balance =? where account_no=?";
            PreparedStatement pstmt2=con.prepareStatement(sql2);
            pstmt2.setBigDecimal(1,newBalance);
            pstmt2.setLong(2,accountNo);

            int rows = pstmt2.executeUpdate();
            if(rows==0){
                con.rollback();
                return null;
            }
            return newBalance;

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

            return null;
        }
        finally {

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
