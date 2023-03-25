package com.example.manageNote.listeners;


import com.example.manageNote.model.Note;
import com.example.manageNote.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class NoteModelListener extends AbstractMongoEventListener<Note> {

    private SequenceGeneratorService sequenceGenerator;

    /**
     *
     * @param sequenceGenerator
     */
    @Autowired
    public NoteModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    /**
     *
     * @param event
     */
    @Override
    public void onBeforeConvert(BeforeConvertEvent<Note> event) {
        if (event.getSource().getId() == null || event.getSource().getId() < 1) {
            event.getSource().setId((int)sequenceGenerator.generateSequence(Note.SEQUENCE_NAME));
        }
    }


}
