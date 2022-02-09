package com.tarabanov.testapp;

import com.tarabanov.testapp.util.CodeGenerator;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;


public class CodeGeneratorTest {

    private static CodeGenerator codeGenerator;

    @BeforeAll
    public static void createNewCodeGenerator() {
        codeGenerator = CodeGenerator.of(4);
    }

    @Test
    public void newCodeGeneratorShouldHaveLengthFour() {
        assertEquals(4, codeGenerator.generate().length());
    }


    @Test
    public void newCodeGeneratorShouldReturnIllegalArgumentExAndCorrectMessage() {
        codeGenerator = CodeGenerator.of(0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, codeGenerator::generate);

        assertAll(
                () -> assertEquals(IllegalArgumentException.class, exception.getClass()),
                () -> assertEquals("Length of argument 'length' must be greater than zero", exception.getMessage())
        );
    }
}
