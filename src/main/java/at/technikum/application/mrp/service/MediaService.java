package at.technikum.application.mrp.service;

import at.technikum.application.mrp.model.Media;
import at.technikum.application.mrp.repository.MediaRepository;

import java.util.List;

public class MediaService {
    private final MediaRepository repo = new MediaRepository();

    public Media create(int creatorId, Media m){
        m.creatorId = creatorId;
        return repo.create(m);
    }

    public Media get(int id){ return repo.findById(id); }

    public List<Media> all(){ return repo.listAll(); }

    public Media update(int creatorId, Media m){
        m.creatorId = creatorId;
        return repo.update(m);
    }

    public boolean delete(int creatorId, int mediaId){
        return repo.delete(mediaId, creatorId);
    }
}
