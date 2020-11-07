package com.sweng.astaonline.junit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.sweng.astaonline.shared.Domanda;

class TestDomanda {

	@Test
	void test() {
		String id = "1";
		String user = "mario";
		String domanda = "Come stai?";
		String idOggetto = "2";
		
		Domanda d = new Domanda(id,user,domanda,idOggetto);
		
		String checkId = d.getIdDomanda();
		String checkUser = d.getUserDomanda();
		String checkDomanda = d.getDomanda();
		String checkIdOggetto = d.getIdOggetto();
		
		assertEquals(id,checkId);
		assertEquals(user,checkUser);
		assertEquals(domanda,checkDomanda);
		assertEquals(idOggetto,checkIdOggetto);

	}

}
