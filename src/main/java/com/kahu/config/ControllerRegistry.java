package com.kahu.config;

import com.kahu.controller.*;
import io.javalin.Javalin;

public final class ControllerRegistry {

    private ControllerRegistry() {}

    public static void register(Javalin app, AppConfig ctx) {
        new AuthController(ctx.getAuthService()).registerRoutes(app);
        new RolController(ctx.getRolService()).registerRoutes(app);
        new UsuarioController(ctx.getUsuarioService()).registerRoutes(app);
        new EspecieController(ctx.getEspecieService()).registerRoutes(app);
        new RazaController(ctx.getRazaService()).registerRoutes(app);
        new AnimalController(ctx.getAnimalService()).registerRoutes(app);
        new TipoConsultaController(ctx.getTipoConsultaService()).registerRoutes(app);
        new ServicioController(ctx.getServicioService()).registerRoutes(app);
        new CitaController(ctx.getCitaService()).registerRoutes(app);
        new ReporteClinicoController(ctx.getReporteClinicoService()).registerRoutes(app);
        new CatalogoVacunaController(ctx.getCatalogoVacunaService()).registerRoutes(app);
        new VacunacionController(ctx.getVacunacionService()).registerRoutes(app);
        new PublicacionAdopcionController(ctx.getPublicacionAdopcionService()).registerRoutes(app);
        new SolicitudAdopcionController(ctx.getSolicitudAdopcionService()).registerRoutes(app);
    }
}
