package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.PlayerNotes.RandomGenerator;
import no.runsafe.framework.command.Command;
import no.runsafe.framework.timer.IScheduler;

public class NoteCommand extends Command
{
	public NoteCommand(NoteManager manager, IScheduler scheduler, RandomGenerator randomGenerator)
	{
		super("note", "Manage player notes", "runsafe.notes", "player");

		addSubCommand(new SetCommand(manager, scheduler, randomGenerator));
		addSubCommand(new ClearCommand(manager, scheduler));
		addSubCommand(new ListCommand(manager, scheduler));
	}
}
