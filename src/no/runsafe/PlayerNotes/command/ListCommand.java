package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.command.ExecutableCommand;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;

import java.util.HashMap;

public class ListCommand extends ExecutableCommand // RunsafeAsyncCommand
{
	public ListCommand(NoteManager manager)//, IScheduler scheduler)
	{
		super("list", "Lists all notes you can see for the given player", null);
		this.manager = manager;
	}

	@Override
	public String OnExecute(RunsafePlayer executor, HashMap<String, String> params, String[] args)
	{
		return manager.getNotes(RunsafeServer.Instance.getPlayer(params.get("player")), executor);
	}

	private final NoteManager manager;
}
