package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.server.player.RunsafePlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.util.Arrays;

public class SetCommand extends NoteSubCommand
{
	public SetCommand(NoteManager manager)
	{
		super("set");
		this.manager = manager;
	}

	@Override
	public boolean Execute(RunsafePlayer player, String[] args)
	{
		if (args == null || args.length < 2)
			return false;

		String tier = args[0];

		if(player.hasPermission("playernotes.set." + tier))
			Execute(args);
		else
			player.sendMessage(ChatColor.RED + "Access denied to that command.");

		return true;
	}

	@Override
	public boolean Execute(String[] args)
	{
		if (args == null || args.length < 2)
			return false;

		String tier = args[0];
		String note = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
		manager.setNoteForPlayer(getPlayer(), tier, note);
		return true;
	}

	private NoteManager manager;
}
