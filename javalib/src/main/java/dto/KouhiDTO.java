package dev.myclinic.vertx.dto;

public class KouhiDTO {
	public int kouhiId;
	public int patientId;
	public int futansha;
	public int jukyuusha;
	public String validFrom;
	public String validUpto;

	public KouhiDTO copy(){
		KouhiDTO dst = new KouhiDTO();
		dst.kouhiId = kouhiId;
		dst.patientId = patientId;
		dst.futansha = futansha;
		dst.jukyuusha = jukyuusha;
		dst.validFrom = validFrom;
		dst.validUpto = validUpto;
		return dst;
	}

	public void assign(KouhiDTO src){
		kouhiId = src.kouhiId;
		patientId = src.patientId;
		futansha = src.futansha;
		jukyuusha = src.jukyuusha;
		validFrom = src.validFrom;
		validUpto = src.validUpto;
	}

	@Override
	public String toString(){
		return "KouhiDTO[" + 
			"kouhiId=" + kouhiId + "," + 
			"patientId=" + patientId + "," + 
			"futansha=" + futansha + "," + 
			"jukyuusha=" + jukyuusha + "," + 
			"validFrom=" + validFrom + "," + 
			"validUpto=" + validUpto + 
		"]";
	}
}