package dev.myclinic.vertx.dto;

public class IntraclinicCommentDTO {
    public int id;
    public String name;
    public String content;
    public int postId;
    public String createdAt;

    @Override
    public String toString() {
        return "IntraclinicCommentDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", postId=" + postId +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
