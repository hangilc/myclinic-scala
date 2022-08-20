package dev.myclinic.vertx.dto;


public class ConductShinryouDTO {
	public int conductShinryouId;
	public int conductId;
	public int shinryoucode;

	public static dev.myclinic.vertx.dto.ConductShinryouDTO copy(dev.myclinic.vertx.dto.ConductShinryouDTO src){
		dev.myclinic.vertx.dto.ConductShinryouDTO dst = new dev.myclinic.vertx.dto.ConductShinryouDTO();
		dst.conductShinryouId = src.conductShinryouId;
		dst.conductId = src.conductId;
		dst.shinryoucode = src.shinryoucode;
		return dst;
	}
	
	@Override
	public String toString(){
		return "ConductShinryouDTO[" +
			"conductShinryouId=" + conductShinryouId + ", " +
			"conductId=" + conductId + ", " +
			"shinryoucode=" + shinryoucode +
		"]";
	}
}
