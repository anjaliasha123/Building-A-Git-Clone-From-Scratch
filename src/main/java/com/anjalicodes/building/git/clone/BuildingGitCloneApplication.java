package com.anjalicodes.building.git.clone;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class BuildingGitCloneApplication {
	private static String computeFileHash(File file) {
		String sha1 = null;
		MessageDigest digest;
		try{
			digest = MessageDigest.getInstance("SHA-1");
			FileInputStream inputStream = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int read = 0;
			while((read = inputStream.read(buffer)) != -1) digest.update(buffer, 0, read);
			inputStream.close();
			byte[] hashedBytes = digest.digest();
			StringBuilder hashedString = new StringBuilder();
			for(byte b: hashedBytes) hashedString.append(String.format("%02x", b));
			return hashedString.toString();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("SHA-1 algorithm instance not found");
		} catch (FileNotFoundException e) {
			System.out.println("File not found: "+file.getName());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sha1;
	}

	public static void main(String[] args) {
		System.out.println("Logs from your program will appear here!");
		final String command = args[0];

		switch (command) {
			case "init" : {
				final File root = new File(".mygit");
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
			break;
			case "hash-object":{
				try{
					//get the path of the file that is to be stored
					//read it from command line
					String path = Paths.get("").toAbsolutePath()
							.toString()+"\\"+args[1];
//					System.out.println("Path is: "+path);
					//read the contents of the file and hash it
					String oid = computeFileHash(new File(path));

					//Store the file under ".ugit/objects/{the SHA-1 hash}"
					String outPath = Paths.get("").toAbsolutePath().toString()+"\\.mygit\\objects\\";
					File outFile = new File(outPath+oid);
					FileOutputStream outputStream = new FileOutputStream(outFile);
					outputStream.write(oid.getBytes());
					outputStream.close();
					System.out.println("OID: "+oid);
				}catch(ArrayIndexOutOfBoundsException e){
					System.out.println("Please enter the path to the file");
				} catch (FileNotFoundException e) {
					System.out.println("Did not find the file in path \n"+e.getMessage());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			break;
			default : System.out.println("Unknown git command: " + command);
		}
	}

}
