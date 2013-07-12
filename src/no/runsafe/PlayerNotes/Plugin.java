package no.runsafe.PlayerNotes;

import no.runsafe.PlayerNotes.command.NoteCommand;
import no.runsafe.PlayerNotes.database.NoteRepository;
import no.runsafe.PlayerNotes.event.PlayerJoin;
import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.RunsafePlugin;

public class Plugin extends RunsafeConfigurablePlugin
{
	@Override
	protected void PluginSetup()
	{
		addComponent(NoteRepository.class);
		addComponent(NoteManager.class);
		addComponent(NoteCommand.class);
		addComponent(PlayerJoin.class);
	}
}
