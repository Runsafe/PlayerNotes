package no.runsafe.PlayerNotes.database;

import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.List;

public class PlayerNotes
{
	public RunsafePlayer getPlayer()
	{
		return player;
	}

	public void setPlayer(RunsafePlayer player)
	{
		this.player = player;
	}

	public List<Note> getNotes()
	{
		return notes;
	}

	public void setNotes(List<Note> notes)
	{
		this.notes = notes;
	}

	private RunsafePlayer player;
	private List<Note> notes;
}
