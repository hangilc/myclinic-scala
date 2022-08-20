package dev.myclinic.vertx.dto;

public class PharmaDrugDTO {
    public int iyakuhincode;
    public String description;
    public String sideeffect;

    @Override
    public String toString() {
        return "PharmaDrugDTO{" +
                "iyakuhincode=" + iyakuhincode +
                ", description='" + description + '\'' +
                ", sideeffect='" + sideeffect + '\'' +
                '}';
    }
}
