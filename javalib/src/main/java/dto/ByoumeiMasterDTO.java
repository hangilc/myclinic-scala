package dev.myclinic.vertx.dto;

public class ByoumeiMasterDTO {

    public int shoubyoumeicode;
    public String name;
    public String validFrom;
    public String validUpto;

    @Override
    public String toString() {
        return "ByoumeiMasterDTO{" +
                "shoubyoumeicode=" + shoubyoumeicode +
                ", name='" + name + '\'' +
                ", validFrom='" + validFrom + '\'' +
                ", validUpto='" + validUpto + '\'' +
                '}';
    }
}
