package no.runsafe.PlayerNotes.database;

import no.runsafe.framework.api.player.IPlayer;

import java.time.Instant;

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

	public String getSetter()
	{
		return setter;
	}

	public void setSetter(String setter)
	{
		this.setter = setter;
	}

	public Instant getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(Instant timestamp)
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

	public boolean isVisible(IPlayer viewer)
	{
		return viewer.hasPermission(getPermission());
	}

	private String note;
	private String setter;
	private Instant timestamp;
	private String tier;
}
