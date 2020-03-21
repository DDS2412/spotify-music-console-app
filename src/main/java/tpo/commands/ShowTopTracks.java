package tpo.commands;

import tpo.ApplicationProxy;

public class ShowTopTracks implements ConsoleCommand {
    private static final String COMMAND_INFO = "Рекомендуемые треки";

    @Override
    public Boolean execute(ApplicationProxy applicationProxy) {
        applicationProxy.show("Список треков для вас");
        applicationProxy.showTracks(applicationProxy.getPersonalTracks());

        return false;
    }

    @Override
    public String getCommandInfo() {
        return COMMAND_INFO;
    }
}
