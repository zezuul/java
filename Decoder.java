import java.util.ArrayList;
import java.util.Arrays;

import javax.lang.model.util.ElementScanner6;

/**
 * Klasa dekodera.
 */
public class Decoder {
	String number =""; //wynik
    String temp = ""; //string pomocniczy

	boolean readsection = false; //sygnal na przejscie do sekcji powtorzen
	Integer readcn = 4; //kontrola nad dl sekcji powotrzen
    String repeat = ""; //przechowujemy ilosc powtorzen
    /**
	 * Metoda pozwalająca na wprowadzanie danych.
	 * @param value dostarczona wartość
	 */
	public void input( byte value ) {
		//System.out.println("in: "+ value);
		if(readsection == false)
		{
			//wartosci z przedzialu (0.9] dopisujemy do pomocniczej zmiennej
			if((value > 0) && (value <= 9))
			{
				temp += value;
			}
			//znak ze bedziemy przechodzic do sekcji powtorzen
			else if(value == 0)
			{
				readsection = true;
			}
		}
		//sekcja powtorzen
		else if((readsection == true) && (readcn > 1))
		{
			if((value >= 0) && (value <= 9))
			{
				repeat += value; //dodajemy do zmiennej na ilosc powtorzen
				readcn -= 1;
			}
			//System.out.println("Sekcja powtorzen chceck");
		}
		//ostatni etap sekcji powtorzen
		else if ((readsection == true) && (readcn <= 1))
		{	
			if((value >= 0) && (value <= 9))
			{	repeat += value;
				//System.out.println("Ostatnia sekcja powtorzen chceck");
				//musimy wyciagnac liczbe ze stringa repeat
				int k = 0;
				int num = 0;
				while(k < repeat.length())
				{
					num *= 10;
					num += repeat.charAt(k++) - '0';
				}
				//System.out.println(num);
				for(int i = 0; i < num;  i++)
				{
					number += temp;
				}
				//przywracamy dane tak aby przy nastepnym wywolaniu input byla sekcja dancych
				readsection = false;
				readcn = 4;
				temp = "";
				repeat ="";
			}
		}
	}
	
	/**
	 * Metoda pozwalająca na pobranie wyniku dekodowania danych.
	 * @return wynik działania
	 */
	public String output() {
		return number; //zwroc wynik
	}
	
	/**
	 * Przywrócenie początkowego stanu obiektu.
	 */
	public void reset() {
		number =""; //czyscimy stringa
		readsection = false; // przywracamy wszystkie mozliwe zmienne do pierwotnych form
		readcn = 4;
		repeat ="";
		temp = "";
	}


	public static String printByte(byte[] value)
	{
		StringBuffer buf = new StringBuffer();
		for (byte val: value)
		{
			buf.append(val);			
		}
		return buf.toString();
	}


    public static boolean runTest(Decoder underTest,  byte[] value, String expected)
	{		
		underTest.reset();		
		for (byte val: value)
		{			
			underTest.input(val);
		}
		String result = underTest.output();
		boolean passed =  result.compareTo(expected) == 0 ;
		if (passed)
		{
			System.out.println("PASS: " + printByte(value) + " : " + expected + " vs. " + result);
		} 
		else
		{
			System.out.println("FAIL: " + printByte(value) + " : " + expected + " vs. " + result);
		}
		return passed;
	}

	public static void selfTest()
	{
		Decoder underTest = new Decoder();
		
		boolean sucess = true;
		//Here add tests
		byte[] t = {1,2,2,1, 0, 0, 0, 0, 6, 3, 0, 0, 0, 0, 12, 0};
		sucess &= runTest(underTest, t, "122112211221122112211221");
		byte[] t2 =  {1,4,5,1,2,0, 0, 0, 0,7};
		sucess &= runTest(underTest, t2, "14512145121451214512145121451214512");
		
		//End of tests
		if (sucess)
		{
			System.out.println("ALL TESTS: PASS");
		} 
		else
		{
			System.out.println("ALL TESTS: FAIL");
		}
	}
	/* 
	public static void main(String[] args)
	{
		selfTest();

	}
	*/
}
