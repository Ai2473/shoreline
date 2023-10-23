package com.caspian.client.impl.command;

import com.caspian.client.api.command.arg.Argument;
import com.caspian.client.api.command.arg.OptionalArgument;
import com.caspian.client.api.command.arg.arguments.CommandArgument;
import com.caspian.client.api.command.Command;
import com.caspian.client.init.Managers;
import com.caspian.client.util.chat.ChatUtil;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class HelpCommand extends Command
{
    //
    @OptionalArgument
    Argument<Command> commandArgument = new CommandArgument("Command", "The " +
            "specified command to display info");

    /**
     *
     */
    public HelpCommand()
    {
        super("Help", "Displays command functionality");
    }

    /**
     *
     */
    @Override
    public void onCommandInput()
    {
        final Command command = commandArgument.parse();
        if (command != null)
        {
            ChatUtil.clientSendMessage(toHelpMessage(command));
        }
        else
        {
            ChatUtil.clientSendMessageRaw("§7[§fCommands Help§7]");
            boolean sent = false;
            for (Command c : Managers.COMMAND.getCommands())
            {
                if (c instanceof ModuleCommand)
                {
                    if (!sent)
                    {
                        ChatUtil.clientSendMessageRaw(toHelpMessage(c));
                        sent = true;
                    }
                    continue;
                }
                ChatUtil.clientSendMessageRaw(toHelpMessage(c));
            }
        }
    }

    /**
     *
     * @param command
     * @return
     */
    private String toHelpMessage(Command command)
    {
        if (command instanceof ModuleCommand)
        {
            return String.format("module %s- %s", command.getUsage(),
                    command.getDescription());
        }
        return String.format("%s %s- %s", command.getName(),
                command.getUsage(), command.getDescription());
    }
}
