package com.banco.servicios;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FechaServicio {

	
	public Calendar convert(String fechaString) {
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		Calendar calendar = Calendar.getInstance();

		try {
			calendar.setTime(formatoDeFecha.parse(fechaString));
		} catch (ParseException e) {
			System.out.println("\n -Error en la fecha "+ e); 
		}

		return calendar;
	}
}
