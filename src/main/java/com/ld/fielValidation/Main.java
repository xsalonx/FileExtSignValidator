package com.ld.fielValidation;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("No input");
            return;
        }
        for (String path : args) {
            FileExtSignValidatable file = new FileExtSignValidatable(path);
            Optional<Boolean> res = file.validate();

            if (res.isPresent()) {
                if (res.get() == Boolean.TRUE)
                    System.out.println(file.getPath() + " is " + file.getExtension() + " file");
                else if (res.get() == Boolean.FALSE)
                    System.out.println(file.getPath() + ": extension lies, it is actually " + file.getExtension() + " file");
            }
        }
    }
}
