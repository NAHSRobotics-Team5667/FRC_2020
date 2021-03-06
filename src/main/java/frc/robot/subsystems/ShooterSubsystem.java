/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.RobotState.States;

public class ShooterSubsystem extends SubsystemBase {

	private WPI_TalonFX m_master;
	private ShuffleboardTab compTab = Shuffleboard.getTab("Teleop");
	private ShuffleboardTab graphTab = Shuffleboard.getTab("Graphs");

	private SimpleMotorFeedforward ff = new SimpleMotorFeedforward(Constants.ShooterConstants.ksVolts,
			Constants.ShooterConstants.kvVoltSecondsPerMeter, Constants.ShooterConstants.kaVoltSecondsSquaredPerMeter);

	private PIDController m_controller = new PIDController(Constants.ShooterConstants.kP, Constants.ShooterConstants.kI,
			Constants.ShooterConstants.kD);

	public boolean justRamped = false;

	/**
	 * Creates a shooter subsystem
	 * 
	 * @param masterWheel - motor controller that controls the slave wheel
	 */

	public ShooterSubsystem(WPI_TalonFX master) {
		this.m_master = master;
		m_master.configFactoryDefault();
		m_master.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
		TalonFXConfiguration configuration = new TalonFXConfiguration();
		configuration.openloopRamp = .8; // .5;
		m_master.configAllSettings(configuration);

		m_master.setSelectedSensorPosition(0);

		setNeutralMode(NeutralMode.Coast);

		outputTelemetry();

		m_controller.setTolerance(200, 200);

	}

	@Override
	public void periodic() {
		// This method will be called once per scheduler

	}

	/**
	 * Set the neutral mode of the shooter motor
	 * 
	 * @param mode - The neutral mode
	 */
	public void setNeutralMode(NeutralMode mode) {
		m_master.setNeutralMode(mode);
	}

	/**
	 * Shoot based on a curve using limelight area
	 * 
	 * @param area - Target area
	 */
	public void curveFire(double area) {
		// fireRPM(170.8 * Math.pow(area, 2) + -688.58 * area + 5300);
		fireRPM(155 * Math.pow(area, 2) + -688.58 * area + 5000);
	}

	/**
	 * Fires the shooting wheels
	 * 
	 * @param speed - the speed of the wheels thats needed
	 */
	public void fire(double speed) {
		m_master.set(-speed);
	}

	public void fireRPM(double desiredRPM) {
		m_controller.setSetpoint(desiredRPM);
		double ff = Constants.ShooterConstants.ksVolts + desiredRPM * Constants.ShooterConstants.kvVoltSecondsPerMeter;
		setVoltage(m_controller.calculate(getCurrentRPM()) + ff);
	}

	/**
	 * Fire the shooting wheels using specific voltage
	 * 
	 * @param voltage - The voltage to set the wheels at
	 */
	public void setVoltage(double voltage) {
		fire(voltage / 12);
	}

	/**
	 * Stops the shooter from firing
	 */
	public void stopFire() {
		m_master.stopMotor();
	}

	/**
	 * Resets the encoder for the right shooter wheel
	 */
	public void resetEncoder() {
		m_master.setSelectedSensorPosition(0);
	}

	/**
	 * Get the current RPM of the shooter wheels
	 * 
	 * @return - The current RPM of the shooter wheels
	 */
	public double getCurrentRPM() {
		return -m_master.getSelectedSensorVelocity(0) * Constants.ShooterConstants.ENCODER_CONSTANT * 600.0;
	}

	/**
	 * Reset the Shooter PID Integral Value
	 */
	public void resetIError() {
		m_controller.reset();
	}

	/**
	 * Get the Shooter PID Controller
	 * 
	 * @return - PID Controller for the shooter
	 */
	public PIDController getController() {
		return m_controller;
	}

	/**
	 * Get the current pulled by the motor
	 * 
	 * @return - The current pulled by the motor
	 */
	public double getOutputCurrent() {
		return m_master.getStatorCurrent();
	}

	/**
	 * Output the shooter's telemetry
	 */
	public void outputTelemetry() {
		compTab.addNumber("Shooter RPM", new DoubleSupplier() {
			@Override
			public double getAsDouble() {
				return getCurrentRPM();
			}
		}).withWidget(BuiltInWidgets.kGraph);

		compTab.addNumber("Shooter Temp", new DoubleSupplier() {

			@Override
			public double getAsDouble() {
				return m_master.getTemperature();
			}
		});
	}

	public void debug() {
		graphTab.addNumber("Shooter Current", new DoubleSupplier() {
			@Override
			public double getAsDouble() {
				return getOutputCurrent();
			}
		});

		graphTab.addNumber("Shot Times", new DoubleSupplier() {

			@Override
			public double getAsDouble() {
				return RobotContainer.ballCount;
			}
		}).withWidget(BuiltInWidgets.kGraph).withPosition(0, 0).withSize(2, 2);
	}
}
