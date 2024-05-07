package com.anjalicodes.building.git.clone;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Base {
    public void writeTree(String directory){
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directory))){
            for(Path path: stream){
//                if it is a regular file -> write the file to the object stor
//                if it is a directory
                if(isIgnored(path.toString())) continue;
                if(Files.isDirectory(path)) writeTree(path.toString());
                else if(Files.isRegularFile(path)){
                    System.out.println(path.toString());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean isIgnored(String path){
        String[] components = path.split("/");
        return Arrays.asList(components).contains(".mygit");
    }
}
