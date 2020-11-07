package com.sweng.astaonline.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.sweng.astaonline.shared.Categoria;

class TestCategoria {

	@Test
	void test() {
		/*
		ArrayList<String> arraylist = new ArrayList<>();
		Categoria c = new Categoria("Scarpe", arraylist);
		String var = c.ottieniSottoCategoria();
		assertEquals(null, var);
		*/
		ArrayList<String> arraylistSotto = new ArrayList<>();
		ArrayList<String> arraylistSotto2 = new ArrayList<>();
		arraylistSotto.add("Borsa");
		arraylistSotto.add("Scarpe");
		arraylistSotto2.add("Da calcio");
		arraylistSotto2.add("Da basket");
		Categoria c = new Categoria("Abbigliamento", arraylistSotto);
		Categoria c2 = new Categoria("Scarpe", arraylistSotto2);
		ArrayList<String> output = c.getSottoCategoria();
		ArrayList<String> arraylistSotto3 = new ArrayList<>();
		arraylistSotto3.add("Borsa");
		arraylistSotto3.add("Scarpe");
		arraylistSotto3.add("Da calcio");
		arraylistSotto3.add("Da basket");
		int var = 0;
		for(int i = 0; i < arraylistSotto.size(); i++) {
			if(var == 0) {
			arraylistSotto.addAll(arraylistSotto2);
			} 
			var++;
			
		}
		assertEquals(arraylistSotto, arraylistSotto3);
	}

}
