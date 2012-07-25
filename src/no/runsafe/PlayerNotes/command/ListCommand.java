package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.PlayerNotes.database.NoteRepository;
import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.HashMap;
import java.util.logging.Level;

public class ListCommand extends RunsafeCommand {
	public ListCommand(NoteManager manager) {
		super("list", null);
		this.manager = manager;
	}

	@Override
	public String OnExecute(RunsafePlayer executor, String[] args) {
		return manager.getNotes(getPlayer(), executor);
	}

	private RunsafePlayer getPlayer() {
		return RunsafeServer.Instance.getPlayer(superCommand.getArg("player"));
	}

	private NoteManager manager;

}
