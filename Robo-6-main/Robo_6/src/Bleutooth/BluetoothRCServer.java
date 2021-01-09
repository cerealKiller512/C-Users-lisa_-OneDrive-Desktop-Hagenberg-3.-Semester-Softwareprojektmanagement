package Bleutooth;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Robo_6.Robo_6;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class BluetoothRCServer {
	public static void main(String args[]) throws IOException {
		int input;
		ServerSocket server = new ServerSocket(1111);
		EV3LargeRegulatedMotor motorA = new EV3LargeRegulatedMotor(MotorPort.B);
		EV3LargeRegulatedMotor motorB = new EV3LargeRegulatedMotor(MotorPort.C);
		IsEscapeDownChecker isEscapeDown = new IsEscapeDownChecker(server);
		isEscapeDown.setDaemon(true);
		isEscapeDown.start();
		
		while (true) {
			Socket socket;
			try {
				socket = server.accept();
			} catch (IOException e) {
				break;
			}
			DataInputStream in = new DataInputStream(socket.getInputStream());
			input = in.readInt();
			
			if (input == 1) {
			  System.out.println("Driving forward");
				motorA.forward();
				motorB.forward();
        Delay.msDelay(500);
        motorA.stop();
        motorB.stop();
			} 
			
			if (input == 2) {
				motorA.backward();
				motorB.backward();
        Delay.msDelay(500);
        motorA.stop();
        motorB.stop();
			}
			
			if (input == 3) {
				motorA.backward();
				motorB.forward();
        Delay.msDelay(500);
        motorA.stop();
        motorB.stop();
			}
			
			if (input == 4) {
				motorA.forward();
				motorB.backward();
        Delay.msDelay(500);
        motorA.stop();
        motorB.stop();
			}
			
			if (input == 5) {
				motorA.stop();
				motorB.stop();
			}
			
			if (input == 6) {
				Sound.setVolume(100);
				Sound.buzz();
				server.close();
				motorA.close();
				motorB.close();
				System.exit(0);
			}
			
			//if the input is 7, then the function for the autonomousMode should be started
			if (input == 7) {
				Robo_6.autonomousMode(motorA, motorB);
				System.out.println("'normale' Steuerung start");
			}
		}
		
		Sound.setVolume(100);
		Sound.buzz();
		server.close();
		motorA.close();
		motorB.close();
		System.exit(0);
	}
}