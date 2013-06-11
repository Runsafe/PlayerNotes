package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.PlayerNotes.RandomGenerator;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.internal.command.AsyncCommand;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

import java.util.HashMap;

public class SetCommand extends AsyncCommand
{
	public SetCommand(NoteManager manager, IScheduler scheduler, RandomGenerator randomGenerator)
	{
		super("set", "Sets a note for the given player", "runsafe.note.set.<tier>", scheduler, "tier", "note");
		this.manager = manager;
		this.randomGenerator = randomGenerator;
		captureTail();
	}

	@Override
	public String OnAsyncExecute(ICommandExecutor executor, HashMap<String, String> params)
	{
		RunsafePlayer target = RunsafeServer.Instance.getPlayer(params.get("player"));
		String note = (params.get("note").equalsIgnoreCase("random") ? randomGenerator.getRandomTag() : params.get("note"));
		RunsafePlayer setter = null;
		if (executor instanceof RunsafePlayer)
			setter = (RunsafePlayer) executor;
		manager.setNoteForPlayer(setter, target, params.get("tier"), note);
		return String.format(("%s note set for %s."), params.get("tier"), target.getPrettyName());
	}

	private final NoteManager manager;
	private RandomGenerator randomGenerator;
}
