package dev.myclinic.java;

public class HokenUtil {

    // public int calcRcptAge(int bdYear, int bdMonth, int bdDay, int atYear, int atMonth) {
    //     int age;
    //     age = atYear - bdYear;
    //     if (atMonth < bdMonth) {
    //         age -= 1;
    //     } else if (atMonth == bdMonth) {
    //         if (bdDay != 1) {
    //             age -= 1;
    //         }
    //     }
    //     return age;
    // }

    // public int calcShahokokuhoFutanWariByAge(int age) {
    //     if (age < 3)
    //         return 2;
    //     else if (age >= 70)
    //         return 2;
    //     else
    //         return 3;
    // }

    // public int kouhiFutanWari(int futanshaBangou) {
    //     if (futanshaBangou / 1000000 == 41)
    //         return 1;
    //     else if ((futanshaBangou / 1000) == 80136)
    //         return 1;
    //     else if ((futanshaBangou / 1000) == 80137)
    //         return 0;
    //     else if ((futanshaBangou / 1000) == 81136)
    //         return 1;
    //     else if ((futanshaBangou / 1000) == 81137)
    //         return 0;
    //     else if ((futanshaBangou / 1000000) == 88)
    //         return 0;
    //     else {
    //         System.out.println("unknown kouhi futansha: " + futanshaBangou);
    //         return 0;
    //     }
    // }

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