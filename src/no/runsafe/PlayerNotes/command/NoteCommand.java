package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.command.Command;

public class NoteCommand extends Command
{
	public NoteCommand(NoteManager manager, IScheduler scheduler)
	{
		super("note", "Manage player notes", "runsafe.notes", "player");

		addSubCommand(new SetCommand(manager, scheduler));
		addSubCommand(new ClearCommand(manager, scheduler));
		addSubCommand(new ListCommand(manager, scheduler));
	}
}
