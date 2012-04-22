package no.runsafe.PlayerNotes;

import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.configuration.IConfigurationFile;
import no.runsafe.PlayerNotes.database.*;
import no.runsafe.PlayerNotes.command.*;
import no.runsafe.PlayerNotes.event.*;

import java.io.InputStream;

public class Plugin extends RunsafePlugin implements IConfigurationFile
{
	@Override
	protected void PluginSetup()
	{
		addComponent(NoteRepository.class);
		addComponent(NoteManager.class);
		addComponent(getInstance(NoteCommand.class));
		addComponent(PlayerLogin.class);
	}

	@Override
	public String getConfigurationPath()
	{
		return "plugins/" + this.getName() + "/config.yml";
	}

	@Override
	public InputStream getDefaultConfiguration()
	{
		return getResource("defaults.yml");
	}
}
