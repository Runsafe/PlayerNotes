package no.runsafe.PlayerNotes.command;

import no.runsafe.PlayerNotes.NoteManager;
import no.runsafe.framework.command.RunsafeAsyncCommand;
import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;

public class ClearCommand extends RunsafeAsyncCommand
{
	public ClearCommand(NoteManager manager, IScheduler scheduler) {
		super("clear", scheduler);
		this.manager = manager;
	}

	@Override
	public String requiredPermission() {
		return "playernotes.set." + permissionTier;
	}

	@Override
	public boolean CanExecute(RunsafePlayer executor, String[] args) {
		if(args == null || args.length == 0)
			permissionTier = "*";
		else
			permissionTier = args[0];

		return executor.hasPermission("playernotes.set." + permissionTier);
	}

	@Override
	// Override due to tier being optional
	public String getCommandParams() {
		return superCommand.getCommandParams() + " clear [tier]";
	}

	@Override
	public String OnExecute(RunsafePlayer executor, String[] args) {
		if(args == null || args.length == 0) {
			manager.clearAllNotesForPlayer(getPlayer());
			return String.format("All notes for %s cleared.", getPlayer().getName());
		} else {
			manager.clearNoteForPlayer(getPlayer(), args[0]);
			return String.format("%s note for %s cleared.", args[0], getPlayer().getName());
		}
	}

	private RunsafePlayer getPlayer() {
		return RunsafeServer.Instance.getPlayer(superCommand.getArg("player"));
	}

	private String permissionTier;
	private final NoteManager manager;
}
