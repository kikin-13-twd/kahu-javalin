package com.kahu.service;

import com.kahu.dto.SolicitudAdopcionDTO;
import com.kahu.dto.response.SolicitudAdopcionResponseDTO;
import com.kahu.entity.PublicacionAdopcion;
import com.kahu.entity.SolicitudAdopcion;
import com.kahu.entity.Usuario;
import com.kahu.exception.BusinessException;
import com.kahu.exception.NotFoundException;
import com.kahu.repository.PublicacionAdopcionRepository;
import com.kahu.repository.SolicitudAdopcionRepository;
import com.kahu.repository.UsuarioRepository;
import com.kahu.security.AuthContext;
import com.kahu.security.OwnershipUtil;

import java.time.LocalDate;
import java.util.List;

public class SolicitudAdopcionService {
    private final SolicitudAdopcionRepository solRepo;
    private final UsuarioRepository usuarioRepo;
    private final PublicacionAdopcionRepository pubRepo;

    public SolicitudAdopcionService(SolicitudAdopcionRepository solRepo,
                                    UsuarioRepository usuarioRepo,
                                    PublicacionAdopcionRepository pubRepo) {
        this.solRepo = solRepo;
        this.usuarioRepo = usuarioRepo;
        this.pubRepo = pubRepo;
    }

    public List<SolicitudAdopcionResponseDTO> listarTodas() {
        if (OwnershipUtil.isCliente()) {
            return solRepo.findByUsuarioId(AuthContext.requireUserId()).stream()
                    .map(SolicitudAdopcionResponseDTO::from).toList();
        }
        return solRepo.findAll().stream().map(SolicitudAdopcionResponseDTO::from).toList();
    }

    public List<SolicitudAdopcionResponseDTO> listarPorUsuario(Integer id) {
        OwnershipUtil.requireSameUser(id);
        return solRepo.findByUsuarioId(id).stream().map(SolicitudAdopcionResponseDTO::from).toList();
    }

    public List<SolicitudAdopcionResponseDTO> listarPorPublicacion(Integer id) {
        return solRepo.findByPublicacionId(id).stream().map(SolicitudAdopcionResponseDTO::from).toList();
    }

    public SolicitudAdopcionResponseDTO buscarPorId(Integer id) {
        SolicitudAdopcion s = buscarEntidad(id);
        if (OwnershipUtil.isCliente()) {
            OwnershipUtil.requireSameUser(s.getUsuarioInteresado().getIdUsuario());
        }
        return SolicitudAdopcionResponseDTO.from(s);
    }

    public SolicitudAdopcionResponseDTO crear(SolicitudAdopcionDTO dto) {
        Integer idUsuario = OwnershipUtil.isCliente()
                ? AuthContext.requireUserId()
                : dto.getIdUsuarioInteresado();

        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        PublicacionAdopcion pub = pubRepo.findById(dto.getIdPublicacion())
                .orElseThrow(() -> new NotFoundException("Publicacion no encontrada"));

        if (!"Disponible".equals(pub.getEstado())) {
            throw new BusinessException("Esta publicacion ya no esta disponible");
        }

        SolicitudAdopcion s = new SolicitudAdopcion();
        s.setFechaSolicitud(LocalDate.now());
        s.setEstado("Pendiente");
        s.setUsuarioInteresado(usuario);
        s.setPublicacion(pub);
        return SolicitudAdopcionResponseDTO.from(solRepo.save(s));
    }

    public SolicitudAdopcionResponseDTO actualizarEstado(Integer id, String estado) {
        SolicitudAdopcion s = buscarEntidad(id);
        s.setEstado(estado);
        if ("Aprobada".equals(estado)) {
            PublicacionAdopcion pub = s.getPublicacion();
            pub.setEstado("Adoptado");
            pubRepo.save(pub);
        }
        return SolicitudAdopcionResponseDTO.from(solRepo.save(s));
    }

    private SolicitudAdopcion buscarEntidad(Integer id) {
        return solRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada con id: " + id));
    }
}
