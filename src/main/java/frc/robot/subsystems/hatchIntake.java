/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.constant;

/**
 * Add your docs here.
 */
public class hatchIntake extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  TalonSRX intake = new TalonSRX(constant.hatchIntake);
  DigitalInput zeroSensor = new DigitalInput(constant.hatchZeroSwitch);
  int hatchIntakeState = 1; //init variable for state of intake

  //the values below need to be changed for final robot, they are in encoder rotation units, 
  //to find values, rotate to the correct point, and find encoder value, and input it in the correct spot 

  static int nuetralPos = 2000; //position in nuetral state
  static int holdPos = 537; //position in holding hatch state
  static int pushPos = 3000; //position in pushing hatch state 

  /*hatch intake has 3 states:

  0:neutral state, not holding or pushing
  1:holding hatch
  2:pushing down hatch

  to change the state of the hatch, change this value to 0, 1, or 2
  */

  
  @Override
  public void initDefaultCommand() { //default command
    //setting PID constants
    intake.config_kP(0, 2.6);
    intake.config_kI(0, constant.hatchI);
    intake.config_kD(0, constant.hatchD);
    intake.configAllowableClosedloopError(0, 50);
//    intake.configClosedLoopPeakOutput(0, .5);


    intake.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder); //init encoder
    intake.setNeutralMode(NeutralMode.Brake); //sets motor to break mode
    //zero intake code
    Timer timer = new Timer();
    timer.start();
    while(!timer.hasPeriodPassed(.5)){
      intake.set(ControlMode.PercentOutput, -1);
    }
    intake.set(ControlMode.PercentOutput, 0);
    intake.setSelectedSensorPosition(0);
    timer.stop();
  }


 //zero's intake
 public void zeroIntake(){
  Timer timer = new Timer();
  timer.start();
  while(!timer.hasPeriodPassed(2)){
    intake.set(ControlMode.PercentOutput, -1);
  }
  intake.set(ControlMode.PercentOutput, 0);
  intake.setSelectedSensorPosition(0);
  timer.stop();
 }
 


//main hatch intake loop
  public void hatchIntakeloop(){
    switch(hatchIntakeState){ //find value of hatchIntakeState and :
      case 0: //if state is 0
          intake.set(ControlMode.Position, nuetralPos); //sets intake to nuetralPos
          break;
      case 1 : //if state is 1
          intake.set(ControlMode.Position, holdPos); //sets intake to holdPos
          break;
      case 2 : //if state is 2 
          intake.set(ControlMode.Position, pushPos); //sets intake to pushPos
          break;
      default :  //if state is unreadable (someone broke something)
          intake.set(ControlMode.PercentOutput, 0); //if hatchIntakeState is null or non [0-2], turn motor off
          System.out.println("Hatch Intake shut down, hatchIntakeState: " + hatchIntakeState); 
          break;
    }
  }
//cycles between hatch hold and nuetral Pos
  public void cycleHatch(Boolean button1released, Boolean button2released, Boolean button3released){ //on button press, go the next stage of the intake (neutral, hold hatch, score hatch)
    if (button1released){ //if button is pushed and then released
      hatchIntakeState = 0;
      intake.set(ControlMode.Position, nuetralPos); //sets intake to nuetralPos
    }
    else if (button2released){
      hatchIntakeState = 1;
      intake.set(ControlMode.Position, holdPos); //sets intake to holdPos
    }
    else if (button3released){
        hatchIntakeState = 2;
        intake.set(ControlMode.Position, pushPos); //sets intake to pushPos
    }
  }
  

public int getHatchState(){
  return hatchIntakeState;
}  

  public int getHatchSensor(){
    return intake.getSelectedSensorPosition();
  }

//function for testing or backdriving
public void driveHatch(Boolean button1, Boolean button2){
  if (button1){
    intake.set(ControlMode.PercentOutput, 1);
  }
  else if (button2){
    intake.set(ControlMode.PercentOutput, -1);
  }
  else{
    intake.set(ControlMode.PercentOutput, 0);
  }
}
}






