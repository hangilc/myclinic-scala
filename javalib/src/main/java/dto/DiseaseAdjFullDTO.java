package dev.myclinic.vertx.dto;

public class DiseaseAdjFullDTO {
    public DiseaseAdjDTO diseaseAdj;
    public ShuushokugoMasterDTO master;

    public DiseaseAdjFullDTO(){

    }

    public DiseaseAdjFullDTO(DiseaseAdjDTO diseaseAdj, ShuushokugoMasterDTO master) {
        this.diseaseAdj = diseaseAdj;
        this.master = master;
    }

    @Override
    public String toString() {
        return "DiseaseAdjFullDTO{" +
                "diseaseAdj=" + diseaseAdj +
                ", master=" + master +
                '}';
    }

    public static dev.myclinic.vertx.dto.DiseaseAdjFullDTO create(dev.myclinic.vertx.dto.DiseaseAdjDTO adj, ShuushokugoMasterDTO master){
        dev.myclinic.vertx.dto.DiseaseAdjFullDTO result = new dev.myclinic.vertx.dto.DiseaseAdjFullDTO();
        result.diseaseAdj = adj;
        result.master = master;
        return result;
    }
}
