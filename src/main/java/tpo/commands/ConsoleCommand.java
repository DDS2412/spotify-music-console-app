package tpo.commands;

import tpo.ApplicationProxy;

public interface ConsoleCommand {
    Boolean execute(ApplicationProxy applicationProxy);

    String getCommandInfo();
}
