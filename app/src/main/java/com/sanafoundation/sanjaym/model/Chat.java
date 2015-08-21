package com.sanafoundation.sanjaym.model;

/**
 * Created by IM028 on 7/13/15.
 */
public class Chat {

    private String message;
    private String author;
    private String groupId;
    private boolean isSelf;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Chat() {
    }

    public Chat(String groupId, String message, String author) {
        this.message = message;
        this.author = author;
        this.groupId = groupId;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public String getGroupId() {
        return groupId;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }
}
