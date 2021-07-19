package com.ld.fielValidation;

import java.io.File;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Arrays;


public class FileExtSignValidatable extends File implements Validatable {

    private static HashMap<String, HashSet<String>> extToHexMap;

    static {
        extToHexMap = new HashMap<>();
        extToHexMap.put("gif", new HashSet<>(Arrays.asList(
                "47 49 46 38 37 61",
                "47 49 46 38 39 61")));
    }

    public FileExtSignValidatable(String pathname) {
        super(pathname);
    }

    public void printAvailableExtensions() {
        System.out.println(extToHexMap);
    }

    public String getExtension() {
        String path = this.getPath();
        int lastDotIdx = path.lastIndexOf('.');
        int lastSlashIdx = path.lastIndexOf('/');
        int lastBackslashIdx = path.lastIndexOf("\\");

        if (lastDotIdx == path.length() - 1
                || lastDotIdx == -1
                || lastBackslashIdx < Math.max(lastSlashIdx, lastBackslashIdx))
            return null;
        else {
            return path.substring(lastDotIdx + 1);
        }
    }

    @Override
    public boolean validate() {
        return false;
    }
}
