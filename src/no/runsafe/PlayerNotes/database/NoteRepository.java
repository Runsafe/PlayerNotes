package no.runsafe.PlayerNotes.database;

import no.runsafe.framework.database.IDatabase;
import no.runsafe.framework.database.IRepository;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

public class NoteRepository implements IRepository<PlayerNotes, RunsafePlayer>
{
	public NoteRepository(IDatabase database, IOutput output)
	{
		this.database = database;
		this.output = output;
	}

	@Override
	public PlayerNotes get(RunsafePlayer player)
	{
		try
		{
			PreparedStatement select = database.prepare("SELECT * FROM playerNotes WHERE playerName=?");
			select.setString(1, player.getName());
			PlayerNotes notes = new PlayerNotes();
			notes.setPlayer(player);
			HashMap<String, String> noteMap = new HashMap<String, String>();
			ResultSet data = select.executeQuery();
			while (data.next())
			{
				noteMap.put(data.getString("tier"), data.getString("note"));
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

	@Override
	public void persist(PlayerNotes playerNotes)
	{
		PlayerNotes notes = get(playerNotes.getPlayer());
		try
		{
			PreparedStatement update = database.prepare("UPDATE playerNotes SET note=? WHERE playerName=? AND tier=?");
			update.setString(2, playerNotes.getPlayer().getName());
			PreparedStatement insert = database.prepare("INSERT INTO playerNotes (playerName, tier, note) VALUES (?, ?, ?)");
			insert.setString(1, playerNotes.getPlayer().getName());
			PreparedStatement delete = database.prepare("DELETE FROM playerNotes WHERE playerName=? AND tier=?");
			delete.setString(1, playerNotes.getPlayer().getName());

			for (String tier : playerNotes.getNotes().keySet())
				if (notes.getNotes().containsKey(tier))
				{
					if (!playerNotes.getNotes().get(tier).equals(notes.getNotes().get(tier)))
					{
						update.setString(3, tier);
						update.setString(1, playerNotes.getNotes().get(tier));
						update.executeUpdate();
					}
				}
				else
				{
					insert.setString(2, tier);
					insert.setString(3, playerNotes.getNotes().get(tier));
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

	@Override
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

	private IDatabase database;
	private IOutput output;
}
