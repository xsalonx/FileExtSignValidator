package com.ld.fielValidation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Optional;


public class FileExtSignValidatable extends File implements Validatable {

    private static HashMap<String, HashSet<String>> extToHexMap;
    static {
        extToHexMap = new HashMap<>();
        extToHexMap.put("gif", new HashSet<>(Arrays.asList(
                "47 49 46 38 37 61",
                "47 49 46 38 39 61")));
        extToHexMap.put("jpg", new HashSet<>(Arrays.asList(
                "FF D8 FF DB",
                "FF D8 FF E0 00 10 4A 46",
                "49 46 00 01",
                "FF D8 FF EE",
                "FF D8 FF E1 ?? ?? 45 78",
                "69 66 00 00")));
        extToHexMap.put("png", new HashSet<>(Arrays.asList(
                "89 50 4E 47 0D 0A 1A 0A")));
    }
    private String extension;

    public FileExtSignValidatable(String pathname) {
        super(pathname);
        this.extension = this.getExtensionFromPath();
    }

    public void printAvailableExtensions() {
        System.out.println(extToHexMap);
    }

    private String getExtensionFromPath() {
        String path = this.getPath();
        int lastDotIdx = path.lastIndexOf('.');
        int lastSlashIdx = path.lastIndexOf('/');
        int lastBackslashIdx = path.lastIndexOf("\\");

        if (lastDotIdx == path.length() - 1
                || lastDotIdx == -1
                || lastDotIdx < Math.max(lastSlashIdx, lastBackslashIdx))
            return null;
        else {
            return path.substring(lastDotIdx + 1);
        }
    }
    
    public String getExtension() {
        return this.extension;
    }
    
    @Override 
    public boolean exists() {
        boolean cond = super.exists();
        if (!cond)
            System.out.println(this.getPath() + " : does not exist");
        return cond;
    }

    @Override
    public boolean canValidate() {
        if (!this.exists()) return false;

        boolean cond = extToHexMap.containsKey(this.extension);
        if (!cond)
            System.out.println(this.getPath() + " : cannot handle this extension");
        return cond;
    }

    private Optional<Boolean> validate__() {
        if (!this.canValidate()) return Optional.ofNullable(null);
        try {
            int i, patternByte, headerByte;

            for (String signature : extToHexMap.get(this.extension)) {
                FileInputStream in = new FileInputStream(this);
                String s = signature.replaceAll(" ", "");
                for (i=0; i<s.length() && (patternByte = in.read()) != -1; i += 2 ) {
                    if (s.charAt(i) == '?')
                        continue;
                    headerByte = Integer.parseInt(s.substring(i, i+2), 16);
                    if (patternByte != headerByte)
                        break;
                }
                if (i == s.length())
                    return Optional.of(true);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.of(false);
    }

    private void tryToFindExtension() {

    }

    @Override
    public Optional<Boolean> validate() {
        Optional<Boolean> res =  this.validate__();
        if (res.isPresent() && res.get() == Boolean.FALSE) {

        }
        return res;
    }
}
