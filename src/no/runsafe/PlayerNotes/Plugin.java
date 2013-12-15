package no.runsafe.PlayerNotes;

import no.runsafe.PlayerNotes.command.NoteCommand;
import no.runsafe.PlayerNotes.database.NoteRepository;
import no.runsafe.PlayerNotes.event.PlayerJoin;
import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.features.Commands;
import no.runsafe.framework.features.Database;
import no.runsafe.framework.features.Events;

public class Plugin extends RunsafeConfigurablePlugin
{
	@Override
	protected void PluginSetup()
	{
		// Framework features
		addComponent(Events.class);
		addComponent(Commands.class);
		addComponent(Database.class);

		// Plugin components
		addComponent(NoteRepository.class);
		addComponent(NoteManager.class);
		addComponent(NoteCommand.class);
		addComponent(PlayerJoin.class);
	}
}
