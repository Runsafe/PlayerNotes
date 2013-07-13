package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.command.AsyncCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
		return null;
	}

	public String OnAsyncExecute(ICommandExecutor executor, HashMap<String, String> params, String[] args)
	{
		RunsafePlayer viewer = null;
		if (executor instanceof RunsafePlayer)
			viewer = (RunsafePlayer) executor;
		if (params.get("player").equals("*"))
		{
			List<String> notes = new ArrayList<String>();
			for (RunsafePlayer player : RunsafeServer.Instance.getOnlinePlayers())
			{
				if (viewer != null && viewer.shouldNotSee(player))
					continue;

				notes.add(manager.getNotes(player, viewer, args == null || args.length == 0 ? null : args[0]));
			}
			return Strings.join(notes, "\n");
		}
		return manager.getNotes(RunsafeServer.Instance.getPlayer(params.get("player")), viewer, null);
	}

	private final NoteManager manager;
}
