package tpo.commands;

import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import tpo.ApplicationProxy;

public class ShowMusicRecommendations implements ConsoleCommand {
    private static final String COMMAND_INFO = "Отображение рекомендованной музыки";

    @Override
    public void execute(ApplicationProxy applicationProxy) {
        TrackSimplified[] musicRecommendations = applicationProxy.getMusicRecommendations();
        if(musicRecommendations.length > 0){
            applicationProxy.show("Подборка рекомендованной музыки");
            applicationProxy.showTracks(musicRecommendations);
        }
    }

    @Override
    public String getCommandInfo() {
        return COMMAND_INFO;
    }
}
