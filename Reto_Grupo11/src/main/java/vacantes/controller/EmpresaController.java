package vacantes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import vacantes.modelo.entities.Empresa;
import vacantes.modelo.entities.Usuario;
import vacantes.modelo.services.EmpresaService;
import vacantes.modelo.services.UsuarioService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/empresas/")
@Tag(name = "Empresa", description = "API para las operaciones de empresa")
public class EmpresaController {
    
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("GetAllEmpresa")
    public List<Empresa> listarEmpresas() {
        return empresaService.buscarTodos();
    }

    @GetMapping("GetEmpresaById/{id}")
    public ResponseEntity<Empresa> obtenerEmpresa(@PathVariable int id) {
        Empresa empresa = empresaService.buscarUno(id);
        if (empresa != null) {
            return ResponseEntity.ok(empresa); 
        }
        return ResponseEntity.notFound().build(); 
    }

    @PostMapping("/CreateEmpresa")
    public ResponseEntity<Empresa> crearEmpresa(@RequestBody Empresa empresa) {
   
        Empresa nuevaEmpresa = empresaService.insertUno(empresa);

       
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEmpresa);
    }


    @PutMapping("UpdateEmpresa/{id}")
    public ResponseEntity<Empresa> actualizarEmpresa(@PathVariable int id, @RequestBody Empresa empresaDetalles) {
        
        Empresa empresa = empresaService.buscarUno(id);
        if (empresa != null) {
            
            empresa.setCif(empresaDetalles.getCif());
            empresa.setNombreEmpresa(empresaDetalles.getNombreEmpresa());
            empresa.setDireccionFiscal(empresaDetalles.getDireccionFiscal());
            empresa.setPais(empresaDetalles.getPais());
            
            String email = empresaDetalles.getUsuario().getEmail();
            
            Usuario usuario = usuarioService.buscarPorEmail(email);
            
            if (usuario != null) {

                empresa.setUsuario(usuario);

                int result = empresaService.updateUno(empresa);
                
                if (result == 1) {
                    return ResponseEntity.ok(empresa); 
                } else {
                    return ResponseEntity.notFound().build(); 
                }
            } else {
              
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
        
        return ResponseEntity.notFound().build();
    }



    @DeleteMapping("DeleteEmpresa/{id}")
    public ResponseEntity<Void> eliminarEmpresa(@PathVariable int id) {
        if (empresaService.deleteUno(id) == 1) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.notFound().build(); 
    }
}
