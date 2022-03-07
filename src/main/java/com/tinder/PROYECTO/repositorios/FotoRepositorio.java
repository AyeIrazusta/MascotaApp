
package com.tinder.PROYECTO.repositorios;

import com.tinder.PROYECTO.entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoRepositorio extends JpaRepository<Foto,String>{
    
}
