package com.kahu.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    void hashYVerify_funcionaCorrectamente() {
        String hash = PasswordUtil.hash("miPassword123");
        assertNotEquals("miPassword123", hash);
        assertTrue(PasswordUtil.verify("miPassword123", hash));
        assertFalse(PasswordUtil.verify("passwordIncorrecto", hash));
    }
}
