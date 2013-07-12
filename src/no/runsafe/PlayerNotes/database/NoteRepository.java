package no.runsafe.PlayerNotes.database;

import no.runsafe.framework.api.database.IDatabase;
import no.runsafe.framework.api.database.IRow;
import no.runsafe.framework.api.database.ISet;
import no.runsafe.framework.api.database.Repository;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoteRepository extends Repository
{
	public NoteRepository(IDatabase database)
	{
		this.database = database;
	}

	public List<Note> get(RunsafePlayer player)
	{
		ISet data = database.Query("SELECT * FROM playerNotes WHERE playerName=?", player.getName());
		List<Note> noteMap = new ArrayList<Note>();

		if (data != null)
		{
			for (IRow row : data)
			{
				Note note = new Note();
				note.setSetter(RunsafeServer.Instance.getPlayerExact(row.String("set_by")));
				note.setTimestamp(row.DateTime("set_at"));
				note.setNote(row.String("note"));
				note.setTier(row.String("tier"));
				noteMap.add(note);
			}
		}
		return noteMap;
	}

	public Note get(RunsafePlayer player, String tier)
	{
		IRow data =
			database.QueryRow("SELECT * FROM playerNotes WHERE playerName=? AND tier=?", player.getName(), tier);
		if (data == null)
			return null;
		Note note = new Note();
		note.setSetter(RunsafeServer.Instance.getPlayerExact(data.String("set_by")));
		note.setTimestamp(data.DateTime("set_at"));
		note.setNote(data.String("note"));
		return note;
	}

	public void persist(RunsafePlayer player, String tier, String note, String setter)
	{
		if (note == null || note.isEmpty())
			database.Execute("DELETE FROM playerNotes WHERE playerName=? AND tier=?", player.getName(), tier);

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
