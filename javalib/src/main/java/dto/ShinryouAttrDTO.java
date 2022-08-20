package dev.myclinic.vertx.dto;

import java.util.Objects;

public class ShinryouAttrDTO {
    public int shinryouId;
    public String tekiyou;

    public static ShinryouAttrDTO copy(ShinryouAttrDTO src){
        if( src == null ){
            return null;
        }
        ShinryouAttrDTO dst = new dev.myclinic.vertx.dto.ShinryouAttrDTO();
        dst.shinryouId = src.shinryouId;
        dst.tekiyou = src.tekiyou;
        return dst;
    }

    public static dev.myclinic.vertx.dto.ShinryouAttrDTO create(int shinryouId){
        return create(shinryouId, null);
    }

    public static dev.myclinic.vertx.dto.ShinryouAttrDTO create(int shinryouId, String tekiyou){
        dev.myclinic.vertx.dto.ShinryouAttrDTO attr = new dev.myclinic.vertx.dto.ShinryouAttrDTO();
        attr.shinryouId = shinryouId;
        attr.tekiyou = tekiyou;
        return attr;
    }

    public static boolean isEmpty(dev.myclinic.vertx.dto.ShinryouAttrDTO attr){
        return attr.tekiyou == null || attr.tekiyou.isEmpty();
    }

    public static String extractTekiyou(dev.myclinic.vertx.dto.ShinryouAttrDTO attr){
        return attr == null ? null : attr.tekiyou;
    }

    public static dev.myclinic.vertx.dto.ShinryouAttrDTO setTekiyou(int shinryouId, dev.myclinic.vertx.dto.ShinryouAttrDTO attr, String tekiyou){
        if( attr == null ){
            return create(shinryouId, tekiyou);
        } else {
            attr.tekiyou = tekiyou;
            if( isEmpty(attr) ){
                attr = null;
            }
            return attr;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        dev.myclinic.vertx.dto.ShinryouAttrDTO that = (dev.myclinic.vertx.dto.ShinryouAttrDTO) o;
        return shinryouId == that.shinryouId &&
                Objects.equals(tekiyou, that.tekiyou);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shinryouId, tekiyou);
    }

    @Override
    public String toString() {
        return "ShinryouAttrDTO{" +
                "shinryouId=" + shinryouId +
                ", tekiyou='" + tekiyou + '\'' +
                '}';
    }
}
