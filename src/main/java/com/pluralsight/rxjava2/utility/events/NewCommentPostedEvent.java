package com.pluralsight.rxjava2.utility.events;

public class NewCommentPostedEvent extends EventBase {

    private String authorEmail;
    private String commenterEmail;
    private String commentText;

    public NewCommentPostedEvent(String authorEmail, String commenterEmail, String commentText) {
        this.authorEmail = authorEmail;
        this.commenterEmail = commenterEmail;
        this.commentText = commentText;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public String getCommentorEmail() {
        return commenterEmail;
    }

    public String getCommentText() {
        return commentText;
    }

    @Override
    public String toString() {
        return "NewCommentPostedEvent{" +
                "authorEmail='" + authorEmail + '\'' +
                ", commenterEmail='" + commenterEmail + '\'' +
                ", commentText='" + commentText + '\'' +
                "} " + super.toString();
    }
}
