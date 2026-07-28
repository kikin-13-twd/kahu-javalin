package com.kahu.service;

import com.kahu.dto.CitaRequestDTO;
import com.kahu.dto.response.CitaResponseDTO;
import com.kahu.entity.Animal;
import com.kahu.entity.Cita;
import com.kahu.entity.Servicio;
import com.kahu.entity.Usuario;
import com.kahu.exception.BusinessException;
import com.kahu.exception.NotFoundException;
import com.kahu.repository.AnimalRepository;
import com.kahu.repository.CitaRepository;
import com.kahu.repository.ServicioRepository;
import com.kahu.repository.UsuarioRepository;
import com.kahu.security.AuthContext;
import com.kahu.security.OwnershipUtil;

import java.util.List;

public class CitaService {
    private final CitaRepository citaRepo;
    private final AnimalRepository animalRepo;
    private final UsuarioRepository usuarioRepo;
    private final ServicioRepository servicioRepo;

    public CitaService(CitaRepository citaRepo, AnimalRepository animalRepo,
                       UsuarioRepository usuarioRepo, ServicioRepository servicioRepo) {
        this.citaRepo = citaRepo;
        this.animalRepo = animalRepo;
        this.usuarioRepo = usuarioRepo;
        this.servicioRepo = servicioRepo;
    }

    public List<CitaResponseDTO> listarTodas() {
        if (OwnershipUtil.isCliente()) {
            return citaRepo.findByDuenioId(AuthContext.requireUserId()).stream()
                    .map(CitaResponseDTO::from).toList();
        }
        return citaRepo.findAll().stream().map(CitaResponseDTO::from).toList();
    }

    public List<CitaResponseDTO> listarPorAnimal(Integer idAnimal) {
        Animal animal = animalRepo.findById(idAnimal)
                .orElseThrow(() -> new NotFoundException("Animal no encontrado"));
        OwnershipUtil.requireAnimalOwner(animal);
        return citaRepo.findByAnimalId(idAnimal).stream().map(CitaResponseDTO::from).toList();
    }

    public List<CitaResponseDTO> listarPorPersonal(Integer idPersonal) {
        return citaRepo.findByPersonalId(idPersonal).stream().map(CitaResponseDTO::from).toList();
    }

    public List<CitaResponseDTO> listarPorEstado(String estado) {
        if (OwnershipUtil.isCliente()) {
            return citaRepo.findByDuenioId(AuthContext.requireUserId()).stream()
                    .filter(c -> estado.equals(c.getEstado()))
                    .map(CitaResponseDTO::from).toList();
        }
        return citaRepo.findByEstado(estado).stream().map(CitaResponseDTO::from).toList();
    }

    public CitaResponseDTO buscarPorId(Integer id) {
        Cita cita = buscarEntidad(id);
        OwnershipUtil.requireCitaOwner(cita);
        return CitaResponseDTO.from(cita);
    }

    public CitaResponseDTO crear(CitaRequestDTO dto) {
        Animal animal = animalRepo.findById(dto.getIdAnimal())
                .orElseThrow(() -> new NotFoundException("Animal no encontrado"));
        OwnershipUtil.requireAnimalOwner(animal);

        Servicio servicio = servicioRepo.findById(dto.getIdServicio())
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado"));

        if (OwnershipUtil.isCliente() && dto.getIdPersonal() != null) {
            throw new BusinessException("Los clientes no pueden asignar personal al crear una cita");
        }

        if (dto.getIdPersonal() != null) {
            boolean ocupado = citaRepo.existsByPersonalAndFechaAndHora(
                    dto.getIdPersonal(), dto.getFecha(), dto.getHora());
            if (ocupado) {
                throw new BusinessException("El veterinario ya tiene una cita en ese horario");
            }
        }

        Cita c = new Cita();
        c.setFecha(dto.getFecha());
        c.setHora(dto.getHora());
        c.setSintomasMotivo(dto.getSintomasMotivo());
        c.setEstado("Pendiente");
        c.setAnimal(animal);
        c.setServicio(servicio);

        if (dto.getIdPersonal() != null) {
            Usuario personal = usuarioRepo.findById(dto.getIdPersonal())
                    .orElseThrow(() -> new NotFoundException("Personal no encontrado"));
            c.setPersonal(personal);
        }

        return CitaResponseDTO.from(citaRepo.save(c));
    }

    public CitaResponseDTO actualizarEstado(Integer id, String estado) {
        Cita c = buscarEntidad(id);
        c.setEstado(estado);
        return CitaResponseDTO.from(citaRepo.save(c));
    }

    public CitaResponseDTO asignarPersonal(Integer idCita, Integer idPersonal) {
        Cita c = buscarEntidad(idCita);
        Usuario personal = usuarioRepo.findById(idPersonal)
                .orElseThrow(() -> new NotFoundException("Personal no encontrado"));
        c.setPersonal(personal);
        return CitaResponseDTO.from(citaRepo.save(c));
    }

    public void eliminar(Integer id) {
        if (!citaRepo.existsById(id)) {
            throw new NotFoundException("Cita no encontrada con id: " + id);
        }
        citaRepo.deleteById(id);
    }

    private Cita buscarEntidad(Integer id) {
        return citaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada con id: " + id));
    }
}
