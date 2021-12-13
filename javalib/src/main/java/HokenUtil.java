package dev.myclinic.java;

public class HokenUtil {

    public String formatShahokokuhoHokenshaBangou(int bangou) {
        if (bangou <= 9999) {
            return String.format("%d", bangou);
        } else if (bangou <= 999999) {
            return String.format("%06d", bangou);
        } else {
            return String.format("%08d", bangou);
        }
    }

    public String formatKouhiJukyuushaBangou(int bangou){
        return String.format("%07d", bangou);
    }

}