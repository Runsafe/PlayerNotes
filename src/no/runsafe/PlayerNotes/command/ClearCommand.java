package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.command.AsyncCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.player.IPlayer;

public class ClearCommand extends AsyncCommand
{
	public ClearCommand(NoteManager manager, IScheduler scheduler)
	{
		super(
			"clear", "Removes a note from a player, or all if * is used.", "runsafe.note.clear.<tier>", scheduler,
			new TierArgument(manager)
		);
		this.manager = manager;
	}

	@Override
	public String OnAsyncExecute(ICommandExecutor executor, IArgumentList params)
	{
		IPlayer target = params.getValue("player");
		if (target == null)
			return null;
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

	private final NoteManager manager;
}
