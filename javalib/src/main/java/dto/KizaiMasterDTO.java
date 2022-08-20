package dev.myclinic.vertx.dto;

public class KizaiMasterDTO {
	public int kizaicode;
	public String validFrom;
	public String name;
	public String yomi;
	public String unit;
	public double kingaku;
	public String validUpto;

	@Override
	public String toString() {
		return "KizaiMasterDTO{" +
				"kizaicode=" + kizaicode +
				", validFrom='" + validFrom + '\'' +
				", name='" + name + '\'' +
				", yomi='" + yomi + '\'' +
				", unit='" + unit + '\'' +
				", kingaku=" + kingaku +
				", validUpto='" + validUpto + '\'' +
				'}';
	}
}