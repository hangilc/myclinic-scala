package dev.myclinic.java;

public class RcptCalc {
	public int touyakuKingakuToTen(double kingaku){
		if( kingaku <= 15.0 ){
			return 1;
		} else {
			return (int)Math.ceil((kingaku - 15)/10.0) + 1;
		}
	}

	public int shochiKingakuToTen(double kingaku){
		if( kingaku <= 15 )
			return 0;
		else
			return (int)Math.ceil((kingaku - 15)/10) + 1;
	}

	public int kizaiKingakuToTen(double kingaku){
		return (int)Math.round(kingaku/10.0);
	}

	// public int calcRcptAge(int bdYear, int bdMonth, int bdDay, int atYear, int atMonth){
	//     int age;
	// 	age = atYear - bdYear;
	// 	if( atMonth < bdMonth ){
	// 		age -= 1;
	// 	} else if( atMonth == bdMonth ){
	// 		if( bdDay != 1 ){
	// 			age -= 1;
	// 		}
	// 	}
	// 	return age;
	// }

}
