package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.output.Console;
import no.runsafe.framework.server.player.RunsafePlayer;
import org.bukkit.ChatColor;

public class ClearCommand extends RunsafeCommand
{
	public ClearCommand(NoteManager manager)
	{
		super("clear", null);
		this.manager = manager;
	}

	@Override
	public String requiredPermission()
	{
		return "playernotes.set." + permissionTier;
	}

	@Override
	public boolean CanExecute(RunsafePlayer executor, String[] args)
	{
		if(args == null || args.length == 0)
			permissionTier = "*";
		else
			permissionTier = args[0];

		return executor.hasPermission("playernotes.set." + permissionTier);
	}

	@Override
	// Override due to tier being optional
	public String getCommandParams()
	{
		return superCommand.getCommandParams() + " clear [tier]";
	}

	@Override
	public String OnExecute(String[] args)
	{
		if(args == null || args.length == 0)
		{
			manager.clearAllNotesForPlayer(getPlayer());
			return String.format("All notes for %s cleared.", getPlayer().getName());
		}
		else
		{
			manager.clearNoteForPlayer(getPlayer(), args[0]);
			return String.format("%s note for %s cleared.", args[0], getPlayer().getName());
		}
	}

	private RunsafePlayer getPlayer()
	{
		return new RunsafePlayer(superCommand.getArg("player"));
	}

	private String permissionTier;
	private NoteManager manager;
}
