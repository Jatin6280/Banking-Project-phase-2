package com.company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.mysql.cj.conf.DatabaseUrlContainer;

public class loginDAO {
    public boolean loginUser(int account_no,String enteredPassword){
        String sql="select password from users WHERE account_no=?";
        try(Connection con =  databaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)){
           pstmt.setInt(1,account_no);
            try(ResultSet rs= pstmt.executeQuery()){
                if(rs.next()){
                    String storedHash=rs.getString("password");
                    // Compare entered password with stored bcrypt hash

                    BCrypt.Result result=BCrypt.verifyer().verify(enteredPassword.toCharArray(),storedHash);
                    return  result.verified;//true if pass. matches
                }
                else{
                    return false;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
