package com.banco.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.modelos.Cambios;



public interface CambiosRepository extends JpaRepository<Cambios, Long>  {

}
