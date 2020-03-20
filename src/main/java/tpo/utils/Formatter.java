package tpo.utils;

public class Formatter {
    public static String getFormattedStringWithTrackCount(Integer totalTracks) {
        String formattedString = " треков";

        if (totalTracks == 1) {
            formattedString = " трек";
        } else if (totalTracks >= 2 && totalTracks <= 4){
            formattedString = " трека";
        }

        return totalTracks + formattedString;
    }
}
