/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.utils.PIDFController;

public class ClimbCommand extends CommandBase {
	ClimbSubsystem m_climbSubsystem;
	// private PIDFController winchController = new PIDFController("Hook height",
	// .01, .01, .01, .01);

	/**
	 * Creates a new ClimbCommand.
	 */
	public ClimbCommand(ClimbSubsystem subsystem) {
		// Use addRequirements() here to declare subsystem dependencies.
		m_climbSubsystem = subsystem;
		addRequirements(m_climbSubsystem);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {

	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		/*
		 * 
		 * Haikus with Olu 2:
		 * 
		 * When a trigger's pressed, the motor moves up or down to the target height.
		 * This has been Haikus with Olu.
		 */
		m_climbSubsystem.DriveWinch(RobotContainer.getController().getLeftY());
		m_climbSubsystem.DriveHook(RobotContainer.getController().getRightY());
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
