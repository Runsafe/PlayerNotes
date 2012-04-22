package no.runsafe.PlayerNotes.database;

import no.runsafe.framework.database.RunsafeEntity;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.HashMap;

public class PlayerNotes extends RunsafeEntity
{
	public RunsafePlayer getPlayer()
	{
		return player;
	}

	public void setPlayer(RunsafePlayer player)
	{
		this.player = player;
	}

	public HashMap<String, String> getNotes()
	{
		return notes;
	}

	public void setNotes(HashMap<String, String> notes)
	{
		this.notes = notes;
	}

	private RunsafePlayer player;
	private HashMap<String, String> notes;
}
