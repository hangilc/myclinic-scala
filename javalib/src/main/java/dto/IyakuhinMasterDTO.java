package dev.myclinic.vertx.dto;

public class IyakuhinMasterDTO {
	public int iyakuhincode;
	public String validFrom;
	public String name;
	public String yomi;
	public String unit;
	public double yakka;
	public char madoku;
	public char kouhatsu;
	public char zaikei;
	public String validUpto;

	@Override
	public String toString(){
		return "IyakuhinMasterDTO[" + 
			"iyakuhincode=" + iyakuhincode + "," +
			"validFrom=" + validFrom + "," +
			"name=" + name + "," +
			"yomi=" + yomi + "," +
			"unit=" + unit + "," +
			"yakka=" + yakka + "," +
			"madoku=" + madoku + "," +
			"kouhatsu=" + kouhatsu + "," +
			"zaikei=" + zaikei + "," +
			"validUpto=" + validUpto +
		"]";
	}
}