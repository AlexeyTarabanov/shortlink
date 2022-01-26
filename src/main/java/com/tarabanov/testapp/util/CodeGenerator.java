package com.tarabanov.testapp.util;

import org.apache.commons.text.RandomStringGenerator;

public class CodeGenerator {

    private RandomStringGenerator randomStringGenerator;

    public CodeGenerator() {
        RandomStringGenerator.Builder builder = new RandomStringGenerator
                .Builder();
        builder.filteredBy(c -> isLatinLetterOrDigit(c));
        this.randomStringGenerator = builder
                .build();
    }

    public String generate(int length) {
        return randomStringGenerator.generate(length);
    }

    private static boolean isLatinLetterOrDigit(int codePoint) {

        if ('a' <= codePoint)
            if (codePoint <= 'z')
                return true;

        if ('0' <= codePoint)
            if (codePoint <= '9')
                return true;

        return false;
    }

}
