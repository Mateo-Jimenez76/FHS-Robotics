// on the rev hub the motors and configuration are as follows
// port 0 is FR  port 1 is FL  port 2 is BR  port 3 is BL 
// names of the motors in the hub config should match below 

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="Mecanum2", group="Linear Opmode")
public class Mecanum2 extends LinearOpMode {

    //Declaring motors    F= Front B= Back R= Right L= Left LMotor= Launch Motor I= Intake
    private DcMotor FRDrive = null;
    private DcMotor FLDrive = null;
    private DcMotor BRDrive = null;
    private DcMotor BLDrive = null;
    private DcMotor LMotor  = null;
    private DcMotor IMotor  = null;
    //Power Reduction
    private final double   powerReduction  = .9;
    private boolean  launchOn  = false;
    private boolean  intakeOn  = false;

    //amount LMotor accelerates each repeat of the loop till it reaches 1
    private final double motorAcceleration = .1;

    @Override
    public void runOpMode() 
    {
        motorSetUp();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // gets the value of y for the power forwards and backwards
            double rightStick_y = gamepad1.right_stick_y; 
            // gets the value of x for the power left and right
            double rightStick_x = gamepad1.right_stick_x; 
            
            // gets the power to turn
            double leftStick_x = gamepad1.left_stick_x;
            
            // if the value of the joystick is not zero meaning joystick is not centered.
            // Will take the value of the joystick for the power of the right and left motors.
            if(joystickInputDetected())
            {
                //omni wheel     Left motors are flipped irl so I must flip the rotation

                //any foward movement
                if(rightStick_y < 0){
                    // right side
                    FRDrive.setPower((rightStick_y - rightStick_x)*powerReduction); // values from -1 to 1
                    BRDrive.setPower((rightStick_y + rightStick_x)*powerReduction); // values from -1 to 1

                    // left side
                    FLDrive.setPower((-rightStick_y - rightStick_x)*powerReduction); // values from -1 to 1
                    BLDrive.setPower((-rightStick_y + rightStick_x)*powerReduction); // values from -1 to 1
                }

                //any backward movement
                if(rightStick_y > 0){
                    // right side
                    FRDrive.setPower((rightStick_y - rightStick_x)*powerReduction); // values from -1 to 1
                    BRDrive.setPower((rightStick_y + rightStick_x)*powerReduction); // values from -1 to 1

                    // left side
                    FLDrive.setPower((-rightStick_y - rightStick_x)*powerReduction); // values from -1 to 1
                    BLDrive.setPower((-rightStick_y + rightStick_x)*powerReduction); // values from -1 to 1
                }

                //I think the FR and BR should be positive, FL and BL should be negative - Max
                //Side to side movement
                if(rightStick_y == 0){
                    FRDrive.setPower((rightStick_x)*powerReduction); // values from -1 to 1
                    FLDrive.setPower((rightStick_x)*powerReduction); // values from -1 to 1
                    BRDrive.setPower((-rightStick_x)*powerReduction); // values from -1 to 1
                    BLDrive.setPower((-rightStick_x)*powerReduction); // values from -1 to 1
                }
                //Rotate
                if(leftStick_x != 0){
                    FRDrive.setPower((leftStick_x)*powerReduction); // values from -1 to 1
                    FLDrive.setPower((leftStick_x)*powerReduction); // values from -1 to 1
                    BRDrive.setPower((leftStick_x)*powerReduction); // values from -1 to 1
                    BLDrive.setPower((leftStick_x)*powerReduction); // values from -1 to 1
                }
                
                
            }
            // if the joystick is not moved the motors will not move.
            // if the joystick is centered the motors will no longer move.
            else 
            {
                setPowerToAllWheelMotors(0);
            }

            launcher();
            
             //update
            telemetry.addData("Status", "Running ", rightStick_x, rightStick_y);
            telemetry.update();
        }
    }

    //Sets references for all motors and set default zero power bahaviour to Brake.
    //Sends telemetry data once complete.
    private void motorsSetup()
    {
        FRDrive  = hardwareMap.get(DcMotor.class, "FRDrive");
        FLDrive  = hardwareMap.get(DcMotor.class, "FLDrive");
        BRDrive  = hardwareMap.get(DcMotor.class, "BRDrive");
        BLDrive  = hardwareMap.get(DcMotor.class, "BLDrive");
        LMotor   = hardwareMap.get(DcMotor.class, "LMotor");
        IMotor   = hardwareMap.get(DcMotor.class, "IMotor");

        FRDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FLDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    private void launcher()
    {
        private int sleepTimeInMilleseconds = 300;
        // turn on launch motor and the intake motor to launch balls
        if(gamepad1.right_bumper && launchOn == false)
        {
            launchOn = true;
            //reapeat until LMotor Power = 1 (10 times) or until LMotor is turned off
            for(int i = 0; i < 10 && launchOn == true; i++)
            {  
                LMotor.setPower(-motorAcceleration * i); 
                telemetry.addData("Status","LMotor Power: " + LMotor.getPower());
                sleep(sleepTimeInMilleseconds); //0.3 seconds, 10 times, 3 seconds for loop
            }
        }
        // turn off 
        if(gamepad1.right_bumper && launchOn == true)
        {
            LMotor.setPower(0);
            IMotor.setPower(0);
            launch = false;
            sleep(sleepTimeInMilleseconds);
        }
        
        // turn on intake motor to intake balls into robot
        if(gamepad1.left_bumper && intakeOn == false)
        {
            IMotor.setPower(1);
            intake = true;
            sleep(sleepTimeInMilleseconds);
        }
        //turn off
        if(gamepad1.left_bumper && intakeOn == true)
        {
            IMotor.setPower(0);
            intake = false;
            sleep(sleepTimeInMilleseconds);
        }
    }


    private boolean joystickInputDetected()
    {
        return gamepad1.right_stick_y != 0 || gamepad1.right_stick_x !=0 || gamepad1.left_stick_x != 0;
    }

    //double power: The power to set all wheel motors to. (FRDrive,FLDrive,BRDrive,BLDrive)
    private void setPowerToAllWheelMotors(double power)
    {
        FRDrive.setPower(power);
        FLDrive.setPower(power);
        BRDrive.setPower(power);
        BLDrive.setPower(power);
    }

    //DCMotor motor: The motor to change the power of
    //double endingPower: The ending power the motor should reach
    //double acceleration: How fast the motor should reach the endingPower. Power goes up by the acceleration amount every 0.1 seconds.
    private void SetPowerOverTime(DcMotor motor, double endingPower, double acceleration)
    {
        if(endingPower < 0 && acceleration > 0)
        {
            acceleration *= -1;
        }

        if(endingPower > 0 && )
        {
            
        }


        for(int i = 0; i < 10 && launchOn == true; i++)
        {   
            if(motor.getPower() == endingPower)
            {
                return;
            }
            motor.setPower(-motorAcceleration * i); 
            telemetry.addData("Status","motor Power: " + motor.getPower());
            sleep(100); // 0.1
        }
    }
}
