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
    }
    private String extension;

    public FileExtSignValidatable(String pathname) {
        super(pathname);
        this.extension = this.getExtensionFromPath();
        System.out.println(this.extension);
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

    @Override
    public Optional<Boolean> validate() {
        if (!this.canValidate()) return Optional.ofNullable(null);
        try {
            int i, b, hb;

            for (String signature : extToHexMap.get(this.extension)) {
                FileInputStream in = new FileInputStream(this);
                String s = signature.replaceAll(" ", "");
//                System.out.println(s);
                for (i=0; i<s.length() && (b = in.read()) != -1; i += 2 ) {
                    hb = Integer.parseInt(s.substring(i, i+2), 16);
                    System.out.println(b);
                    System.out.println(hb);
                    if (b != hb)
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
}
