package no.runsafe.PlayerNotes;

import no.runsafe.framework.RunsafePlugin;
import no.runsafe.PlayerNotes.database.*;
import no.runsafe.PlayerNotes.command.*;
import no.runsafe.PlayerNotes.event.*;

public class Plugin extends RunsafePlugin
{
	@Override
	protected void PluginSetup()
	{
		addComponent(NoteRepository.class);
		addComponent(NoteManager.class);
		addComponent(getInstance(NoteCommand.class));
		addComponent(PlayerJoin.class);
	}
}
