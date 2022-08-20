package dev.myclinic.vertx.dto;

public class PharmaQueueDTO {
    public int visitId;
    public int pharmaState;

    public static dev.myclinic.vertx.dto.PharmaQueueDTO copy(dev.myclinic.vertx.dto.PharmaQueueDTO src){
        dev.myclinic.vertx.dto.PharmaQueueDTO dst = new dev.myclinic.vertx.dto.PharmaQueueDTO();
        dst.visitId = src.visitId;
        dst.pharmaState = src.pharmaState;
        return dst;
    }

    @Override
    public String toString() {
        return "PharmaQueueDTO{" +
                "visitId=" + visitId +
                ", pharmaState=" + pharmaState +
                '}';
    }
}
