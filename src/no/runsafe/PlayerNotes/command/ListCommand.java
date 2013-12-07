package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.command.AsyncCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.OptionalArgument;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListCommand extends AsyncCommand
{
	public ListCommand(NoteManager manager, IScheduler scheduler)
	{
		super(
			"list", "Lists all notes you can see for the given player", null, scheduler,
			new OptionalArgument("filter")
		);
		this.manager = manager;
	}

	@Override
	public String OnAsyncExecute(ICommandExecutor executor, Map<String, String> params)
	{
		RunsafePlayer viewer = null;
		if (executor instanceof RunsafePlayer)
			viewer = (RunsafePlayer) executor;
		List<String> notes = new ArrayList<String>();
		if (params.get("player").equals("*"))
			for (IPlayer player : RunsafeServer.Instance.getOnlinePlayers())
				notes.addAll(getNotes(viewer, player, params.containsKey("filter") ? params.get("filter") : null, false));
		else
			notes = getNotes(viewer, RunsafeServer.Instance.getPlayer(params.get("player")), null, true);
		return Strings.join(notes, "\n");
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
}
