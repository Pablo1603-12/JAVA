package com.banco.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.modelos.Movimiento;


public interface MovimientoRepository extends JpaRepository<Movimiento, Long>  {

}
