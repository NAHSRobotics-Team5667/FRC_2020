/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.IntakeSubsystem;

public class LoadCommand extends CommandBase {
	private IntakeSubsystem m_intake;
	private int targetCount;
	private int initialCount;

	/**
	 * Creates a new LoadCommand.
	 */
	public LoadCommand(IntakeSubsystem intake, int targetCount) {
		// Use addRequirements() here to declare subsystem dependencies.
		m_intake = intake;
		addRequirements(m_intake);
		this.targetCount = targetCount;
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		initialCount = RobotContainer.ballCount;
		m_intake.extendIntake();
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		if (m_intake.tof_sensor.isDetecting()) {
			m_intake.startBelt();
		} else {
			m_intake.stopBelt();
		}

		if (m_intake.tof_sensor.hasPassed())
			RobotContainer.ballCount += 1;

	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		m_intake.stopBelt();
		m_intake.retractIntake();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return RobotContainer.ballCount - initialCount == targetCount;
	}
}