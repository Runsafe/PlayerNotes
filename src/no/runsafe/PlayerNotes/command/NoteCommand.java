package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.command.ICommand;
import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.output.Console;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class NoteCommand extends RunsafeCommand
{
	public NoteCommand(NoteManager manager)
	{
		super("note", null, "player");

		addSubCommand(new SetCommand(manager));
		addSubCommand(new ClearCommand(manager));
	}
}
