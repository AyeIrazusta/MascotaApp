
package com.tinder.PROYECTO.repositorios;

import com.tinder.PROYECTO.entidades.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaRepositorio extends JpaRepository<Zona,String>{
    
}
