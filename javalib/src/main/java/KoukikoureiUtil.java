package dev.myclinic.java;

public class KoukikoureiUtil {

	public String rep(int futanWari){
		return "後期高齢" + futanWari + "割";
	}

	public String hokenshaBangouString(int hokenshaBangou){
		return String.format("%08d", hokenshaBangou);
	}

	public String hihokenshaBangouString(int hihokenshaBangou){
		return String.format("%08d", hihokenshaBangou);
	}
}
