package tpo.commands;

import tpo.ApplicationProxy;

public class ShowTopArtists implements ConsoleCommand {
    private static final String COMMAND_INFO = "Рекомендуемые исполнители";

    @Override
    public void execute(ApplicationProxy applicationProxy) {
        applicationProxy.show("Список исполнителей для вас");
        applicationProxy.showArtists(applicationProxy.getPersonalArtists());
    }

    @Override
    public String getCommandInfo() {
        return COMMAND_INFO;
    }
}
