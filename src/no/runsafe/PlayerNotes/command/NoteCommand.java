package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.command.RunsafeCommand;

public class NoteCommand extends RunsafeCommand {
	public NoteCommand(NoteManager manager) {
		super("note", null, "player");

		addSubCommand(new SetCommand(manager));
		addSubCommand(new ClearCommand(manager));
		addSubCommand(new ListCommand(manager));
	}
}
