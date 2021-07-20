package util;

public class RegexUtil {
    private RegexUtil(){}
    public  static final String REGEXP_MAIL = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public static final String REGEXP_UUID = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})";

}
