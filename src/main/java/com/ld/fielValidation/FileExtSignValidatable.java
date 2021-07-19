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
        extToHexMap.put("txt", new HashSet<>(Arrays.asList(
                "EF BB BF",
                "FF FE",
                "FE FF",
                "FF FE 00 00",
                "00 00 FE FF",
                "0E FE FF"
        )));
        extToHexMap.put("bin", new HashSet<>(Arrays.asList(
                "53 50 30 31")));
        extToHexMap.put("exe", new HashSet<>(Arrays.asList(
                "5A 4D")));
        extToHexMap.put("sh", new HashSet<>(Arrays.asList(
                "23 21")));
        extToHexMap.put("mp3", new HashSet<>(Arrays.asList(
                "FF FB",
                "FF F3",
                "FF F2")));
        extToHexMap.put("class", new HashSet<>(Arrays.asList(
                "CA FE BA BE")));
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
    
    public boolean exists(boolean showMessage) {
        boolean cond = super.exists();
        if (!cond && showMessage)
            System.out.println(this.getPath() + " : does not exist");
        return cond;
    }
    @Override
    public boolean exists() {
        return this.exists(true);
    }

    @Override
    public boolean canValidate() {
        if (!this.exists()) return false;

        boolean cond = extToHexMap.containsKey(this.extension);
        if (!cond)
            System.out.println(this.getPath() + " : cannot handle this extension");
        return cond;
    }

    private Optional<Boolean> analyzeForSignature(String signature) throws IOException {
        int i, patternByte, headerByte;

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

        return Optional.of(false);
    }

    private Optional<Boolean> validate__() {
        if (!this.canValidate()) return Optional.ofNullable(null);
        try {

            for (String signature : extToHexMap.get(this.extension)) {
                Optional<Boolean> res = this.analyzeForSignature(signature);
                if (res.get() == Boolean.TRUE)
                    return res;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.of(false);
    }

    private Optional<Boolean> tryToFindExtension() {
        if (!this.exists(false))
            return Optional.ofNullable(null);
        for (String ext : extToHexMap.keySet()) {
            for (String signature : extToHexMap.get(ext)) {
                try {
                    Optional<Boolean> res = this.analyzeForSignature(signature);
                    if (res.get() == Boolean.TRUE) {
                        this.extension = ext;
                        return Optional.of(true);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Optional.of(false);
    }

    @Override
    public Optional<Boolean> validate() {
        Optional<Boolean> res =  this.validate__();
        if (!res.isPresent() || res.get() == Boolean.FALSE) {
            res = this.tryToFindExtension();
        }
        return res;
    }
}
