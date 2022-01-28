package com.tarabanov.testapp.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.text.RandomStringGenerator;

@RequiredArgsConstructor(staticName = "of")
public class CodeGenerator {

    private final int length;

    public String generate() {
        return new RandomStringGenerator.Builder()
                .filteredBy(CodeGenerator::isLatinLetterOrDigit)
                .build().generate(length);
    }

    private static boolean isLatinLetterOrDigit(int codePoint) {

        if ('a' <= codePoint)
            if (codePoint <= 'z')
                return true;

        if ('0' <= codePoint)
            return codePoint <= '9';

        return false;
    }

}
