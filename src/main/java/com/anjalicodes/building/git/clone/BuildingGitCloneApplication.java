package com.anjalicodes.building.git.clone;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class BuildingGitCloneApplication {

	public static void main(String[] args) {
		System.out.println("Logs from your program will appear here!");
		final String command = args[0];

		switch (command) {
			case "init" : {
				final File root = new File("output/.git");
				new File(root, "objects").mkdirs();
				new File(root, "refs").mkdirs();
				final File head = new File(root, "HEAD");

				try {
					head.createNewFile();
					Files.write(head.toPath(), "ref: refs/heads/main\n".getBytes());
					System.out.println("Initialized git directory");
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			default : System.out.println("Unknown git command: " + command);
		}
	}

}
