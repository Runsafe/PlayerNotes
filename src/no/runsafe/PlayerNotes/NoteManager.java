package no.runsafe.PlayerNotes;

import no.runsafe.PlayerNotes.database.Note;
import no.runsafe.PlayerNotes.database.NoteRepository;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.framework.text.ChatColour;
import no.runsafe.framework.text.ConsoleColors;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

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
		List<Note> notes = repository.get(player);
		if (notes != null && notes.size() > 0)
		{
			for (Note note : notes)
			{
				String tier = note.getTier();
				String message = formatMessageForGame(tier, player, note);
				output.write(formatMessageForConsole(tier, player, note));
				String permission = note.getPermission();
				for (RunsafePlayer target : server.getOnlinePlayers())
					if (target.hasPermission(permission))
						target.sendMessage(message);
			}
		}
	}

	public String getNotes(RunsafePlayer player, RunsafePlayer viewer)
	{
		StringBuilder result = new StringBuilder();
		List<Note> notes = repository.get(player);
		if (notes != null && notes.size() > 0)
		{
			for (Note note : notes)
			{
				String tier = note.getTier();
				if (viewer == null)
				{
					result.append(formatMessageForConsole(tier, player, note));
					result.append("\n");
				}
				else if (note.hasPermission(viewer))
				{
					result.append(formatMessageForGame(tier, player, note));
					result.append("\n");
				}
			}
		}
		return result.toString();
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		gameFormat = configuration.getConfigValueAsString("format.broadcast.game").replace("\\n", "\n");
		consoleFormat = configuration.getConfigValueAsString("format.broadcast.console").replace("\\n", "\n");
		noteFormat = configuration.getConfigValueAsString("format.note").replace("\\n", "\n");
		dateFormat = configuration.getConfigValueAsString("format.date").replace("\\n", "\n");
		tierFormat = configuration.getConfigValuesAsMap("format.tier");
		if (tierFormat != null)
			for (String tier : tierFormat.keySet())
				tierFormat.put(tier, tierFormat.get(tier).replace("\\n", "\n"));
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
			tierFormat.containsKey(note.getTier()) ? tierFormat.get(note.getTier()) : noteFormat,
			note.getNote(), convert(note.getSetter()), convert(note.getTimestamp())
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
	private Map<String, String> tierFormat;
}
