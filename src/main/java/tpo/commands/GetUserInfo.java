package tpo.commands;

import tpo.ApplicationProxy;

public class GetUserInfo implements ConsoleCommand {
    private static final String COMMAND_INFO = "Информация о пользователе";

    @Override
    public void execute(ApplicationProxy applicationProxy) {
        applicationProxy.displayUserInfo();
    }

    @Override
    public String getCommandInfo() {
        return COMMAND_INFO;
    }
}
