package dev.myclinic.java;

public class KouhiUtil {

	public String rep(int futanshaBangou){
		if ((futanshaBangou / 1000000)  == 41)
			return "マル福";
		else if ((futanshaBangou / 1000) == 80136)
			return "マル障（１割負担）";
		else if ((futanshaBangou / 1000) == 80137)
			return "マル障（負担なし）";
		else if ((futanshaBangou / 1000) == 81136)
			return "マル親（１割負担）";
		else if ((futanshaBangou / 1000) == 81137)
			return "マル親（負担なし）";
		else if ((futanshaBangou / 1000000) == 88)
			return "マル乳";
		else
			return String.format("公費負担（%d）", futanshaBangou);
	}

	public String futanshaBangouString(int futanshaBangou){
		return String.format("%08d", futanshaBangou);
	}

	public String jukyuushaBangouString(int jukyuushaBangou){
		return String.format("%07d", jukyuushaBangou);
	}

}