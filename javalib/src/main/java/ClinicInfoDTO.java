package dev.myclinic.java;

public class ClinicInfoDTO {

    public String name;
    public String postalCode;
    public String address;
    public String tel;
    public String fax;
    public String todoufukencode;
    public String tensuuhyoucode;
    public String kikancode;
    public String homepage;
    public String doctorName;
    public String doctorLastName;
    public String doctorFirstName;

    @Override
    public String toString() {
        return "ClinicInfoDTO{" +
                "name='" + name + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", address='" + address + '\'' +
                ", tel='" + tel + '\'' +
                ", fax='" + fax + '\'' +
                ", todoufukencode='" + todoufukencode + '\'' +
                ", tensuuhyoucode='" + tensuuhyoucode + '\'' +
                ", kikancode='" + kikancode + '\'' +
                ", homepage='" + homepage + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", doctorLastName='" + doctorLastName + '\'' +
                ", doctorFirstName='" + doctorFirstName + '\'' +
                '}';
    }
}