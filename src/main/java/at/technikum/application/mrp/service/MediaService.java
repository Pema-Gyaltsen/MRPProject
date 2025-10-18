package at.technikum.application.mrp.service;

import at.technikum.application.mrp.model.MediaEntry;
import at.technikum.application.mrp.repository.MediaRepository;

import java.util.List;

public class MediaService {
    private final MediaRepository repo = new MediaRepository(); //service depends on MediaRepo. to access DB

    public MediaEntry create(int creatorId, MediaEntry m){
        m.creatorId = creatorId; //overwrite m.creatorID with authenticated user's ID
        return repo.create(m);
    }// returns newly persisted Media

    public MediaEntry get(int id){ return repo.findById(id); }
    //read (by ID), return Media

    public List<MediaEntry> all(){ return repo.listAll(); }

    public MediaEntry update(int creatorId, MediaEntry m){
        m.creatorId = creatorId;
        return repo.update(m);
    }

    public boolean delete(int creatorId, int mediaId){
        return repo.delete(mediaId, creatorId);
    }
}
