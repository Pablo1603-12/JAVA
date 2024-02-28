package com.banco.modelos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Movimiento", schema = "esqbanco")
public class Movimiento  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long movimientoId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo")
    private EMovimiento tipo;

	@Column(name = "saldo")
    private BigDecimal saldo;
	
	@Column(name = "valor")
    private BigDecimal valor;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha")
	private Date fecha;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="cuenta_Id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@JsonIgnore
	private Cuenta cuenta;

	@Override
	public String toString() {
		return "Movimiento [movimientoId=" + movimientoId + ", tipo=" + tipo + ", saldo=" + saldo + ", valor=" + valor
				+ ", fecha=" + fecha + ", cuenta=" + cuenta + "]";
	}

	public long getMovimientoId() {
		return movimientoId;
	}

	public void setMovimientoId(long movimientoId) {
		this.movimientoId = movimientoId;
	}

	public EMovimiento getTipo() {
		return tipo;
	}

	public void setTipo(EMovimiento tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	

}
