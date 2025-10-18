package at.technikum.application.mrp.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.time.Instant;
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaEntry {
    public Integer id;
    public Integer creatorId;
    public String title;
    public String description;
    public String mediaType;     // movie | series | game
    public Integer releaseYear;
    public Integer ageRestriction;
    public String[] genres;
    public Instant createdAt;
    public Instant updatedAt;
}
