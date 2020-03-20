package tpo.utils;

import com.wrapper.spotify.model_objects.miscellaneous.PlaylistTracksInformation;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;

public class TypeConverter {
    public static Integer tryParseInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException ex){
            return null;
        }
    }

    public static PlaylistSimplified convertPlaylistToSimplified(Playlist playlist){
        return new PlaylistSimplified
                .Builder()
                .setCollaborative(playlist.getIsCollaborative())
                .setExternalUrls(playlist.getExternalUrls())
                .setHref(playlist.getHref())
                .setId(playlist.getId())
                .setImages(playlist.getImages())
                .setName(playlist.getName())
                .setOwner(playlist.getOwner())
                .setPublicAccess(playlist.getIsPublicAccess())
                .setSnapshotId(playlist.getSnapshotId())
                .setTracks(new PlaylistTracksInformation.Builder().setHref(playlist.getTracks().getHref()).setTotal(playlist.getTracks().getTotal()).build())
                .setType(playlist.getType())
                .setUri(playlist.getUri()).build();
    }

    public static TrackSimplified convertTrackToSimplified(Track track){
        return new TrackSimplified
                .Builder()
                .setArtists(track.getArtists())
                .setAvailableMarkets(track.getAvailableMarkets())
                .setDiscNumber(track.getDiscNumber())
                .setDurationMs(track.getDurationMs())
                .setExplicit(track.getIsExplicit())
                .setExternalUrls(track.getExternalUrls())
                .setHref(track.getHref())
                .setId(track.getId())
                .setIsPlayable(track.getIsPlayable())
                .setLinkedFrom(track.getLinkedFrom())
                .setName(track.getName())
                .setPreviewUrl(track.getPreviewUrl())
                .setTrackNumber(track.getTrackNumber())
                .setType(track.getType())
                .setUri(track.getUri())
                .build();
    }
}
