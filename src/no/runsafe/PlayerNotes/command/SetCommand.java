package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.command.AsyncCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.Player;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.text.ChatColour;

public class SetCommand extends AsyncCommand
{
	public SetCommand(NoteManager manager, IScheduler scheduler)
	{
		super(
			"set",
			"Sets a note for the given player",
			"runsafe.note.set.<tier>",
			scheduler,
			new Player().require(),
			new TierArgument(manager).require(),
			new TrailingArgument("note")
		);
		this.manager = manager;
	}

	@Override
	public String OnAsyncExecute(ICommandExecutor executor, IArgumentList params)
	{
		IPlayer target = params.getRequired("player");
		String note = params.getValue("note");

		if (!executor.hasPermission("runsafe.note.colour"))
			note = ChatColour.Strip(note);

		IPlayer setter = null;
		if (executor instanceof IPlayer)
			setter = (IPlayer) executor;

		String tier = params.getRequired("tier");

		if (tier.equals("*"))
			return "&cThis sub-command does not support wildcards.";

		manager.setNoteForPlayer(setter, target, tier, note);
		return String.format(("%s note set for %s."), tier, target.getPrettyName());
	}

	private final NoteManager manager;
}
