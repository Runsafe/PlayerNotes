package no.runsafe.PlayerNotes;

import no.runsafe.PlayerNotes.command.*;
import no.runsafe.PlayerNotes.database.NoteRepository;
import no.runsafe.PlayerNotes.event.PlayerJoin;
import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.api.command.Command;
import no.runsafe.framework.api.command.argument.Player;
import no.runsafe.framework.features.Commands;
import no.runsafe.framework.features.Database;
import no.runsafe.framework.features.Events;

public class Plugin extends RunsafeConfigurablePlugin
{
	@Override
	protected void pluginSetup()
	{
		// Framework features
		addComponent(Events.class);
		addComponent(Commands.class);
		addComponent(Database.class);

		// Plugin components
		addComponent(NoteRepository.class);
		addComponent(NoteManager.class);
		addComponent(PlayerJoin.class);

		// Commands
		Command note = new Command("note", "Manage player notes", "runsafe.notes", new Player().require());
		note.addSubCommand(getInstance(SetCommand.class));
		note.addSubCommand(getInstance(ClearCommand.class));
		note.addSubCommand(getInstance(ListCommand.class));
		addComponent(note);
	}
}
