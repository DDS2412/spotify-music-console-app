package tpo.utils;

public class TypeConverter {
    public static Integer tryParseInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException ex){
            return null;
        }
    }
}
