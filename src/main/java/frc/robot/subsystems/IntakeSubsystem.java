
package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.revrobotics.Rev2mDistanceSensor;
import com.revrobotics.Rev2mDistanceSensor.Unit;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeSubsystem extends SubsystemBase {

	private SpeedController m_intake;
	private Solenoid m_rSolenoid, m_lSolenoid;
	private SpeedController m_belt;
	private Rev2mDistanceSensor m_intakeSensor;
	private Rev2mDistanceSensor m_shooterSensor;

	private ShuffleboardTab compTab = Shuffleboard.getTab("Competition");

	private boolean previousSeenBallEnter = false;
	private boolean previousSeenBallExit = false;

	/**
	 * Creates an intake subsystem instance
	 * 
	 * @param intake        - motor controller for the intake wheels
	 * @param rSolenoid     - right piston for extending/retracting intake
	 * @param lSolenoid     - left piston for extending/retracting intake
	 * @param belt          - motor controller for the belt
	 * @param intakeSensor  - Rev2mDistanceSensor that detects if a ball has entered
	 *                      the robot
	 * @param shooterSensor - Rev2mDistanceSensor that detects if a ball has exited
	 *                      the robot
	 */
	public IntakeSubsystem(SpeedController intake, Solenoid rSolenoid, Solenoid lSolenoid, SpeedController belt,
			Rev2mDistanceSensor intakeSensor, Rev2mDistanceSensor shooterSensor) {
		m_intake = intake;
		m_rSolenoid = rSolenoid;
		m_lSolenoid = lSolenoid;
		m_belt = belt;
		m_intakeSensor = intakeSensor;
		m_shooterSensor = shooterSensor;
	}

	/**
	 * Extends the intake outside of frame perimeter
	 */
	public void extendIntake() {
		m_rSolenoid.set(Constants.IntakeConstants.SOLENOID_FIRED);
		m_lSolenoid.set(Constants.IntakeConstants.SOLENOID_FIRED);
		m_intake.set(Constants.IntakeConstants.INTAKE_MOTOR_SPEED);
	}

	/**
	 * Retracts the intake inside of frame perimeter
	 */
	public void retractIntake() {
		m_rSolenoid.set(!Constants.IntakeConstants.SOLENOID_FIRED);
		m_lSolenoid.set(!Constants.IntakeConstants.SOLENOID_FIRED);
		m_intake.set(0);
	}

	/**
	 * Determine if the Rev2mDistanceSensor saw a power cell enter the robot or not
	 * 
	 * @return true if ball is present, therefore seen entering robot
	 */
	public boolean hasSeenBallEnter() {
		boolean currentSeenBallEnter = false;
		if (m_intakeSensor.getRange(Unit.kInches) <= Constants.IntakeConstants.SENSOR_RANGE_INCHES
				&& previousSeenBallEnter == false) {
			currentSeenBallEnter = true;
			previousSeenBallEnter = currentSeenBallEnter;
		} else if (m_intakeSensor.getRange(Unit.kInches) > Constants.IntakeConstants.SENSOR_RANGE_INCHES
				&& previousSeenBallEnter == true) {
			currentSeenBallEnter = false;
			previousSeenBallEnter = currentSeenBallEnter;
		}
		return currentSeenBallEnter;
	}

	/**
	 * Determine if the Rev2mDistanceSensor saw a power cell leave the robot or not
	 * 
	 * @return true if ball is absent, therefore seen leaving robot
	 */
	public boolean hasSeenBallExit() {
		boolean currentSeenBallExit = false;
		if (m_shooterSensor.getRange(Unit.kInches) <= Constants.IntakeConstants.SENSOR_RANGE_INCHES
				&& previousSeenBallExit == true) {
			currentSeenBallExit = false;
			previousSeenBallExit = currentSeenBallExit;
		} else if (m_shooterSensor.getRange(Unit.kInches) > Constants.IntakeConstants.SENSOR_RANGE_INCHES
				&& previousSeenBallExit == false) {
			currentSeenBallExit = true;
			previousSeenBallExit = currentSeenBallExit;
		}
		return currentSeenBallExit;
	}

	/**
	 * Start the belt
	 */
	public void startBelt() {
		m_belt.set(Constants.IntakeConstants.BELT_MOTOR_SPEED);
	}

	/**
	 * Stop the belt
	 */
	public void stopBelt() {
		m_belt.set(0);
	}

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}

	public void outputIntakeStats() {
		compTab.add("Right Solenoid Fired", m_rSolenoid.get());
		compTab.add("Left Solenoid Fired", m_lSolenoid.get());
	}
}