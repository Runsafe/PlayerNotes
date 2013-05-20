package no.runsafe.PlayerNotes.database;

import no.runsafe.framework.database.IDatabase;
import no.runsafe.framework.database.Repository;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class NoteRepository extends Repository
{
	public NoteRepository(IDatabase database, IOutput output)
	{
		this.database = database;
		this.output = output;
	}

	public PlayerNotes get(RunsafePlayer player)
	{
		try
		{
			PreparedStatement select = database.prepare("SELECT * FROM playerNotes WHERE playerName=?");
			select.setString(1, player.getName());
			PlayerNotes notes = new PlayerNotes();
			notes.setPlayer(player);
			HashMap<String, Note> noteMap = new HashMap<String, Note>();
			ResultSet data = select.executeQuery();
			while (data.next())
			{
				Note note = new Note();
				note.setSetter(RunsafeServer.Instance.getPlayerExact(data.getString("set_by")));
				note.setTimestamp(convert(data.getTimestamp("set_at")));
				note.setNote(data.getString("note"));
				noteMap.put(data.getString("tier"), note);
			}
			notes.setNotes(noteMap);
			return notes;
		}
		catch (SQLException e)
		{
			output.outputToConsole(e.getMessage(), Level.SEVERE);
		}
		return null;
	}

	public void persist(PlayerNotes playerNotes)
	{
		PlayerNotes notes = get(playerNotes.getPlayer());
		try
		{
			PreparedStatement update = database.prepare("UPDATE playerNotes SET note=?, set_at=NOW(), set_by=? WHERE playerName=? AND tier=?");
			update.setString(3, playerNotes.getPlayer().getName());
			PreparedStatement insert = database.prepare("INSERT INTO playerNotes (playerName, tier, note, set_by, set_at) VALUES (?, ?, ?, ?, NOW())");
			insert.setString(1, playerNotes.getPlayer().getName());
			PreparedStatement delete = database.prepare("DELETE FROM playerNotes WHERE playerName=? AND tier=?");
			delete.setString(1, playerNotes.getPlayer().getName());

			for (String tier : playerNotes.getNotes().keySet())
				if (notes.getNotes().containsKey(tier))
				{
					if (!playerNotes.getNotes().get(tier).equals(notes.getNotes().get(tier)))
					{
						update.setString(4, tier);
						String setter = null;
						if (playerNotes.getNotes().get(tier).getSetter() != null)
							setter = playerNotes.getNotes().get(tier).getSetter().getName();
						update.setString(2, setter);
						update.setString(1, playerNotes.getNotes().get(tier).getNote());
						update.executeUpdate();
					}
				}
				else
				{
					String setter = null;
					if (playerNotes.getNotes().get(tier).getSetter() != null)
						setter = playerNotes.getNotes().get(tier).getSetter().getName();
					insert.setString(2, tier);
					insert.setString(3, playerNotes.getNotes().get(tier).getNote());
					insert.setString(4, setter);
					insert.executeUpdate();
				}

			for (String tier : notes.getNotes().keySet())
				if (!playerNotes.getNotes().containsKey(tier))
				{
					delete.setString(2, tier);
					delete.executeUpdate();
				}
		}
		catch (SQLException e)
		{
			output.outputToConsole(e.getMessage(), Level.SEVERE);
		}
	}

	public void delete(PlayerNotes playerNotes)
	{
		try
		{
			PreparedStatement delete = database.prepare("DELETE FROM playerNotes WHERE playerName=?");
			delete.setString(1, playerNotes.getPlayer().getName());
			delete.executeUpdate();
		}
		catch (SQLException e)
		{
			output.outputToConsole(e.getMessage(), Level.SEVERE);
		}
	}

	@Override
	public String getTableName()
	{
		return "playerNotes";
	}

	@Override
	public HashMap<Integer, List<String>> getSchemaUpdateQueries()
	{
		HashMap<Integer, List<String>> queries = new HashMap<Integer, List<String>>();
		List<String> sql = new ArrayList<String>();
		sql.add(
			"CREATE TABLE IF NOT EXISTS `playerNotes` (" +
				"`playerName` varchar(50)NOT NULL," +
				"`tier` varchar(50)NOT NULL," +
				"`note` varchar(100)NOT NULL," +
				"PRIMARY KEY(`playerName`,`tier`)" +
				")"
		);
		queries.put(1, sql);
		sql = new ArrayList<String>();
		sql.add("ALTER TABLE `playerNotes` ADD COLUMN `set_by` VARCHAR(50) NULL");
		sql.add("ALTER TABLE `playerNotes` ADD COLUMN `set_at` DATETIME NULL");
		queries.put(2, sql);
		return queries;
	}

	private final IDatabase database;
	private final IOutput output;
}
