package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.command.ICommand;
import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class NoteCommand extends RunsafeCommand
{
	public NoteCommand(NoteManager manager)
	{
		super("note", new ArrayList<ICommand>());

		addSubCommand(new SetCommand(manager));
		addSubCommand(new ClearCommand(manager));
	}

	public NoteCommand(String name)
	{
		super(name, null);
	}

	@Override
	public boolean Execute(String[] args)
	{
		if (args == null || args.length < 2)
			return false;

		if (subCommands.containsKey(args[1]))
		{
			RunsafePlayer player = new RunsafePlayer(args[0]);
			((NoteSubCommand) subCommands.get(args[1])).setPlayer(player);
			subCommands.get(args[1]).Execute(Arrays.copyOfRange(args, 2, args.length));
			return true;
		}
		return false;
	}

	public void setPlayer(RunsafePlayer player)
	{
		this.player = player;
	}

	protected RunsafePlayer player;
}
