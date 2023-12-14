package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.command.AsyncCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.Player;
import no.runsafe.framework.api.player.IPlayer;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ListCommand extends AsyncCommand
{
	public ListCommand(NoteManager manager, IScheduler scheduler)
	{
		super(
			"list",
			"Lists all notes you can see for the given player",
			null,
			scheduler,
			new Player().require()
		);
		this.manager = manager;
	}

	@Override
	public String OnAsyncExecute(ICommandExecutor executor, IArgumentList params)
	{
		IPlayer viewer = null;
		if (executor instanceof IPlayer)
			viewer = (IPlayer) executor;
		List<String> notes = getNotes(viewer, params.getRequired("player"));
		return StringUtils.join(notes, "\n");
	}

	private List<String> getNotes(IPlayer viewer, IPlayer player)
	{
		List<String> notes = new ArrayList<>();

		notes.add(String.format("Notes for %s:", player.getPrettyName()));
		notes.addAll(manager.getNotes(player, viewer, null));
		return notes;
	}

	private final NoteManager manager;
}
