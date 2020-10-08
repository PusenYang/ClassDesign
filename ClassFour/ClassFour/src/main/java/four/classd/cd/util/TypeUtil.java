package four.classd.cd.util;

import four.classd.cd.model.enums.ResourceType;

import java.util.List;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/3 16:59
 *
 */
public class TypeUtil {

    public static int getNumber(Integer tmp) {
        return tmp == null ? 0 : tmp;
    }

    public static String getDate(String str) {return str == null ? "运输中..." : str;}

    public static ResourceType getType(String tmp) {
        if (tmp.equals(ResourceType.N95.getCode())) {
            return ResourceType.N95;
        }
        else if (tmp.equals(ResourceType.PM25.getCode())) {
            return ResourceType.PM25;
        }
        else {
            return ResourceType.Ori;
        }
    }
}
