package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.server.player.RunsafePlayer;
import org.bukkit.ChatColor;

public class ClearCommand extends NoteSubCommand
{
	public ClearCommand(NoteManager manager)
	{
		super("clear");
		this.manager = manager;
	}

	@Override
	public boolean Execute(RunsafePlayer player, String[] args)
	{
		if(args == null || args.length < 1)
			return false;

		if(player.hasPermission("playernotes.set." + args[0]))
			return Execute(args);

		player.sendMessage(ChatColor.RED + "Access denied to that command.");
		return true;
	}

	@Override
	public boolean Execute(String[] args)
	{
		if(args == null || args.length == 0)
			manager.clearAllNotesForPlayer(getPlayer());
		else
			manager.clearNoteForPlayer(getPlayer(), args[0]);

		return true;
	}

	private NoteManager manager;
}
