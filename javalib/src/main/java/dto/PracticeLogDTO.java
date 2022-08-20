package dev.myclinic.vertx.dto;

public class PracticeLogDTO {

    public int serialId;
    public String createdAt;
    public String kind;
    public String body;

    @Override
    public String toString() {
        return "PracticeLogDTO{" +
                "serialId=" + serialId +
                ", createdAt='" + createdAt + '\'' +
                ", kind='" + kind + '\'' +
                ", body=" + body +
                '}';
    }
}
