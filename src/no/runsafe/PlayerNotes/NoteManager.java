package no.runsafe.PlayerNotes;

import no.runsafe.PlayerNotes.database.NoteRepository;
import no.runsafe.PlayerNotes.database.PlayerNotes;
import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.output.ChatColour;
import no.runsafe.framework.output.ConsoleColors;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.HashMap;

public class NoteManager implements IConfigurationChanged
{
	public NoteManager(NoteRepository repository, RunsafeServer server, IOutput output)
	{
		this.repository = repository;
		this.server = server;
		this.output = output;
	}

	public void setNoteForPlayer(RunsafePlayer player, String tier, String note)
	{
		PlayerNotes notes = repository.get(player);
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
				String message = formatMessageForGame(tier, player, notes.get(tier));
				output.write(formatMessageForConsole(tier, player, notes.get(tier)));
				String permission = getPermission(tier);
				for (RunsafePlayer target : server.getOnlinePlayers())
					if (target.hasPermission(permission))
						target.sendMessage(message);
			}
		}
	}

	public String getNotes(RunsafePlayer player, RunsafePlayer viewer)
	{
		StringBuilder result = new StringBuilder();
		HashMap<String, String> notes = repository.get(player).getNotes();
		if (notes != null && notes.size() > 0)
		{
			for (String tier : notes.keySet())
			{
				if (viewer == null)
				{
					result.append(formatMessageForConsole(tier, player, notes.get(tier)));
					result.append("\n");
				}
				else if (viewer.hasPermission(getPermission(tier)))
				{
					result.append(formatMessageForGame(tier, player, notes.get(tier)));
					result.append("\n");
				}
			}
		}
		return result.toString();
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		gameFormat = configuration.getConfigValueAsString("broadcast.format.game");
		consoleFormat = configuration.getConfigValueAsString("broadcast.format.console");
	}

	private String getPermission(String tier)
	{
		return String.format("runsafe.note.show.%s", tier);
	}

	private String formatMessageForGame(String tier, RunsafePlayer player, String message)
	{
		return ChatColour.ToMinecraft(String.format(gameFormat, tier, player.getPrettyName(), message));
	}

	private String formatMessageForConsole(String tier, RunsafePlayer player, String message)
	{
		return ChatColour.ToConsole(String.format(
			consoleFormat,
			tier,
			ConsoleColors.FromMinecraft(player.getPrettyName()),
			message
		));
	}

	private final NoteRepository repository;
	private final RunsafeServer server;
	private final IOutput output;
	private String gameFormat;
	private String consoleFormat;
}
