package no.runsafe.PlayerNotes;

import no.runsafe.PlayerNotes.database.Note;
import no.runsafe.PlayerNotes.database.NoteRepository;
import no.runsafe.PlayerNotes.database.PlayerNotes;
import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.output.ChatColour;
import no.runsafe.framework.output.ConsoleColors;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import org.joda.time.DateTime;

import java.util.HashMap;

public class NoteManager implements IConfigurationChanged
{
	public NoteManager(NoteRepository repository, RunsafeServer server, IOutput output)
	{
		this.repository = repository;
		this.server = server;
		this.output = output;
	}

	public void setNoteForPlayer(RunsafePlayer executor, RunsafePlayer player, String tier, String note)
	{
		String setter = (executor == null) ? null : executor.getName();
		repository.persist(player, tier, note, setter);
	}

	public void clearNoteForPlayer(RunsafePlayer player, String tier)
	{
		repository.persist(player, tier, null, null);
	}

	public void clearAllNotesForPlayer(RunsafePlayer player)
	{
		repository.clear(player);
	}

	public void sendNotices(RunsafePlayer player)
	{
		HashMap<String, Note> notes = repository.get(player).getNotes();
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
		HashMap<String, Note> notes = repository.get(player).getNotes();
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
		noteFormat = configuration.getConfigValueAsString("broadcast.format.note");
		dateFormat = configuration.getConfigValueAsString("broadcast.format.date");
	}

	private String getPermission(String tier)
	{
		return String.format("runsafe.note.show.%s", tier);
	}

	private String formatMessageForGame(String tier, RunsafePlayer player, Note message)
	{
		return ChatColour.ToMinecraft(String.format(
			gameFormat,
			tier,
			player.getPrettyName(),
			convert(message)
		));
	}

	private String formatMessageForConsole(String tier, RunsafePlayer player, Note message)
	{
		return ChatColour.ToConsole(String.format(
			consoleFormat,
			tier,
			ConsoleColors.FromMinecraft(player.getPrettyName()),
			convert(message)
		));
	}

	private String convert(Note note)
	{
		if (note == null)
			return "-";
		return String.format(
			noteFormat,
			note.getNote(),
			convert(note.getSetter()),
			convert(note.getTimestamp())
		);
	}

	private String convert(RunsafePlayer player)
	{
		if (player == null)
			return "-";
		return player.getName();
	}

	private String convert(DateTime date)
	{
		if (date == null)
			return "-";
		return date.toString(dateFormat);
	}

	private final NoteRepository repository;
	private final RunsafeServer server;
	private final IOutput output;
	private String gameFormat;
	private String consoleFormat;
	private String noteFormat;
	private String dateFormat;
}
