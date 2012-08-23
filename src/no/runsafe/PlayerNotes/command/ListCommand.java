package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.command.RunsafeAsyncCommand;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;

public class ListCommand extends RunsafeAsyncCommand
{
	public ListCommand(NoteManager manager, IScheduler scheduler)
	{
		super("list", scheduler);
		this.manager = manager;
	}

	@Override
	public String OnExecute(RunsafePlayer executor, String[] args)
	{
		return manager.getNotes(getPlayer(), executor);
	}

	private RunsafePlayer getPlayer()
	{
		return RunsafeServer.Instance.getPlayer(superCommand.getArg("player"));
	}

	private NoteManager manager;

}
