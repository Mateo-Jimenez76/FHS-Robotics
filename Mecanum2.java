// on the rev hub the motors and configuration are as follows
// port 0 is FR  port 1 is FL  port 2 is BR  port 3 is BL 
// names of the motors in the hub config should match below 

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
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
    //Speed modifier
    private Double  pwrRed  = .9;
    private double  launch  = 1;
    private double  intake  = 1;
    
    @Override
    public void runOpMode() {
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
            if(gamepad1.right_stick_y != 0 || gamepad1.right_stick_x !=0 || gamepad1.left_stick_x != 0) {

                //omni wheel     Left motors are flipped irl so I must flip the rotation

                //any foward movement
                if(rightStick_y < 0){
                    // right side
                    FRDrive.setPower((rightStick_y - rightStick_x)*pwrRed); // values from -1 to 1
                    BRDrive.setPower((rightStick_y + rightStick_x)*pwrRed); // values from -1 to 1

                    // left side
                    FLDrive.setPower((-rightStick_y - rightStick_x)*pwrRed); // values from -1 to 1
                    BLDrive.setPower((-rightStick_y + rightStick_x)*pwrRed); // values from -1 to 1
                }

                //any backward movement
                if(rightStick_y > 0){
                    // right side
                    FRDrive.setPower((rightStick_y - rightStick_x)*pwrRed); // values from -1 to 1
                    BRDrive.setPower((rightStick_y + rightStick_x)*pwrRed); // values from -1 to 1

                    // left side
                    FLDrive.setPower((-rightStick_y - rightStick_x)*pwrRed); // values from -1 to 1
                    BLDrive.setPower((-rightStick_y + rightStick_x)*pwrRed); // values from -1 to 1
                }

                //I think the FR and BR should be positive, FL and BL should be negative - Max
                //Side to side movement
                if(rightStick_y == 0){
                    FRDrive.setPower((rightStick_x)*pwrRed); // values from -1 to 1
                    FLDrive.setPower((rightStick_x)*pwrRed); // values from -1 to 1
                    BRDrive.setPower((-rightStick_x)*pwrRed); // values from -1 to 1
                    BLDrive.setPower((-rightStick_x)*pwrRed); // values from -1 to 1
                }
                //Rotate
                if(leftStick_x != 0){
                    FRDrive.setPower((leftStick_x)*pwrRed); // values from -1 to 1
                    FLDrive.setPower((leftStick_x)*pwrRed); // values from -1 to 1
                    BRDrive.setPower((leftStick_x)*pwrRed); // values from -1 to 1
                    BLDrive.setPower((leftStick_x)*pwrRed); // values from -1 to 1
                }
                
                
            }
            // if the joystick is not moved the motors will not move.
            // if the joystick is centered the motors will no longer move.
            else {
                FRDrive.setPower(0);
                FLDrive.setPower(0);
                BRDrive.setPower(0);
                BLDrive.setPower(0);
            }
            
            // turn on launch motor and the intake motor to launch balls
            if(gamepad1.right_bumper & launch == 1){
                LMotor.setPower(-1);
                IMotor.setPower(1);
                launch = 0;
                sleep(300);
            }
            // turn off 
            if(gamepad1.right_bumper & launch == 0){
                LMotor.setPower(0);
                IMotor.setPower(0);
                launch = 1;
                sleep(300);
            }
            
            // turn on intake motor to intake balls into robot
            if(gamepad1.left_bumper & intake == 1){
                IMotor.setPower(1);
                intake = 0;
                sleep(300);
            }
            //turn off 
            if(gamepad1.left_bumper & intake == 0){
                    IMotor.setPower(0);
                    intake = 1;
                    sleep(300);
            }
             
             //update
            telemetry.addData("Status", "Running ", rightStick_x, rightStick_y);
            telemetry.update();
        }
    }
}
