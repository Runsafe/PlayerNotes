package no.runsafe.PlayerNotes.command;

import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.server.player.RunsafePlayer;

public class NoteSubCommand extends RunsafeCommand
{
	public NoteSubCommand(String name)
	{
		super(name, null);
	}

	public RunsafePlayer getPlayer()
	{
		return player;
	}

	public void setPlayer(RunsafePlayer player)
	{
		this.player = player;
	}

	protected RunsafePlayer player;
}
