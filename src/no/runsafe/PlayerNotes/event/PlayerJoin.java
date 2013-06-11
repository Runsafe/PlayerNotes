package no.runsafe.PlayerNotes.event;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.api.event.IAsyncEvent;
import no.runsafe.framework.api.event.player.IPlayerJoinEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerJoinEvent;

public class PlayerJoin implements IPlayerJoinEvent, IAsyncEvent
{
	public PlayerJoin(NoteManager manager)
	{
		this.manager = manager;
	}

	@Override
	public void OnPlayerJoinEvent(RunsafePlayerJoinEvent event)
	{
		manager.sendNotices(event.getPlayer());
	}

	private final NoteManager manager;
}
