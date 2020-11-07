package com.sweng.astaonline.junit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.sweng.astaonline.shared.FieldVerifier;

class TestValidNumber {

	@Test
	void test() {
		FieldVerifier fv = new FieldVerifier();
		boolean b = fv.isValidNumber("300");
		assertEquals(true, b);
	}

}
