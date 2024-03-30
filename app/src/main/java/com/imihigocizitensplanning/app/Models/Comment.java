package com.imihigocizitensplanning.app.Models;
import java.util.ArrayList;
import java.util.List;

public class Comment {

    String userMsg;
    String userName;
    List<Reply> replies; // List to store multiple replies

    public Comment() {
        replies = new ArrayList<>();
    }


    public Comment(String userMsg, String userName) {
        this.userMsg = userMsg;
        this.userName = userName;
        replies = new ArrayList<>();
    }

    public String getUserMsg() {
        return userMsg;
    }

    public void setUserMsg(String userMsg) {
        this.userMsg = userMsg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    // Method to add a reply
    public void addReply(String replyMsg, String repliedBy) {
        Reply reply = new Reply(replyMsg, repliedBy);
        replies.add(reply);
    }

    // You can create a Reply class to store reply information
    public static class Reply {
        String replyMsg;
        String repliedBy;

        public Reply() {
        }

        public Reply(String replyMsg, String repliedBy) {
            this.replyMsg = replyMsg;
            this.repliedBy = repliedBy;
        }

        public String getReplyMsg() {
            return replyMsg;
        }

        public String getRepliedBy() {
            return repliedBy;
        }
    }
}
