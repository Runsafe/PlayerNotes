package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.command.AsyncCommand;
import no.runsafe.framework.server.ICommandExecutor;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;

import java.util.HashMap;

public class ListCommand extends AsyncCommand
{
	public ListCommand(NoteManager manager, IScheduler scheduler)
	{
		super("list", "Lists all notes you can see for the given player", null, scheduler);
		this.manager = manager;
	}

	@Override
	public String OnAsyncExecute(ICommandExecutor executor, HashMap<String, String> params, String[] args)
	{
		RunsafePlayer viewer = null;
		if (executor instanceof RunsafePlayer)
			viewer = (RunsafePlayer) executor;
		return manager.getNotes(RunsafeServer.Instance.getPlayer(params.get("player")), viewer);
	}

	private final NoteManager manager;
}
