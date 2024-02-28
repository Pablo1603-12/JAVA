package com.banco.servicios;

/**
 * Interface donde se declaran los metodos necesarios para el tratamiento de
 * imagenes.
 */
public interface IFotoServicio {

	public String convertirAbase64(byte[] datosImg);

	public byte[] convertirAarrayBytes(String imgBase64);

	public byte[] cargarFotoPredeterminada();
}
