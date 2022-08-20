package dev.myclinic.vertx.dto;

public class HotlineDTO {
    public int hotlineId;
    public String message;
    public String sender;
    public String recipient;
    public String postedAt;

    @Override
    public String toString() {
        return "HotlineDTO{" +
                "hotlineId=" + hotlineId +
                ", message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", postedAt='" + postedAt + '\'' +
                '}';
    }
}
