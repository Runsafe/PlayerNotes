package no.runsafe.PlayerNotes.database;

import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.HashMap;

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

	public HashMap<String, Note> getNotes()
	{
		return notes;
	}

	public void setNotes(HashMap<String, Note> notes)
	{
		this.notes = notes;
	}

	private RunsafePlayer player;
	private HashMap<String, Note> notes;
}
