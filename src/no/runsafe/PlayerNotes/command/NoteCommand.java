package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.command.Command;
import no.runsafe.framework.timer.IScheduler;

public class NoteCommand extends Command // RunsafeCommand
{
	public NoteCommand(NoteManager manager, IScheduler scheduler)
	{
		super("note", "Manage player notes", "runsafe.notes", "player");

		addSubCommand(new SetCommand(manager));
		addSubCommand(new ClearCommand(manager));
		addSubCommand(new ListCommand(manager));
	}
}
