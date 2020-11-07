package com.sweng.astaonline.junit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.sweng.astaonline.shared.Offerta;

class TestGetOggettoOfferta {

	@Test
	void test() {
		Offerta o = new Offerta("mario","iphone",800, "iphone");
		String var = o.getNomeOggetto();
		assertEquals("iphone", var.trim());
	}

}
