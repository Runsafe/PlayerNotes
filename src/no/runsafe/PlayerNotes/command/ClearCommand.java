package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.command.ExecutableCommand;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.HashMap;

public class ClearCommand extends ExecutableCommand // RunsafeAsyncCommand
{
	public ClearCommand(NoteManager manager)
	{
		super("clear", "Removes a note from a player, or all if * is used.", "runsafe.note.clear.<tier>", "tier");
		this.manager = manager;
	}

	@Override
	public String OnExecute(RunsafePlayer player, HashMap<String, String> params, String[] strings)
	{
		RunsafePlayer target = RunsafeServer.Instance.getPlayer(params.get("player"));
		if (params.get("tier").equals("*"))
		{
			manager.clearAllNotesForPlayer(target);
			return String.format("All notes for %s cleared.", target.getPrettyName());
		}
		else
		{
			manager.clearNoteForPlayer(target, params.get("tier"));
			return String.format("%s note for %s cleared.", params.get("tier"), target.getPrettyName());
		}
	}
//	@Override
//	public String requiredPermission() {
//		return "playernotes.set." + permissionTier;
//	}
//
//	@Override
//	public boolean CanExecute(RunsafePlayer executor, String[] args) {
//		if(args == null || args.length == 0)
//			permissionTier = "*";
//		else
//			permissionTier = args[0];
//
//		return executor.hasPermission("playernotes.set." + permissionTier);
//	}
//
//	@Override
//	// Override due to tier being optional
//	public String getCommandParams() {
//		return superCommand.getCommandParams() + " clear [tier]";
//	}
//
//	@Override
//	public String OnExecute(RunsafePlayer executor, String[] args) {
//		if(args == null || args.length == 0) {
//			manager.clearAllNotesForPlayer(getPlayer());
//			return String.format("All notes for %s cleared.", getPlayer().getName());
//		} else {
//			manager.clearNoteForPlayer(getPlayer(), args[0]);
//			return String.format("%s note for %s cleared.", args[0], getPlayer().getName());
//		}
//	}
//
//	private RunsafePlayer getPlayer() {
//		return RunsafeServer.Instance.getPlayer(superCommand.getArg("player"));
//	}

	private String permissionTier;
	private final NoteManager manager;
}
