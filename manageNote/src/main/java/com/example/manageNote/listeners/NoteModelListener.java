package com.example.manageNote.listeners;


import com.example.manageNote.model.Note;
import com.example.manageNote.service.SequenceGeneratorService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

/**
 * This class serves as a listener for MongoDB events related to the 'Note' model.
 * It is responsible for generating unique IDs for Note entities before they are saved to the database.
 */
@Component
public class NoteModelListener extends AbstractMongoEventListener<Note> {

    private final SequenceGeneratorService sequenceGenerator;

    /**
     * Constructs a new NoteModelListener with the provided SequenceGeneratorService.
     *
     * @param sequenceGenerator The service responsible for generating unique sequences.
     */
    public NoteModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    /**
     * Overrides the onBeforeConvert method to generate and set a unique ID for a Note entity
     * before it is converted and saved to the database.
     *
     * @param event The BeforeConvertEvent that triggered this method.
     */
    @Override
    public void onBeforeConvert(BeforeConvertEvent<Note> event) {
        if (event.getSource().getId() == null || event.getSource().getId() < 1) {
            event.getSource().setId((int)sequenceGenerator.generateSequence(Note.SEQUENCE_NAME));
        }
    }


}
