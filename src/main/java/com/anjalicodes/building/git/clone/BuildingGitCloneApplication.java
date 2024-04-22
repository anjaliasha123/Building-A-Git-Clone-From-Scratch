package com.anjalicodes.building.git.clone;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class BuildingGitCloneApplication {
	private static final String GIT_DIR = Paths.get("").toAbsolutePath().toString();
	private static String computeFileHash(File file) {
		String sha1 = null;
		MessageDigest digest;
		try{
			digest = MessageDigest.getInstance("SHA-1");
			FileInputStream inputStream = new FileInputStream(file);
			byte[] data = new byte[(int)file.length()];
			inputStream.read(data);
			byte[] buffer = new byte[8192];
			int read = 0;
			while((read = inputStream.read(buffer)) != -1) digest.update(buffer, 0, read);
			inputStream.close();
			byte[] hashedBytes = digest.digest();
			StringBuilder hashedString = new StringBuilder();
			for(byte b: hashedBytes) hashedString.append(String.format("%02x", b));

			//Store the file under ".ugit/objects/{the SHA-1 hash}"
			String outPath = Paths.get("").toAbsolutePath().toString()+"\\.mygit\\objects\\";
			File rootFile = new File(".mygit/objects");
			new File(rootFile, hashedString.substring(0,2)).mkdirs();
			File newFile = new File(".mygit/objects/"+hashedString.substring(0,2), hashedString.substring(2));
			newFile.createNewFile();
//					System.out.println(newFile.getPath());
			Files.write(Path.of(newFile.getPath()), data);

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
	private static byte[] readHashedFile(String path, String oid){
		try{
			System.out.println("path: "+path+oid);
			File file = new File(path+oid);
			FileInputStream inputStream = new FileInputStream(file);
			byte[] data = new byte[(int)file.length()];
			inputStream.read(data);
			inputStream.close();
			return data;
		} catch (FileNotFoundException e) {
			System.out.println("File not found in location "+path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return new byte[0];
	}
	private static void catFile(String oid) {
		System.out.flush();
		try{
			byte[] objData = getObject(oid);
//			getObject(oid);
			System.out.write(objData);
			System.out.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static byte[] getObject(String oid) throws IOException{
		String path = GIT_DIR+"\\.mygit\\objects\\"+oid.substring(0,2)+"\\"+oid
				.substring(2);
//		System.out.println("path: "+path);
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int)file.length()];
		fis.read(data);
		fis.close();
		return data;
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
					//read the contents of the file and hash it
					String oid = computeFileHash(new File(path));
					System.out.println("OID: "+oid);
				}catch(ArrayIndexOutOfBoundsException e){
					System.out.println("Please enter the path to the file");
				}
			}
//			da39a3ee5e6b4b0d3255bfef95601890afd80709
			break;
			case "cat-file":{
				String oid = args[1];
				catFile(oid);
			}
			break;
			default : System.out.println("Unknown git command: " + command);
		}
	}

}
