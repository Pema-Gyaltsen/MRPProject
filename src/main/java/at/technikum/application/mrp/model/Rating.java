// Rating.java  (stub is enough for Intermediate; you’ll use it in Final)
package at.technikum.application.mrp.model;

import java.time.Instant;

public class Rating {
    public Integer id;
    public Integer mediaId;
    public Integer userId;
    public int     stars;          // 1..5
    public String  comment;        // optional
    public boolean confirmed;      // moderation flag (final phase)
    public int     likes;          // final phase
    public Instant createdAt = Instant.now();
    public Instant updatedAt = Instant.now();
}
