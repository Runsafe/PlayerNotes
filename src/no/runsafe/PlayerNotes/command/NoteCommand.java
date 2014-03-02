package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.command.Command;
import no.runsafe.framework.api.command.argument.Player;

public class NoteCommand extends Command
{
	public NoteCommand(NoteManager manager, IScheduler scheduler, IServer server)
	{
		super("note", "Manage player notes", "runsafe.notes", new Player.Any().require());

		addSubCommand(new SetCommand(manager, scheduler));
		addSubCommand(new ClearCommand(manager, scheduler));
		addSubCommand(new ListCommand(manager, scheduler, server));
	}
}
