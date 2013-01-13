package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.command.ExecutableCommand;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

public class SetCommand extends ExecutableCommand // RunsafeAsyncCommand
{
	public SetCommand(NoteManager manager)
	{
		super("set", "Sets a note for the given player", "runsafe.note.set.<tier>", "tier", "note");
		this.manager = manager;
	}

	@Override
	public String OnExecute(RunsafePlayer player, HashMap<String, String> params, String[] args)
	{
		RunsafePlayer target = RunsafeServer.Instance.getPlayer(params.get("player"));
		String note;
		if (args.length > 0)
			note = params.get("note") + " " + StringUtils.join(args, " ");
		else
			note = params.get("note");
		manager.setNoteForPlayer(target, params.get("tier"), note);
		return String.format(("%s note set for %s."), params.get("tier"), target.getPrettyName());
	}

	//	public SetCommand(NoteManager manager, IScheduler scheduler)
//	{
//		super("set", scheduler, "tier", "note");
//		this.manager = manager;
//	}
//
//	@Override
//	public String requiredPermission()
//	{
//		return "playernotes.set." + permissionTier;
//	}
//
//	@Override
//	public boolean CanExecute(RunsafePlayer executor, String[] args)
//	{
//		if (args == null || args.length == 0)
//		{
//			permissionTier = "";
//			return true;
//		}
//		permissionTier = args[0];
//		return executor.hasPermission("playernotes.set." + args[0]);
//	}
//
//	@Override
//	public String OnExecute(RunsafePlayer executor, String[] args)
//	{
//		RunsafePlayer player = getPlayer();
//		String tier = getArg("tier");
//		String note = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
//
//		manager.setNoteForPlayer(player, tier, note);
//
//		return String.format(("%s note set for %s."), tier, player.getName());
//	}
//
//	private RunsafePlayer getPlayer()
//	{
//		return RunsafeServer.Instance.getPlayer(superCommand.getArg("player"));
//	}
//
	private final NoteManager manager;
//	protected String permissionTier;
}
