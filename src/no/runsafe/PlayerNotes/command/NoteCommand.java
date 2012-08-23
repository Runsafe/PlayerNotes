package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.timer.IScheduler;

public class NoteCommand extends RunsafeCommand
{
	public NoteCommand(NoteManager manager, IScheduler scheduler)
	{
		super("note", "player");

		addSubCommand(new SetCommand(manager, scheduler));
		addSubCommand(new ClearCommand(manager, scheduler));
		addSubCommand(new ListCommand(manager, scheduler));
	}
}
