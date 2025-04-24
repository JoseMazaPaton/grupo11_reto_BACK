package vacantes.controller;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vacantes.dto.AltaEmpresaRequestDto;
import vacantes.dto.AltaEmpresaResponseDto;
import vacantes.dto.UsuarioRequestDto;
import vacantes.modelo.entities.Empresa;
import vacantes.modelo.entities.Rol;
import vacantes.modelo.entities.Usuario;
import vacantes.modelo.services.EmpresaServiceImpl;
import vacantes.modelo.services.UsuarioServiceImpl;
import vacantes.utils.PasswordGenerator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
@Tag(name = "Administrador", description = "Gestiones relacionadas con el rol de Administrador (ADMON)")
public class AdminController {
	
	
	@Autowired
	private EmpresaServiceImpl eService;
	
	@Autowired
	private UsuarioServiceImpl uService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper mapper;
	
	
	
	@Operation(
		    summary = "Dar de alta una empresa con su usuario",
		    description = "Crea una nueva empresa junto a su usuario con rol EMPRESA.",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "Datos para registrar una nueva empresa",
		        content = @Content(
		            schema = @Schema(implementation = AltaEmpresaRequestDto.class),
		            examples = {
		                @ExampleObject(
		                    name = "Ejemplo alta empresa",
		                    value = """
		                    {
		                        "email": "empresa@correo.com",
		                        "nombreUsuario": "Carlos",
		                        "apellidoUsuario": "García",
		                        "nombreEmpresa": "Tech SL",
		                        "cif": "B12345678",
		                        "direccionFiscal": "Calle Mayor 1",
		                        "pais": "España"
		                    }
		                    """
		                )
		            }
		        )
		    ),
		    responses = {
		        @ApiResponse(
		            responseCode = "201",
		            description = "Empresa creada correctamente",
		            content = @Content(
		                schema = @Schema(implementation = AltaEmpresaResponseDto.class),
		                examples = @ExampleObject(
		                    name = "Respuesta OK",
		                    value = """
		                    {
		                        "email": "empresa@correo.com",
		                        "password": "XyZ123@!",
		                        "rol": "EMPRESA"
		                    }
		                    """
		                )
		            )
		        )
		    }
		)
	@Transactional  // Si falla empresa, se revierte el usuario
	@PostMapping("/alta/empresa")
	public ResponseEntity<?> altaEmpresa(@RequestBody AltaEmpresaRequestDto altaEmpresaDto) {
		
		if (uService.buscarPorEmail(altaEmpresaDto.getEmail()) != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Ese email ya esta en uso, usa otro");
		}
		
		Usuario usuario = new Usuario();
		
		usuario.setEmail(altaEmpresaDto.getEmail());
		usuario.setNombre(altaEmpresaDto.getNombreUsuario());
		usuario.setApellidos(altaEmpresaDto.getApellidoUsuario());
		
		usuario.setEnabled(1);
		usuario.setFechaRegistro(new java.sql.Date(System.currentTimeMillis()));
		usuario.setRol(usuario.getRol().EMPRESA);
		
		String password = PasswordGenerator.generateRandomPassword();
		
		usuario.setPassword(passwordEncoder.encode(password));
		
		uService.insertUno(usuario);
			
		Empresa empresa = new Empresa();
			
		empresa.setIdEmpresa(0);
		empresa.setCif(altaEmpresaDto.getCif());
		empresa.setNombreEmpresa(altaEmpresaDto.getNombreEmpresa());
		empresa.setDireccionFiscal(altaEmpresaDto.getDireccionFiscal());
		empresa.setPais(altaEmpresaDto.getPais());
		empresa.setUsuario(usuario);
		
		eService.insertUno(empresa);
		
		AltaEmpresaResponseDto response = new AltaEmpresaResponseDto();
		
		response.setEmail(altaEmpresaDto.getEmail());
		response.setPassword(password);
		response.setRol(usuario.getRol().EMPRESA.toString());
		
		return new ResponseEntity<AltaEmpresaResponseDto>(response, HttpStatus.CREATED);
		

		
	}
	
	
	@Operation(
		    summary = "Deshabilitar usuario por email",
		    description = "Desactiva un usuario existente poniéndolo como deshabilitado (enabled = 0).",
		    parameters = {
		        @Parameter(
		            name = "email",
		            description = "Email del usuario a deshabilitar",
		            required = true,
		            example = "usuario@ejemplo.com"
		        )
		    },
		    responses = {
		        @ApiResponse(responseCode = "200", description = "Usuario deshabilitado correctamente"),
		        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
		        @ApiResponse(responseCode = "400", description = "Problema con la base de datos"),
		        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
		    }
		)
	@PutMapping("/deshabilitar/{email}")
	public ResponseEntity<Map<String, String>> baja(@PathVariable String email) {
	    Usuario usuario = uService.buscarUno(email);

	    if (usuario != null) {
	        usuario.setEnabled(0);
	        switch (uService.updateUno(usuario)) {
	            case 1:
	                return ResponseEntity.ok(Map.of("message", "✅ Usuario deshabilitado correctamente"));
	            case 0:
	                return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                        .body(Map.of("message", "❌ Usuario no existe"));
	            case -1:
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                        .body(Map.of("message", "⚠️ Problema en la base de datos"));
	            default:
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                        .body(Map.of("message", "Error desconocido"));
	        }
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(Map.of("message", "❌ Este usuario no existe"));
	    }
	}
	
	
	@Operation(
		    summary = "Habilitar un usuario",
		    description = "Permite volver a habilitar un usuario previamente deshabilitado (enabled = 1) a partir de su email.",
		    parameters = {
		        @Parameter(
		            name = "email",
		            description = "Email del usuario a habilitar",
		            required = true,
		            example = "usuario@correo.com"
		        )
		    },
		    responses = {
		        @ApiResponse(
		            responseCode = "200",
		            description = "Usuario habilitado correctamente",
		            content = @Content(
		                mediaType = "application/json",
		                examples = @ExampleObject(
		                    name = "Respuesta exitosa",
		                    value = """
		                    {
		                      "message": "✅ Usuario habilitado correctamente"
		                    }
		                    """
		                )
		            )
		        ),
		        @ApiResponse(
		            responseCode = "404",
		            description = "Usuario no encontrado",
		            content = @Content(
		                mediaType = "application/json",
		                examples = @ExampleObject(
		                    name = "Usuario no encontrado",
		                    value = """
		                    {
		                      "message": "❌ Usuario no encontrado"
		                    }
		                    """
		                )
		            )
		        ),
		        @ApiResponse(
		            responseCode = "400",
		            description = "Error en la base de datos",
		            content = @Content(
		                mediaType = "application/json",
		                examples = @ExampleObject(
		                    name = "Error BBDD",
		                    value = """
		                    {
		                      "message": "⚠️ Error en la base de datos"
		                    }
		                    """
		                )
		            )
		        ),
		        @ApiResponse(
		            responseCode = "500",
		            description = "Error interno del servidor",
		            content = @Content(
		                mediaType = "application/json",
		                examples = @ExampleObject(
		                    name = "Error desconocido",
		                    value = """
		                    {
		                      "message": "Error desconocido"
		                    }
		                    """
		                )
		            )
		        )
		    }
		)	
	@PutMapping("/habilitar/{email}")
	public ResponseEntity<Map<String, String>> habilitar(@PathVariable String email) {
	    Usuario usuario = uService.buscarUno(email);

	    if (usuario != null) {
	        usuario.setEnabled(1);
	        switch (uService.updateUno(usuario)) {
	            case 1:
	                return ResponseEntity.ok(Map.of("message", "✅ Usuario habilitado correctamente"));
	            case 0:
	                return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                        .body(Map.of("message", "❌ Usuario no encontrado"));
	            case -1:
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                        .body(Map.of("message", "⚠️ Error en la base de datos"));
	            default:
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                        .body(Map.of("message", "Error desconocido"));
	        }
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(Map.of("message", "❌ Este usuario no existe"));
	    }
	}
	
	
	@Operation(
		    summary = "Dar de alta un administrador",
		    description = "Crea un nuevo usuario con rol ADMINISTRADOR.",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "Datos del nuevo administrador",
		        content = @Content(
		            schema = @Schema(implementation = UsuarioRequestDto.class),
		            examples = @ExampleObject(
		                name = "Ejemplo alta admin",
		                value = """
		                {
		                    "email": "admin@correo.com",
		                    "nombre": "Laura",
		                    "apellidos": "Pérez González",
		                    "password": "Admin1234"
		                }
		                """
		            )
		        )
		    ),
		    responses = {
		        @ApiResponse(responseCode = "201", description = "Administrador creado correctamente"),
		        @ApiResponse(responseCode = "409", description = "Email ya está en uso"),
		        @ApiResponse(responseCode = "400", description = "Error al crear el administrador")
		    }
		)
	@Transactional  // Si falla, se revierte el usuario
	@PostMapping("/add")
	public ResponseEntity<Map<String, String>> altaAdmon(@RequestBody UsuarioRequestDto nuevoAdmin) {
	    
	    if (uService.buscarPorEmail(nuevoAdmin.getEmail()) != null) {
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body(Map.of("message", "Ese email ya está en uso, usa otro"));
	    }

	    Usuario nuevoUsuario = mapper.map(nuevoAdmin, Usuario.class);
	    nuevoUsuario.setRol(Rol.ADMON);
	    nuevoUsuario.setFechaRegistro(new java.sql.Date(System.currentTimeMillis()));

	    if (uService.insertUno(nuevoUsuario) != null) {
	        return ResponseEntity.status(HttpStatus.CREATED)
	                .body(Map.of("message", "✅ Administrador creado correctamente"));
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(Map.of("message", "❌ El usuario ya existe o hubo un error"));
	    }
	}
	
}
