/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

// TALON IDS
	// 0 = climber
	// 1 = softball
	// 2 = blender
	// 3 = softball wheel
	// 4 = EXTRA
	
	// 13 = front left
	// 14 = front right
	// 15 = rear left
	// 16 = rear right
	
// SOLENOID NUMBERS
	// 0,1 = Drive Train
	// 2,3 = Gear Grabber
	// 4,5 = Extendo Plastico
	// 6,7 = Gear Closer
	

package org.usfirst.frc.team4415.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends IterativeRobot {
	DoubleSolenoid m_solenoid1;
	DoubleSolenoid m_gearSolenoid;
	DoubleSolenoid m_gearGrabSolenoid;
	
	//Drive Joy
	Button m_driveTog;
	Button m_gearUp;
	Button m_gearClose;
	Button m_blenderGo;

	Joystick m_driveJoy;
	Joystick m_assistJoy;
	
	Boolean loop = false;
	Boolean softLoop = false;
	Boolean climbLoop = false;
	Boolean gearLoop = false;
	Boolean gear2Loop = false;
	Boolean blenderLoop = false;
	Boolean blenders = false;
	
	WPI_TalonSRX m_frontLeft;
	WPI_TalonSRX m_frontRight;
	WPI_TalonSRX m_rearLeft;
	WPI_TalonSRX m_rearRight;
	
	WPI_TalonSRX m_softball;
	WPI_TalonSRX m_climber;
	WPI_TalonSRX m_blender;
	
	WPI_TalonSRX m_softTurn;

	
	@Override
	public void robotInit() {
		
		
		m_softTurn = new WPI_TalonSRX(3);
		m_softball = new WPI_TalonSRX(1);
		m_climber = new WPI_TalonSRX(0);
		m_blender = new WPI_TalonSRX(2);
		
		m_solenoid1 = new DoubleSolenoid(0,1);
		m_gearSolenoid = new DoubleSolenoid(6,7);
		m_gearGrabSolenoid = new DoubleSolenoid(2,3);
		
		m_driveJoy = new Joystick(0);
		m_assistJoy = new Joystick (1);
		
		m_driveTog = new JoystickButton(m_driveJoy, 1);
		m_gearUp = new JoystickButton(m_assistJoy, 2);
		m_gearClose = new JoystickButton(m_assistJoy, 4);
		m_blenderGo = new JoystickButton(m_assistJoy, 8);
		
		
		m_frontLeft = new WPI_TalonSRX(13);
		m_frontRight = new WPI_TalonSRX(14);
		m_rearLeft = new WPI_TalonSRX(15);
		m_rearRight = new WPI_TalonSRX(16);
	}
	@Override
	public void teleopInit() {
		
		
	}
	@Override
	public void teleopPeriodic() {
		
		
		int m_assistPOV = m_assistJoy.getPOV();
		SmartDashboard.putNumber("Value of Motor", m_frontLeft.get());
		SmartDashboard.putNumber("POV", m_assistPOV);
		
		if (m_driveTog.get() == true && loop == false) {
			loop = true;
			if (m_solenoid1.get() == DoubleSolenoid.Value.kReverse) {
				m_solenoid1.set(DoubleSolenoid.Value.kForward);
			} else {
				m_solenoid1.set(DoubleSolenoid.Value.kReverse);
			}
			
		}
		if (m_driveTog.get() == false) {
			loop = false;
		}
		
		
		
//SUBSYSTEMS
		
		//SOFTBALL
		if (m_assistPOV != -1 && softLoop == false) {
			softLoop = true;
			
			if (m_assistPOV == 0) {
			m_softball.set(ControlMode.PercentOutput, -0.75);
			
			} else if (m_assistPOV == 180) {
				m_softball.set(ControlMode.PercentOutput, 0);
				
			} else if (m_assistPOV == 270 || m_assistPOV == 90) {
				m_softball.set(ControlMode.PercentOutput, -0.5);
			}
		}
		if (m_assistPOV == -1) {
			softLoop = false;
		}
		
		//TURNTABLE
		double axisX = m_assistJoy.getRawAxis(0);
		
		m_softTurn.set(ControlMode.PercentOutput, axisX);
		
		//BLENDER
		if (m_blenderGo.get() == true && blenderLoop == false) {
			blenderLoop = true;
			if (blenders == false) {
				m_blender.set(ControlMode.PercentOutput, -1.0);
				blenders = true;
			} else {
				m_blender.set(ControlMode.PercentOutput, 0.0);
				blenders = false;
			}
			
			SmartDashboard.putNumber("blender", m_blender.get());
			
		}
		if (m_blenderGo.get() == false) {
			blenderLoop = false;
		}
		
		//CLIMBER
		double rTrig, lTrig;
		rTrig = m_assistJoy.getRawAxis(3);
		lTrig = m_assistJoy.getRawAxis(2);
		
		if (rTrig != 0 && lTrig == 0 && climbLoop == false) {
			
			m_climber.set(ControlMode.PercentOutput, 0.75);
			
		} else if (lTrig !=0 && rTrig == 0 && climbLoop == false) {
			
			m_climber.set(ControlMode.PercentOutput, -0.75);
		
		} else if (lTrig !=0 && rTrig !=0) {
			climbLoop = true;
			m_climber.set(ControlMode.PercentOutput, 0);
			
		}
		
		if (lTrig == 0 && rTrig == 0) {
			climbLoop = false;
		}
		
		//GEAR GRABBER
		if (m_gearUp.get() == true && gearLoop == false) {
			gearLoop = true;
			if (m_gearSolenoid.get() == DoubleSolenoid.Value.kReverse) {
				m_gearSolenoid.set(DoubleSolenoid.Value.kForward);
			} else {
				m_gearSolenoid.set(DoubleSolenoid.Value.kReverse);
			}
			
		}
		if (m_gearUp.get() == false) {
			gearLoop = false;
		}
		if (m_gearClose.get() == true && gear2Loop == false) {
			gear2Loop = true;
			if (m_gearGrabSolenoid.get() == DoubleSolenoid.Value.kReverse) {
				m_gearGrabSolenoid.set(DoubleSolenoid.Value.kForward);
			} else {
				m_gearGrabSolenoid.set(DoubleSolenoid.Value.kReverse);
			}
			
		}
		if (m_gearClose.get() == false) {
			gear2Loop = false;
		}
		
		
		
		
//DRIVE TRAIN
		double valueX,valueY,valueZ;
		valueX = m_driveJoy.getRawAxis(0);
		valueY = m_driveJoy.getRawAxis(1) * -1;
		valueZ = m_driveJoy.getRawAxis(4) * -1;
		
		if (m_solenoid1.get() == DoubleSolenoid.Value.kForward) {
			double fR,fL,rR,rL;
			fR = valueY - valueX + valueZ;
			fL = -1 * (valueX + valueY - valueZ);
			rR = valueY - valueX - valueZ;
			rL = -1 * (valueY + valueX + valueZ);
			
			
			m_frontLeft.set(ControlMode.PercentOutput, fL);
			m_rearLeft.set(ControlMode.PercentOutput, rL);
			
			m_frontRight.set(ControlMode.PercentOutput, fR);
			m_rearRight.set(ControlMode.PercentOutput, rR);
		} else {
			
			double powRight = valueY + valueX;
			double powLeft = valueX - valueY;
			
			m_frontLeft.set(ControlMode.PercentOutput, powLeft);
			m_rearLeft.set(ControlMode.PercentOutput, powLeft);
			
			m_frontRight.set(ControlMode.PercentOutput, powRight);
			m_rearRight.set(ControlMode.PercentOutput, powRight);
		}
		
		
		
		
		
	}
}
