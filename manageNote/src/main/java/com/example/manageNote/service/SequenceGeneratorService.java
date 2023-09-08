package com.example.manageNote.service;

import com.example.manageNote.model.DatabaseSequence;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Service for managing the ID sequence for notes.
 */
@Service
public class SequenceGeneratorService {

    /**
     * MongoDB operations used to handle interactions with the database.
     * @param mongoOperations The MongoOperations instance used to interact with MongoDB.
     */
    private final MongoOperations mongoOperations;

    /**
     * Constructor for the SequenceGeneratorService class.
     * @param mongoOperations The MongoOperations instance used to interact with MongoDB.
     */
    public SequenceGeneratorService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    /**
     * Generates a sequence of IDs for integer-type notes.
     * @param seqName The name of the sequence to generate.
     * @return The generated ID sequence.
     */
    public long generateSequence(String seqName) {

        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;

    }
}