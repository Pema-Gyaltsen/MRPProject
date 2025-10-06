package at.technikum.application.mrp.repository;

import at.technikum.application.mrp.model.Media;
import at.technikum.server.util.Db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MediaRepository {

    private Media map(ResultSet rs) throws SQLException {
        Media m = new Media();
        m.id = rs.getInt("id");
        m.creatorId = rs.getInt("creator_id");
        m.title = rs.getString("title");
        m.description = rs.getString("description");
        m.mediaType = rs.getString("media_type");
        m.releaseYear = rs.getInt("release_year");
        m.ageRestriction = rs.getInt("age_restriction");
        m.genres = (String[]) rs.getArray("genres").getArray();
        m.createdAt = rs.getTimestamp("created_at").toInstant();
        m.updatedAt = rs.getTimestamp("updated_at").toInstant();
        return m;
    }

    public Media create(Media m){
        String sql = """
            INSERT INTO media(creator_id,title,description,media_type,release_year,age_restriction,genres)
            VALUES(?,?,?,?,?,?,?) RETURNING id, created_at, updated_at
        """;
        try(Connection c = Db.connect(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, m.creatorId);
            ps.setString(2, m.title);
            ps.setString(3, m.description);
            ps.setString(4, m.mediaType);
            ps.setInt(5, m.releaseYear);
            ps.setInt(6, m.ageRestriction);
            ps.setArray(7, c.createArrayOf("text", m.genres));
            try(ResultSet rs = ps.executeQuery()){
                rs.next();
                m.id = rs.getInt("id");
                m.createdAt = rs.getTimestamp("created_at").toInstant();
                m.updatedAt = rs.getTimestamp("updated_at").toInstant();
                return m;
            }
        } catch (SQLException e){ throw new RuntimeException(e); }
    }

    public Media findById(int id){
        String sql="SELECT * FROM media WHERE id=?";
        try(Connection c=Db.connect(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1, id);
            try(ResultSet rs=ps.executeQuery()){
                return rs.next()? map(rs) : null;
            }
        } catch(SQLException e){ throw new RuntimeException(e); }
    }

    public List<Media> listAll(){
        String sql="SELECT * FROM media ORDER BY id";
        try(Connection c=Db.connect(); PreparedStatement ps=c.prepareStatement(sql);
            ResultSet rs=ps.executeQuery()){
            List<Media> list = new ArrayList<>();
            while(rs.next()) list.add(map(rs));
            return list;
        } catch(SQLException e){ throw new RuntimeException(e); }
    }

    public Media update(Media m){
        String sql = """
            UPDATE media SET title=?, description=?, media_type=?, release_year=?, 
            age_restriction=?, genres=?, updated_at=NOW()
            WHERE id=? AND creator_id=? RETURNING *
        """;
        try(Connection c=Db.connect(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1, m.title);
            ps.setString(2, m.description);
            ps.setString(3, m.mediaType);
            ps.setInt(4, m.releaseYear);
            ps.setInt(5, m.ageRestriction);
            ps.setArray(6, c.createArrayOf("text", m.genres));
            ps.setInt(7, m.id);
            ps.setInt(8, m.creatorId);
            try(ResultSet rs=ps.executeQuery()){
                return rs.next()? map(rs) : null;
            }
        } catch(SQLException e){ throw new RuntimeException(e); }
    }

    public boolean delete(int id, int creatorId){
        String sql="DELETE FROM media WHERE id=? AND creator_id=?";
        try(Connection c=Db.connect(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,id);
            ps.setInt(2,creatorId);
            return ps.executeUpdate()>0;
        } catch(SQLException e){ throw new RuntimeException(e); }
    }
}
