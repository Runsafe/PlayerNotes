package no.runsafe.PlayerNotes;

import com.google.common.collect.Lists;
import no.runsafe.PlayerNotes.database.Note;
import no.runsafe.PlayerNotes.database.NoteRepository;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.player.IPlayer;
import org.apache.commons.lang.time.DateFormatUtils;

import java.time.Instant;
import java.util.*;

public class NoteManager implements IConfigurationChanged
{
	public NoteManager(NoteRepository repository, IServer server, IConsole output, IScheduler scheduler)
	{
		this.repository = repository;
		this.server = server;
		this.output = output;
		this.scheduler = scheduler;
	}

	public void setNoteForPlayer(IPlayer executor, IPlayer player, String tier, String note)
	{
		String setter = (executor == null ? "Server" : executor.getName());
		repository.persist(player, tier, note, setter);
	}

	public void clearNoteForPlayer(IPlayer player, String tier)
	{
		repository.persist(player, tier, null, null);
	}

	public void clearAllNotesForPlayer(IPlayer player)
	{
		repository.clear(player);
	}

	public void sendNotices(final IPlayer player)
	{
		List<Note> notes = repository.get(player);
		if (notes == null || notes.isEmpty())
			return;

		for (final Note note : notes)
			scheduler.startSyncTask(new Notifier(note, player), 1);
	}

	public List<String> getTiers()
	{
		return Lists.newArrayList(tierFormat.keySet());
	}

	class Notifier implements Runnable
	{
		private final Note note;
		private final IPlayer player;

		Notifier(Note note, IPlayer player)
		{
			this.note = note;
			this.player = player;
		}

		@Override
		public void run()
		{
			String message = formatMessageForGame(note.getTier(), player, note);
			output.logInformation(formatMessageForConsole(note.getTier(), player, note));
			for (IPlayer target : server.getOnlinePlayers())
				if (target.hasPermission(note.getPermission()))
					target.sendColouredMessage(message);
		}
	}

	public List<String> getNotes(IPlayer player, IPlayer viewer, String tierFilter)
	{
		if (tierFilter == null)
			return convertNotes(repository.get(player), viewer);

		Note note = repository.get(player, tierFilter);
		if (note == null || (viewer != null && !note.isVisible(viewer)))
			return new ArrayList<>();

		return Collections.singletonList(convert(note));
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		gameFormat = configuration.getConfigValueAsString("format.broadcast.game").replace("\\n", "\n");
		consoleFormat = configuration.getConfigValueAsString("format.broadcast.console").replace("\\n", "\n");
		noteFormat = configuration.getConfigValueAsString("format.note").replace("\\n", "\n");
		dateFormat = configuration.getConfigValueAsString("format.date").replace("\\n", "\n");
		tierFormat = configuration.getConfigValuesAsMap("format.tier");
		if (tierFormat == null)
			return;

		tierFormat.replaceAll((t, v) -> tierFormat.get(t).replace("\\n", "\n"));
	}

	private String formatMessageForGame(String tier, IPlayer player, Note message)
	{
		return String.format(gameFormat, tier, player.getPrettyName(), convert(message));
	}

	private String formatMessageForConsole(String tier, IPlayer player, Note message)
	{
		return String.format(consoleFormat, tier, player.getPrettyName(), convert(message));
	}

	private List<String> convertNotes(List<Note> notes, IPlayer viewer)
	{
		List<String> result = new ArrayList<>();
		if (notes == null || notes.isEmpty())
			return result;

		if (viewer == null)
		{
			for (Note note : notes)
				result.add(convert(note));

			return result;
		}

		for (Note note : notes)
			if (note.isVisible(viewer))
				result.add(convert(note));

		return result;
	}

	private String convert(Note note)
	{
		if (note == null)
			return "-";
		return String.format(
			tierFormat.containsKey(note.getTier()) ? tierFormat.get(note.getTier()) : noteFormat,
			note.getNote(), note.getSetter(), convert(note.getTimestamp())
		);
	}

	private String convert(Instant date)
	{
		if (date == null)
			return "-";
		return DateFormatUtils.format(date.toEpochMilli(), dateFormat);
	}

	private final NoteRepository repository;
	private final IServer server;
	private final IConsole output;
	private final IScheduler scheduler;
	private String gameFormat;
	private String consoleFormat;
	private String noteFormat;
	private String dateFormat;
	private Map<String, String> tierFormat;
}
