package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.command.AsyncCommand;
import no.runsafe.framework.server.ICommandExecutor;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;

import java.util.HashMap;

public class SetCommand extends AsyncCommand
{
	public SetCommand(NoteManager manager, IScheduler scheduler)
	{
		super("set", "Sets a note for the given player", "runsafe.note.set.<tier>", scheduler, "tier", "note");
		this.manager = manager;
		captureTail();
	}

	@Override
	public String OnAsyncExecute(ICommandExecutor executor, HashMap<String, String> params)
	{
		RunsafePlayer target = RunsafeServer.Instance.getPlayer(params.get("player"));
		manager.setNoteForPlayer(target, params.get("tier"), params.get("note"));
		return String.format(("%s note set for %s."), params.get("tier"), target.getPrettyName());
	}

	private final NoteManager manager;
}
