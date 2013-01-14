package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.command.AsyncCommand;
import no.runsafe.framework.command.ExecutableCommand;
import no.runsafe.framework.server.ICommandExecutor;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;

import java.util.HashMap;

public class ClearCommand extends AsyncCommand
{
	public ClearCommand(NoteManager manager, IScheduler scheduler)
	{
		super("clear", "Removes a note from a player, or all if * is used.", "runsafe.note.clear.<tier>", scheduler, "tier");
		this.manager = manager;
	}

	@Override
	public String OnAsyncExecute(ICommandExecutor executor, HashMap<String, String> params, String[] strings)
	{
		RunsafePlayer target = RunsafeServer.Instance.getPlayer(params.get("player"));
		if (params.get("tier").equals("*"))
		{
			manager.clearAllNotesForPlayer(target);
			return String.format("All notes for %s cleared.", target.getPrettyName());
		}
		else
		{
			manager.clearNoteForPlayer(target, params.get("tier"));
			return String.format("%s note for %s cleared.", params.get("tier"), target.getPrettyName());
		}
	}

	private String permissionTier;
	private final NoteManager manager;
}
