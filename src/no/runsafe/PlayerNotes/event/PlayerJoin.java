package no.runsafe.PlayerNotes.event;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.event.player.IPlayerJoinEvent;
import no.runsafe.framework.event.player.IPlayerLoginEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerJoinEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerLoginEvent;

public class PlayerJoin implements IPlayerJoinEvent
{
	public PlayerJoin(NoteManager manager)
	{
		this.manager = manager;
	}

	@Override
	public void OnPlayerJoinEvent(RunsafePlayerJoinEvent event) {
		manager.sendNotices(event.getPlayer());
	}

	private NoteManager manager;
}
