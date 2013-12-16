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
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		if (notes != null && notes.size() > 0)
		{
			for (final Note note : notes)
				scheduler.startSyncTask(new Notifier(note, player), 1);
		}
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
		List<String> result = new ArrayList<String>();
		List<Note> notes = repository.get(player);
		if (notes != null && notes.size() > 0)
			for (Note note : notes)
				if (tierFilter == null || note.getTier().startsWith(tierFilter))
					if (viewer == null || note.hasPermission(viewer))
						result.add(convert(note));
		return result;
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

	private String formatMessageForGame(String tier, IPlayer player, Note message)
	{
		return String.format(gameFormat, tier, player.getPrettyName(), convert(message));
	}

	private String formatMessageForConsole(String tier, IPlayer player, Note message)
	{
		return String.format(consoleFormat, tier, player.getPrettyName(), convert(message));
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

	private String convert(DateTime date)
	{
		if (date == null)
			return "-";
		return date.toString(dateFormat);
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
