package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.command.Command;
import no.runsafe.framework.api.command.argument.PlayerArgument;

public class NoteCommand extends Command
{
	public NoteCommand(NoteManager manager, IScheduler scheduler, IServer server)
	{
		super("note", "Manage player notes", "runsafe.notes", new PlayerArgument());

		addSubCommand(new SetCommand(manager, scheduler, server));
		addSubCommand(new ClearCommand(manager, scheduler, server));
		addSubCommand(new ListCommand(manager, scheduler, server));
	}
}
