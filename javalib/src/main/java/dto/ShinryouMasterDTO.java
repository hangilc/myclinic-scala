package dev.myclinic.vertx.dto;

public class ShinryouMasterDTO {
	public int shinryoucode;
	public String validFrom;
	public String name;
	public int tensuu;
	public char tensuuShikibetsu;
	public String shuukeisaki;
	public String houkatsukensa;
	public char oushinkubun; // not used (2018-10-31)
	public String kensaGroup;
	public String validUpto;

	@Override
	public String toString(){
		return "ShinryouMasterDTO[" +
			"shinryoucode=" + shinryoucode + "," +
			"validFrom=" + validFrom + "," +
			"name=" + name + "," +
			"tensuu=" + tensuu + "," +
			"tensuuShikibetsu=" + tensuuShikibetsu + "," +
			"shuukeisaki=" + shuukeisaki + "," +
			"houkatsukensa=" + houkatsukensa + "," +
			"oushinkubun=" + oushinkubun + "," +
			"kensaGroup=" + kensaGroup + "," +
			"validUpto=" + validUpto +
		"]";
	}
}