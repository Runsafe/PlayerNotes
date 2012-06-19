package no.runsafe.PlayerNotes.event;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.event.player.IPlayerLoginEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerLoginEvent;

public class PlayerLogin implements IPlayerLoginEvent
{
	public PlayerLogin(NoteManager manager)
	{
		this.manager = manager;
	}

	@Override
	public void OnPlayerLogin(RunsafePlayerLoginEvent event)
	{
		manager.sendNotices(event.getPlayer());
	}

	private NoteManager manager;
}
