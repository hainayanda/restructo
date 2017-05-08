package main;

import java.io.File;
import java.io.IOException;

import restructo.robot.context.WorkspaceContext;

public class Main {

	public static void main(String[] args) {
		WorkspaceContext ctx;
		try {
			ctx = WorkspaceContext.parseWorkspace(new File("C:\\Users\\nayan\\OneDrive\\Docs\\Works\\KMKOnline\\automata\\desktop-bintang"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Success");
	}
	
}
