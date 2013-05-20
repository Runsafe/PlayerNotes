package no.runsafe.PlayerNotes.database;

import no.runsafe.framework.server.player.RunsafePlayer;
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

	private String note;
	private RunsafePlayer setter;
	private DateTime timestamp;
}
