package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.command.AsyncCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.framework.text.ChatColour;

import java.util.List;
import java.util.Map;

public class SetCommand extends AsyncCommand
{
	public SetCommand(NoteManager manager, IScheduler scheduler, IServer server)
	{
		super(
			"set", "Sets a note for the given player", "runsafe.note.set.<tier>", scheduler,
			new RequiredArgument("tier"), new TrailingArgument("note")
		);
		this.manager = manager;
		this.server = server;
	}

	@Override
	public List<String> getParameterOptions(String parameter)
	{
		if (parameter.equals("tier"))
			return manager.getTiers();
		return null;
	}

	@Override
	public String OnAsyncExecute(ICommandExecutor executor, Map<String, String> params)
	{
		IPlayer target = server.getPlayer(params.get("player"));
		String note = params.get("note");

		if (!executor.hasPermission("runsafe.note.colour"))
			note = ChatColour.Strip(note);

		RunsafePlayer setter = null;
		if (executor instanceof RunsafePlayer)
			setter = (RunsafePlayer) executor;

		manager.setNoteForPlayer(setter, target, params.get("tier"), note);
		return String.format(("%s note set for %s."), params.get("tier"), target.getPrettyName());
	}

	private final NoteManager manager;
	private final IServer server;
}
