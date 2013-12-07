package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.command.AsyncCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.RunsafeServer;

import java.util.Map;

public class ClearCommand extends AsyncCommand
{
	public ClearCommand(NoteManager manager, IScheduler scheduler)
	{
		super(
			"clear", "Removes a note from a player, or all if * is used.", "runsafe.note.clear.<tier>", scheduler,
			new RequiredArgument("tier")
		);
		this.manager = manager;
	}

	@Override
	public String OnAsyncExecute(ICommandExecutor executor, Map<String, String> params)
	{
		IPlayer target = RunsafeServer.Instance.getPlayer(params.get("player"));
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
