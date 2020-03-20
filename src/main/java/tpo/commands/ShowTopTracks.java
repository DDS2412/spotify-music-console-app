package tpo.commands;

import tpo.ApplicationProxy;

public class ShowTopTracks implements ConsoleCommand {
    private static final String COMMAND_INFO = "Рекомендуемые треки";

    @Override
    public void execute(ApplicationProxy applicationProxy) {
        applicationProxy.show("Список треков для вас");
        applicationProxy.showTracks(applicationProxy.getPersonalTracks());
    }

    @Override
    public String getCommandInfo() {
        return COMMAND_INFO;
    }
}
