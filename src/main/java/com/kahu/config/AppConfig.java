package com.kahu.config;

import com.kahu.repository.*;
import com.kahu.security.JwtUtil;
import com.kahu.service.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Contenedor de dependencias manual.
 * Reemplaza la inyeccion de dependencias de Spring.
 * Crea una sola instancia de cada repositorio y servicio (patron Singleton).
 */
@Getter
public class AppConfig {

    private final EntityManagerFactory emf;
    private final JwtUtil jwtUtil;

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final EspecieRepository especieRepository;
    private final RazaRepository razaRepository;
    private final AnimalRepository animalRepository;
    private final PublicacionAdopcionRepository publicacionAdopcionRepository;
    private final SolicitudAdopcionRepository solicitudAdopcionRepository;
    private final TipoConsultaRepository tipoConsultaRepository;
    private final ServicioRepository servicioRepository;
    private final CitaRepository citaRepository;
    private final ReporteClinicoRepository reporteClinicoRepository;
    private final CatalogoVacunaRepository catalogoVacunaRepository;
    private final VacunacionRepository vacunacionRepository;

    private final RolService rolService;
    private final UsuarioService usuarioService;
    private final EspecieService especieService;
    private final RazaService razaService;
    private final AnimalService animalService;
    private final PublicacionAdopcionService publicacionAdopcionService;
    private final SolicitudAdopcionService solicitudAdopcionService;
    private final TipoConsultaService tipoConsultaService;
    private final ServicioService servicioService;
    private final CitaService citaService;
    private final ReporteClinicoService reporteClinicoService;
    private final CatalogoVacunaService catalogoVacunaService;
    private final VacunacionService vacunacionService;

    private final AuthService authService;

    public AppConfig() {
        EnvConfig env = EnvConfig.get();

        this.jwtUtil = new JwtUtil(env.getJwtSecret(), env.getJwtExpirationHours());

        Map<String, String> props = new HashMap<>();
        props.put("jakarta.persistence.jdbc.url", env.getDbUrl());
        props.put("jakarta.persistence.jdbc.user", env.getDbUser());
        props.put("jakarta.persistence.jdbc.password", env.getDbPassword());
        props.put("hibernate.show_sql", String.valueOf(env.isShowSql()));

        this.emf = Persistence.createEntityManagerFactory("kahuPU", props);

        this.rolRepository                 = new RolRepository(emf);
        this.usuarioRepository             = new UsuarioRepository(emf);
        this.especieRepository             = new EspecieRepository(emf);
        this.razaRepository                = new RazaRepository(emf);
        this.animalRepository              = new AnimalRepository(emf);
        this.publicacionAdopcionRepository = new PublicacionAdopcionRepository(emf);
        this.solicitudAdopcionRepository   = new SolicitudAdopcionRepository(emf);
        this.tipoConsultaRepository        = new TipoConsultaRepository(emf);
        this.servicioRepository            = new ServicioRepository(emf);
        this.citaRepository                = new CitaRepository(emf);
        this.reporteClinicoRepository      = new ReporteClinicoRepository(emf);
        this.catalogoVacunaRepository      = new CatalogoVacunaRepository(emf);
        this.vacunacionRepository          = new VacunacionRepository(emf);

        this.rolService                = new RolService(rolRepository);
        this.usuarioService            = new UsuarioService(usuarioRepository, rolRepository);
        this.especieService            = new EspecieService(especieRepository);
        this.razaService               = new RazaService(razaRepository, especieRepository);
        this.animalService             = new AnimalService(animalRepository, razaRepository, usuarioRepository);
        this.tipoConsultaService       = new TipoConsultaService(tipoConsultaRepository, rolRepository);
        this.servicioService           = new ServicioService(servicioRepository, tipoConsultaRepository);
        this.citaService               = new CitaService(citaRepository, animalRepository, usuarioRepository, servicioRepository);
        this.reporteClinicoService     = new ReporteClinicoService(reporteClinicoRepository, citaRepository);
        this.catalogoVacunaService     = new CatalogoVacunaService(catalogoVacunaRepository);
        this.vacunacionService         = new VacunacionService(vacunacionRepository, catalogoVacunaRepository, citaRepository);
        this.publicacionAdopcionService = new PublicacionAdopcionService(publicacionAdopcionRepository, animalRepository);
        this.solicitudAdopcionService   = new SolicitudAdopcionService(solicitudAdopcionRepository, usuarioRepository, publicacionAdopcionRepository);

        this.authService = new AuthService(usuarioRepository, rolRepository, jwtUtil);
    }

    public void shutdown() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
