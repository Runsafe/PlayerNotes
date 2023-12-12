package no.runsafe.PlayerNotes.database;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import no.runsafe.framework.api.database.*;
import no.runsafe.framework.api.player.IPlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class NoteRepository extends Repository
{
	public List<Note> get(IPlayer player)
	{
		return Lists.transform(
			database.query("SELECT * FROM playerNotes WHERE player=?", player.getUniqueId().toString()),
			new Function<IRow, Note>()
			{
				@Override
				public Note apply(@Nullable IRow row)
				{
					assert row != null;
					Note note = new Note();
					note.setSetter(row.String("set_by"));
					note.setTimestamp(row.Instant("set_at"));
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
			"SELECT * FROM playerNotes WHERE player=? AND tier=?",
			player.getUniqueId().toString(), tier
		);
		if (data.isEmpty())
			return null;
		Note note = new Note();
		note.setSetter(data.String("set_by"));
		note.setTimestamp(data.Instant("set_at"));
		note.setNote(data.String("note"));
		return note;
	}

	public void persist(IPlayer player, String tier, String note, String setter)
	{
		if (note == null || note.isEmpty())
			database.execute("DELETE FROM playerNotes WHERE player=? AND tier=?", player.getUniqueId().toString(), tier);
		else
			database.update(
				"INSERT INTO playerNotes (player, tier, note, set_by, set_at) VALUES (?, ?, ?, ?, NOW()) " +
					"ON DUPLICATE KEY UPDATE note=VALUES(note),set_by=VALUES(set_by),set_at=NOW()",
				player.getUniqueId().toString(), tier, note, setter
			);
	}

	public void clear(IPlayer player)
	{
		database.execute("DELETE FROM playerNotes WHERE player=?", player.getUniqueId().toString());
	}

	@Nonnull
	@Override
	public String getTableName()
	{
		return "playerNotes";
	}

	@Nonnull
	@Override
	public ISchemaUpdate getSchemaUpdateQueries()
	{
		ISchemaUpdate update = new SchemaUpdate();

		update.addQueries(
			"CREATE TABLE IF NOT EXISTS `playerNotes` (" +
				"`playerName` varchar(50)NOT NULL," +
				"`tier` varchar(50)NOT NULL," +
				"`note` varchar(100)NOT NULL," +
				"PRIMARY KEY(`playerName`,`tier`)" +
			")"
		);

		update.addQueries(
			"ALTER TABLE `playerNotes` ADD COLUMN `set_by` VARCHAR(50) NULL",
			"ALTER TABLE `playerNotes` ADD COLUMN `set_at` DATETIME NULL"
		);

		update.addQueries(
			String.format("ALTER TABLE `%s` CHANGE `playerName` `player` varchar(50) NOT NULL", getTableName()),
			String.format( // Player names -> Unique IDs
				"UPDATE IGNORE `%s` SET `player` = " +
					"COALESCE((SELECT `uuid` FROM player_db WHERE `name`=`%s`.`player`), `player`) " +
					"WHERE length(`player`) != 36",
				getTableName(), getTableName()
			)
		);

		return update;
	}
}
