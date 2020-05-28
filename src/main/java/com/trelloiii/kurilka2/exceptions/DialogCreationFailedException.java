package com.trelloiii.kurilka2.exceptions;

public class DialogCreationFailedException extends RuntimeException {
    public DialogCreationFailedException() {
        super("Cant create dialog, one of user not in your contacts");
    }
}
