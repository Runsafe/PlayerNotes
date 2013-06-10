package no.runsafe.PlayerNotes.database;

import no.runsafe.framework.database.IDatabase;
import no.runsafe.framework.database.Repository;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteRepository extends Repository
{
	public NoteRepository(IDatabase database)
	{
		this.database = database;
	}

	public List<Note> get(RunsafePlayer player)
	{
		List<Map<String, Object>> data = database.Query("SELECT * FROM playerNotes WHERE playerName=?", player.getName());
		List<Note> noteMap = new ArrayList<Note>();

		if (data != null)
		{
			for (Map<String, Object> row : data)
			{
				Note note = new Note();
				note.setSetter(RunsafeServer.Instance.getPlayerExact((String) row.get("set_by")));
				note.setTimestamp(convert(row.get("set_at")));
				note.setNote((String) row.get("note"));
				note.setTier((String) row.get("tier"));
				noteMap.add(note);
			}
		}
		return noteMap;
	}

	public Note get(RunsafePlayer player, String tier)
	{
		Map<String, Object> data =
			database.QueryRow("SELECT * FROM playerNotes WHERE playerName=? AND tier=?", player.getName(), tier);
		if (data == null || data.isEmpty())
			return null;
		Note note = new Note();
		note.setSetter(RunsafeServer.Instance.getPlayerExact((String) data.get("set_by")));
		note.setTimestamp(convert(data.get("set_at")));
		note.setNote((String) data.get("note"));
		return note;
	}

	public void persist(RunsafePlayer player, String tier, String note, String setter)
	{
		Note current = get(player, tier);
		if (note == null || note.isEmpty())
			database.Execute("DELETE FROM playerNotes WHERE playerName=? AND tier=?", player.getName(), tier);

		else if (current == null)
			database.Update(
				"INSERT INTO playerNotes (playerName, tier, note, set_by, set_at) VALUES (?, ?, ?, ?, NOW()) " +
					"ON DUPLICATE KEY UPDATE note=VALUES(note),set_by=VALUES(set_by),set_at=NOW()",
				player.getName(), tier, note, setter
			);
	}

	public void clear(RunsafePlayer player)
	{
		database.Execute("DELETE FROM playerNotes WHERE playerName=?", player.getName());
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
}
