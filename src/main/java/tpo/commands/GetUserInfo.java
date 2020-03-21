package tpo.commands;

import tpo.ApplicationProxy;

public class GetUserInfo implements ConsoleCommand {
    private static final String COMMAND_INFO = "Информация о пользователе";

    @Override
    public Boolean execute(ApplicationProxy applicationProxy) {
        applicationProxy.displayUserInfo();

        return false;
    }

    @Override
    public String getCommandInfo() {
        return COMMAND_INFO;
    }
}
