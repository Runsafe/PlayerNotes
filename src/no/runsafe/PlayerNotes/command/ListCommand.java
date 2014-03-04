package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.command.AsyncCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.OptionalArgument;
import no.runsafe.framework.api.player.IPlayer;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ListCommand extends AsyncCommand
{
	public ListCommand(NoteManager manager, IScheduler scheduler, IServer server)
	{
		super(
			"list", "Lists all notes you can see for the given player", null, scheduler,
			new OptionalArgument("filter")
		);
		this.manager = manager;
		this.server = server;
	}

	@Override
	public String OnAsyncExecute(ICommandExecutor executor, IArgumentList params)
	{
		IPlayer viewer = null;
		if (executor instanceof IPlayer)
			viewer = (IPlayer) executor;
		List<String> notes = new ArrayList<String>();
		if (params.get("player").equals("*"))
			for (IPlayer player : server.getOnlinePlayers())
				notes.addAll(getNotes(viewer, player, params.get("filter"), false));
		else
			notes = getNotes(viewer, (IPlayer) params.getValue("player"), null, true);
		return StringUtils.join(notes, "\n");
	}

	private List<String> getNotes(IPlayer viewer, IPlayer player, String tier, boolean offline)
	{
		List<String> notes = new ArrayList<String>();
		if (player == null)
		{
			notes.add("Player not found");
			return notes;
		}
		if (!offline && viewer.shouldNotSee(player))
			return notes;
		notes.add(String.format("Notes for %s:", player.getPrettyName()));
		notes.addAll(manager.getNotes(player, viewer, tier));
		return notes;
	}

	private final NoteManager manager;
	private final IServer server;

}
