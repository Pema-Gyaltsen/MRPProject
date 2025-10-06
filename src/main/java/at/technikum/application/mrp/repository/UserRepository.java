package at.technikum.application.mrp.repository;

import at.technikum.application.mrp.model.User;
import at.technikum.server.util.Db;

import java.sql.*;

public class UserRepository {

    public User findByUsername(String username){
        String sql = "SELECT id, username, password_hash FROM users WHERE username = ?";
        try(Connection c = Db.connect(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, username);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    User u = new User();
                    u.id = rs.getInt("id");
                    u.username = rs.getString("username");
                    u.passwordHash = rs.getString("password_hash");
                    return u;
                }
                return null;
            }
        } catch (SQLException e){ throw new RuntimeException(e); }
    }

    public User create(String username, String passwordHash){
        String sql = "INSERT INTO users(username, password_hash) VALUES(?,?) RETURNING id";
        try(Connection c = Db.connect(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            try(ResultSet rs = ps.executeQuery()){
                rs.next();
                User u = new User();
                u.id = rs.getInt(1);
                u.username = username;
                u.passwordHash = passwordHash;
                return u;
            }
        } catch (SQLException e){
            if(e.getMessage()!=null && e.getMessage().contains("unique")) {
                throw new IllegalStateException("USERNAME_TAKEN");
            }
            throw new RuntimeException(e);
        }
    }

    public User findById(int id){
        String sql="SELECT id,username,password_hash FROM users WHERE id=?";
        try(Connection c=Db.connect(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,id);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    User u=new User();
                    u.id=rs.getInt("id");
                    u.username=rs.getString("username");
                    u.passwordHash=rs.getString("password_hash");
                    return u;
                }
                return null;
            }
        } catch(SQLException e){ throw new RuntimeException(e); }
    }
}
