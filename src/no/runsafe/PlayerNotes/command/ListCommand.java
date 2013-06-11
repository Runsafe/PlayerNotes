package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.internal.command.AsyncCommand;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

import java.util.HashMap;

public class ListCommand extends AsyncCommand
{
	public ListCommand(NoteManager manager, IScheduler scheduler)
	{
		super("list", "Lists all notes you can see for the given player", null, scheduler);
		this.manager = manager;
	}

	@Override
	public String OnAsyncExecute(ICommandExecutor executor, HashMap<String, String> params)
	{
		RunsafePlayer viewer = null;
		if (executor instanceof RunsafePlayer)
			viewer = (RunsafePlayer) executor;
		return manager.getNotes(RunsafeServer.Instance.getPlayer(params.get("player")), viewer);
	}

	private final NoteManager manager;
}
