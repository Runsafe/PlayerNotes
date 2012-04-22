package no.runsafe.PlayerNotes;

import no.runsafe.PlayerNotes.database.NoteRepository;
import no.runsafe.PlayerNotes.database.PlayerNotes;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.player.IPlayerByPermissionProvider;
import no.runsafe.framework.server.player.RunsafePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Server;

import java.util.HashMap;
import java.util.logging.Level;

public class NoteManager
{
	public NoteManager(NoteRepository repository, IPlayerByPermissionProvider playerProvider, IOutput output)
	{
		this.repository = repository;
		this.provider = playerProvider;
		this.output = output;
	}

	public void setNoteForPlayer(RunsafePlayer player, String tier, String note)
	{
		PlayerNotes notes = repository.get(player);
		provider.watchPermission(getPermission(tier));
		notes.getNotes().put(tier, note);
		repository.persist(notes);
	}

	public void clearNoteForPlayer(RunsafePlayer player, String tier)
	{
		PlayerNotes notes = repository.get(player);
		if (notes.getNotes().containsKey(tier))
			notes.getNotes().remove(tier);
		repository.persist(notes);
	}

	public void clearAllNotesForPlayer(RunsafePlayer player)
	{
		PlayerNotes notes = repository.get(player);
		notes.getNotes().clear();
		repository.persist(notes);
	}

	public void sendNotices(RunsafePlayer player)
	{
		HashMap<String, String> notes = repository.get(player).getNotes();
		if (notes != null && notes.size() > 0)
		{
			for (String tier : notes.keySet())
			{
				String message = formatMessage(tier, player.getName(), notes.get(tier));
				output.outputColoredToConsole(message, Level.INFO);
				String permission = getPermission(tier);
				if (provider.watchPermission(permission))
					provider.checkPlayer(player);
				for (RunsafePlayer target : provider.getPlayersWithPermission(permission))
					target.sendMessage(message);
			}
		}
	}

	private String getPermission(String tier)
	{
		return String.format("playernotes.show.%s", tier);
	}

	private String formatMessage(String tier, String player, String message)
	{
		return String.format("%s[PN:%s] %s%s%s logged in: %s", ChatColor.BLUE, tier, ChatColor.LIGHT_PURPLE, player, ChatColor.YELLOW, message);
	}

	private NoteRepository repository;
	private IPlayerByPermissionProvider provider;
	private IOutput output;
}
