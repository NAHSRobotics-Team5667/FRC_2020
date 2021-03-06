/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.Constants.VisionConstants;
import frc.robot.RobotState.States;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.DriveTrainSubsystem.DriveModes;
import frc.robot.utils.LimeLight;
import frc.robot.utils.PIDFController;

public class DriveTrainCommand extends CommandBase {

	private DriveTrainSubsystem m_drive;
	private PIDFController angleController = new PIDFController("Angle", VisionConstants.kP, VisionConstants.kI,
			VisionConstants.kD, 0);

	/**
	 * Create a Drive Train Subsystem
	 * 
	 * @param DriveTrain - The Drive Train Subsystem
	 */
	public DriveTrainCommand(DriveTrainSubsystem DriveTrain) {
		// Use addRequirements() here to declare subsystem dependencies.
		m_drive = DriveTrain;
		addRequirements(m_drive);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		m_drive.stop();
		LimeLight.getInstance().turnLightOn();
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {

		Map<String, Double> sticks = RobotContainer.getController().getSticks();
		if (m_drive.getDriveMode() == DriveModes.MANUAL) {
			m_drive.setDriveMode(DriveTrainSubsystem.DriveModes.MANUAL);
			// Drive using joysticks

			m_drive.drive(sticks.get("LSY"), sticks.get("RSX"),
					RobotContainer.getController().getStickButtonPressed(RobotContainer.getController().getLeftHand()));

		}

		if (Constants.m_RobotState.getCurrentState() != States.INTAKING
				&& Constants.m_RobotState.getCurrentState() != States.ALIGNED
				&& Constants.m_RobotState.getCurrentState() != States.ALIGNING
				&& Constants.m_RobotState.getCurrentState() != States.REVED
				&& Constants.m_RobotState.getCurrentState() != States.REVING
				&& Constants.m_RobotState.getCurrentState() != States.CLIMBING) {
			Constants.m_RobotState.setState(States.DRIVE);
		}
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		m_drive.stop();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}

}
