package com.tinder.PROYECTO.controladores;

import com.tinder.PROYECTO.entidades.Mascota;
import com.tinder.PROYECTO.entidades.Usuario;
import com.tinder.PROYECTO.enumeraciones.Sexo;
import com.tinder.PROYECTO.enumeraciones.Tipo;
import com.tinder.PROYECTO.errores.ErrorServicio;
import com.tinder.PROYECTO.servicios.MascotaServicio;
import com.tinder.PROYECTO.servicios.UsuarioServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
@Controller
@RequestMapping("/mascota")

public class MascotaController {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private MascotaServicio mascotaServicio;

    @PostMapping("/eliminar-perfil")
    public String eliminar(HttpSession session, @RequestParam String id) {
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            mascotaServicio.eliminar(login.getId(), id);

        } catch (ErrorServicio ex) {
            Logger.getLogger(MascotaController.class.getName()).log(Level.SEVERE, null, ex);

        }
        return "redirect:/mascota/mis-mascotas";
    }

    @GetMapping("/mis-mascotas")
    public String misMascotas(HttpSession session, ModelMap model) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";
        }
        List<Mascota> mascotas = mascotaServicio.buscarMascotasPorUsuario(login.getId());
        model.put("mascotas", mascotas);
        return "mascotas";
    }

    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam(required = false) String id, @RequestParam(required = false) String accion, ModelMap model) {

        if (accion == null) {
            accion = "Crear";
        }

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";
        }

        Mascota mascota = new Mascota();
        if (id != null && !id.isEmpty()) {
            try {
                mascota = mascotaServicio.buscarPorId(id);
            } catch (ErrorServicio ex) {
                Logger.getLogger(MascotaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        model.put("perfil", mascota);
        model.put("accion", accion);
        model.put("sexos", Sexo.values());
        model.put("tipos", Tipo.values());
        return "mascota.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/actualizar-perfil")
    public String actualizar(ModelMap modelo, HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam Sexo sexo, @RequestParam Tipo tipo) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");

        if (login == null) {
            return "redirect:/inicio";
        }
        try {
            if (id == null || id.isEmpty()) {

                mascotaServicio.agregarMascota(archivo, login.getId(), nombre, sexo, tipo);

            } else {
                mascotaServicio.modificar(archivo, login.getId(), id, nombre, sexo, tipo);
            }

            return "redirect:/inicio";
        } catch (ErrorServicio ex) {
            Mascota mascota = new Mascota();
            mascota.setId(id);
            mascota.setNombre(nombre);
            mascota.setSexo(sexo);
            mascota.setTipo(tipo);

            modelo.put("accion", "Actualizar");
            modelo.put("sexos", Sexo.values());
            modelo.put("tipos", Tipo.values());
            modelo.put("error", ex.getMessage());
            modelo.put("perfil", ex.getMessage());
            return "perfil.html";
        }
    }

}
