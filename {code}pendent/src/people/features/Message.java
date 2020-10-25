package people.features;

import tools.Input;

import java.util.UUID;

public class Message {


    private String messageId;
    private String subject;
    private Input input = Input.getInstance();
    private String body;
    private String senderID;
    private String sender;
    private boolean readStatus;

    public Message(String subject, String body, String senderID, String sender) {

        this.messageId = UUID.randomUUID().toString();
        this.body = body;
        this.subject = subject;
        this.senderID = senderID;
        this.sender = sender;
        this.readStatus = false;
    }

    public void setReadStatus(boolean status) {
        this.readStatus = status;
    }

    public boolean getReadStatus() {
        return this.readStatus;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String toString() {
        return input.EOL + "Message ID: " + messageId + input.EOL + "Senders ID: " + senderID +
                input.EOL + "Sender: " + sender + input.EOL + "Title: " + subject + input.EOL + "Message: " + body;
    }
}
