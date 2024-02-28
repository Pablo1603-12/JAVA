package com.banco.modelos;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "cambios", schema = "esqbanco")
public class Cambios {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "titulo")
	private String titulo;
	@Column(name = "description")
	private String description;
	@Column(name = "publicado")
	private boolean publicado;

	public Cambios() {
	}

	public Cambios(String titulo, String description, boolean publicado) {
		this.titulo = titulo;
		this.description = description;
		this.publicado = publicado;
	}
	

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, orphanRemoval = true)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@JoinColumn(name = "cambios_id")
	private Set<Cuenta> cuentas = new HashSet<Cuenta>();


	public long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPublicado() {
		return publicado;
	}

	public void setPublicado(boolean isPublicado) {
		this.publicado = isPublicado;
	}

	@Override
	public String toString() {
		return "Cambios [id=" + id + ", titulo=" + titulo + ", desc=" + description + ", publicado=" + publicado + "]";
	}
}