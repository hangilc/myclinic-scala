package dev.myclinic.vertx.dto;

import java.util.Objects;

public class DiseaseAdjDTO {
    public int diseaseAdjId;
    public int diseaseId;
    public int shuushokugocode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        dev.myclinic.vertx.dto.DiseaseAdjDTO that = (dev.myclinic.vertx.dto.DiseaseAdjDTO) o;
        return diseaseAdjId == that.diseaseAdjId &&
                diseaseId == that.diseaseId &&
                shuushokugocode == that.shuushokugocode;
    }

    @Override
    public int hashCode() {

        return Objects.hash(diseaseAdjId, diseaseId, shuushokugocode);
    }

    @Override
    public String toString() {
        return "DiseaseAdjDTO{" +
                "diseaseAdjId=" + diseaseAdjId +
                ", diseaseId=" + diseaseId +
                ", shuushokugocode=" + shuushokugocode +
                '}';
    }
}
