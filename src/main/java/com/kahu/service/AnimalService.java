package com.kahu.service;

import com.kahu.dto.AnimalDTO;
import com.kahu.dto.response.AnimalResponseDTO;
import com.kahu.entity.Animal;
import com.kahu.entity.Raza;
import com.kahu.entity.Usuario;
import com.kahu.exception.NotFoundException;
import com.kahu.repository.AnimalRepository;
import com.kahu.repository.RazaRepository;
import com.kahu.repository.UsuarioRepository;
import com.kahu.security.AuthContext;
import com.kahu.security.OwnershipUtil;

import java.util.List;

public class AnimalService {
    private final AnimalRepository animalRepo;
    private final RazaRepository razaRepo;
    private final UsuarioRepository usuarioRepo;

    public AnimalService(AnimalRepository animalRepo, RazaRepository razaRepo, UsuarioRepository usuarioRepo) {
        this.animalRepo = animalRepo;
        this.razaRepo = razaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    public List<AnimalResponseDTO> listarTodos() {
        if (OwnershipUtil.isCliente()) {
            return animalRepo.findByDuenioId(AuthContext.requireUserId()).stream()
                    .map(AnimalResponseDTO::from).toList();
        }
        return animalRepo.findAll().stream().map(AnimalResponseDTO::from).toList();
    }

    public List<AnimalResponseDTO> listarPorDuenio(Integer idUsuario) {
        OwnershipUtil.requireSameUser(idUsuario);
        return animalRepo.findByDuenioId(idUsuario).stream().map(AnimalResponseDTO::from).toList();
    }

    public AnimalResponseDTO buscarPorId(Integer id) {
        Animal animal = buscarEntidad(id);
        OwnershipUtil.requireAnimalOwner(animal);
        return AnimalResponseDTO.from(animal);
    }

    public AnimalResponseDTO crear(AnimalDTO dto) {
        Raza raza = razaRepo.findById(dto.getIdRaza())
                .orElseThrow(() -> new NotFoundException("Raza no encontrada"));
        Animal a = new Animal();
        a.setNombre(dto.getNombre());
        a.setEdadAnios(dto.getEdadAnios());
        a.setSexo(dto.getSexo());
        a.setTamano(dto.getTamano());
        a.setEsterilizado(dto.getEsterilizado());
        a.setRaza(raza);

        if (OwnershipUtil.isCliente()) {
            Usuario duenio = usuarioRepo.findById(AuthContext.requireUserId())
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
            a.setDuenio(duenio);
        } else if (dto.getIdDuenio() != null) {
            Usuario duenio = usuarioRepo.findById(dto.getIdDuenio())
                    .orElseThrow(() -> new NotFoundException("Duenio no encontrado"));
            a.setDuenio(duenio);
        }

        return AnimalResponseDTO.from(animalRepo.save(a));
    }

    public AnimalResponseDTO actualizar(Integer id, AnimalDTO dto) {
        Animal a = buscarEntidad(id);
        OwnershipUtil.requireAnimalOwner(a);
        Raza raza = razaRepo.findById(dto.getIdRaza())
                .orElseThrow(() -> new NotFoundException("Raza no encontrada"));
        a.setNombre(dto.getNombre());
        a.setEdadAnios(dto.getEdadAnios());
        a.setSexo(dto.getSexo());
        a.setTamano(dto.getTamano());
        a.setEsterilizado(dto.getEsterilizado());
        a.setRaza(raza);
        return AnimalResponseDTO.from(animalRepo.save(a));
    }

    public void eliminar(Integer id) {
        Animal a = buscarEntidad(id);
        OwnershipUtil.requireAnimalOwner(a);
        animalRepo.deleteById(id);
    }

    private Animal buscarEntidad(Integer id) {
        return animalRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Animal no encontrado con id: " + id));
    }
}
