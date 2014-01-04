package no.runsafe.PlayerNotes.database;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import no.runsafe.framework.api.database.IDatabase;
import no.runsafe.framework.api.database.IRow;
import no.runsafe.framework.api.database.Repository;
import no.runsafe.framework.api.player.IPlayer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class NoteRepository extends Repository
{
	public NoteRepository(IDatabase database)
	{
		this.database = database;
	}

	public List<Note> get(IPlayer player)
	{
		return Lists.transform(
			database.query("SELECT * FROM playerNotes WHERE playerName=?", player.getName()),
			new Function<IRow, Note>()
			{
				@Override
				public Note apply(@Nullable IRow row)
				{
					assert row != null;
					Note note = new Note();
					note.setSetter(row.String("set_by"));
					note.setTimestamp(row.DateTime("set_at"));
					note.setNote(row.String("note"));
					note.setTier(row.String("tier"));
					return note;
				}
			}
		);
	}

	public Note get(IPlayer player, String tier)
	{
		IRow data = database.queryRow(
			"SELECT * FROM playerNotes WHERE playerName=? AND tier=?",
			player.getName(), tier
		);
		if (data.isEmpty())
			return null;
		Note note = new Note();
		note.setSetter(data.String("set_by"));
		note.setTimestamp(data.DateTime("set_at"));
		note.setNote(data.String("note"));
		return note;
	}

	public void persist(IPlayer player, String tier, String note, String setter)
	{
		if (note == null || note.isEmpty())
			database.execute("DELETE FROM playerNotes WHERE playerName=? AND tier=?", player.getName(), tier);
		else
			database.update(
				"INSERT INTO playerNotes (playerName, tier, note, set_by, set_at) VALUES (?, ?, ?, ?, NOW()) " +
					"ON DUPLICATE KEY UPDATE note=VALUES(note),set_by=VALUES(set_by),set_at=NOW()",
				player.getName(), tier, note, setter
			);
	}

	public void clear(IPlayer player)
	{
		database.execute("DELETE FROM playerNotes WHERE playerName=?", player.getName());
	}

	@Override
	public String getTableName()
	{
		return "playerNotes";
	}

	@Override
	public HashMap<Integer, List<String>> getSchemaUpdateQueries()
	{
		HashMap<Integer, List<String>> queries = new LinkedHashMap<Integer, List<String>>();
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
