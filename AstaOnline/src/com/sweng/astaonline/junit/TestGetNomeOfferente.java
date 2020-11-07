package com.sweng.astaonline.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.sweng.astaonline.client.Vendita;
import com.sweng.astaonline.shared.Offerta;


class TestGetNomeOfferente {


	@Test
	void test() {
		
		Offerta o = new Offerta("mario","iphone",800);
		String var = o.getUsername();
		assertEquals("mario", var.trim());
		
	}

}
