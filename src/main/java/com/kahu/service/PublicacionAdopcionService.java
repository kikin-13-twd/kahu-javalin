package com.kahu.service;

import com.kahu.dto.PublicacionAdopcionDTO;
import com.kahu.dto.response.PublicacionAdopcionResponseDTO;
import com.kahu.entity.Animal;
import com.kahu.entity.PublicacionAdopcion;
import com.kahu.exception.NotFoundException;
import com.kahu.repository.AnimalRepository;
import com.kahu.repository.PublicacionAdopcionRepository;

import java.util.List;

public class PublicacionAdopcionService {
    private final PublicacionAdopcionRepository pubRepo;
    private final AnimalRepository animalRepo;

    public PublicacionAdopcionService(PublicacionAdopcionRepository pubRepo, AnimalRepository animalRepo) {
        this.pubRepo = pubRepo;
        this.animalRepo = animalRepo;
    }

    public List<PublicacionAdopcionResponseDTO> listarTodas() {
        return pubRepo.findAll().stream().map(PublicacionAdopcionResponseDTO::from).toList();
    }

    public List<PublicacionAdopcionResponseDTO> listarDisponibles() {
        return pubRepo.findByEstado("Disponible").stream().map(PublicacionAdopcionResponseDTO::from).toList();
    }

    public PublicacionAdopcionResponseDTO buscarPorId(Integer id) {
        return PublicacionAdopcionResponseDTO.from(buscarEntidad(id));
    }

    public PublicacionAdopcionResponseDTO crear(PublicacionAdopcionDTO dto) {
        Animal animal = animalRepo.findById(dto.getIdAnimal())
                .orElseThrow(() -> new NotFoundException("Animal no encontrado"));
        PublicacionAdopcion p = new PublicacionAdopcion();
        p.setDescripcion(dto.getDescripcion());
        p.setUrlImagen(dto.getUrlImagen());
        p.setEstado("Disponible");
        p.setAnimal(animal);
        return PublicacionAdopcionResponseDTO.from(pubRepo.save(p));
    }

    public PublicacionAdopcionResponseDTO actualizarEstado(Integer id, String estado) {
        PublicacionAdopcion p = buscarEntidad(id);
        p.setEstado(estado);
        return PublicacionAdopcionResponseDTO.from(pubRepo.save(p));
    }

    public void eliminar(Integer id) {
        if (!pubRepo.existsById(id)) {
            throw new NotFoundException("Publicacion no encontrada con id: " + id);
        }
        pubRepo.deleteById(id);
    }

    private PublicacionAdopcion buscarEntidad(Integer id) {
        return pubRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Publicacion no encontrada con id: " + id));
    }
}
