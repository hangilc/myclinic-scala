package dev.myclinic.vertx.dto;

import java.util.List;

public class IntraclinicPostFullPageDTO {
    public int totalPages;
    public int page;
    public List<IntraclinicPostFullDTO> posts;

    @Override
    public String toString() {
        return "IntraclinicPostFullPageDTO{" +
                "totalPages=" + totalPages +
                ", posts=" + posts +
                '}';
    }
}
