package no.runsafe.PlayerNotes.database;

import no.runsafe.framework.minecraft.player.RunsafePlayer;
import org.joda.time.DateTime;

public class Note
{
	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public RunsafePlayer getSetter()
	{
		return setter;
	}

	public void setSetter(RunsafePlayer setter)
	{
		this.setter = setter;
	}

	public DateTime getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getTier()
	{
		return tier;
	}

	public void setTier(String tier)
	{
		this.tier = tier;
	}

	public String getPermission()
	{
		return String.format("runsafe.note.show.%s", tier);
	}

	public boolean hasPermission(RunsafePlayer viewer)
	{
		return viewer.hasPermission(getPermission());
	}

	private String note;
	private RunsafePlayer setter;
	private DateTime timestamp;
	private String tier;
}
