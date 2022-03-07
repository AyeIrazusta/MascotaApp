package com.tinder.PROYECTO.controladores;

import com.tinder.PROYECTO.entidades.Mascota;
import com.tinder.PROYECTO.entidades.Usuario;
import com.tinder.PROYECTO.errores.ErrorServicio;
import com.tinder.PROYECTO.servicios.MascotaServicio;
import com.tinder.PROYECTO.servicios.UsuarioServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static org.springframework.web.servlet.function.RequestPredicates.headers;

@Controller
@RequestMapping("/foto")

public class FotoController {

    @Autowired
    private UsuarioServicio usuarioServicio;
       @Autowired
    private MascotaServicio mascotaServicio;

    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoUsuario(@PathVariable String id) {

        try {
            Usuario usuario = usuarioServicio.buscarPorId(id);
            if(usuario.getFoto()==null){
                throw new ErrorServicio("El usuario no tiene foto asignada");
            }
            
            byte[] foto = usuario.getFoto().getContenido();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
     @GetMapping("/mascota/{id}")
    public ResponseEntity<byte[]> fotoMascota(@PathVariable String id) {

        try {
            Mascota mascota = mascotaServicio.buscarPorId(id);
            if(mascota.getFoto()==null){
                throw new ErrorServicio("El usuario no tiene foto asignada");
            }
            
            byte[] foto = mascota.getFoto().getContenido();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
