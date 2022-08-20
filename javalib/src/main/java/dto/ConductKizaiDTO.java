package dev.myclinic.vertx.dto;

public class ConductKizaiDTO {
	public int conductKizaiId;
	public int conductId;
	public int kizaicode;
	public double amount;

	public static ConductKizaiDTO copy(ConductKizaiDTO src){
		ConductKizaiDTO dst = new ConductKizaiDTO();
		dst.conductKizaiId = src.conductKizaiId;
		dst.conductId = src.conductId;
		dst.kizaicode = src.kizaicode;
		dst.amount = src.amount;
		return dst;
	}
	
	@Override
	public String toString(){
		return "ConductKizaiDTO[" +
			"conductKizaiId=" + conductKizaiId + ", " +
			"conductId=" + conductId + ", " +
			"kizaicode=" + kizaicode + ", " +
			"amount=" + amount + 
		"]";
	}
}