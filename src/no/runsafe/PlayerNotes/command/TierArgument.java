package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.ITabComplete;
import no.runsafe.framework.api.command.argument.IValueExpander;
import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.player.IPlayer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TierArgument extends RequiredArgument implements ITabComplete, IValueExpander
{
	public TierArgument(NoteManager manager)
	{
		super("tier");
		this.manager = manager;
	}

	@Override
	public List<String> getAlternatives(IPlayer player, String filter)
	{
		List<String> alternatives = manager.getTiers();
		alternatives.add("*");
		if (filter == null || filter.isEmpty())
			return alternatives;

		String match = filter.toLowerCase();
		List<String> options = new ArrayList<>(alternatives.size());
		for (String option : alternatives)
			if (option.toLowerCase().startsWith(match))
				options.add(option);
		return options;
	}

	@Nullable
	@Override
	public String expand(ICommandExecutor executor, @Nullable String filter)
	{
		List<String> alternatives = getAlternatives(null, filter);
		return alternatives.size() != 1 ? null : alternatives.get(0);
	}

	private final NoteManager manager;
}
