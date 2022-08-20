package dev.myclinic.vertx.dto;


public class ConductDTO {
	public int conductId;
	public int visitId;
	public int kind;

	public static ConductDTO copy(ConductDTO src){
		ConductDTO dst = new ConductDTO();
		dst.conductId = src.conductId;
		dst.visitId = src.visitId;
		dst.kind = src.kind;
		return dst;
	}

	@Override
	public String toString(){
		return "ConductDTO[" +
		"conductId=" + conductId + "," +
		"visitId=" + visitId + "," +
		"kind=" + kind + //"," +
		"]";
	}
}