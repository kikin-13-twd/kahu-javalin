package com.kahu.service;

import com.kahu.dto.response.RazaResponseDTO;
import com.kahu.entity.Especie;
import com.kahu.entity.Raza;
import com.kahu.exception.NotFoundException;
import com.kahu.repository.EspecieRepository;
import com.kahu.repository.RazaRepository;

import java.util.List;

public class RazaService {
    private final RazaRepository razaRepo;
    private final EspecieRepository especieRepo;

    public RazaService(RazaRepository razaRepo, EspecieRepository especieRepo) {
        this.razaRepo = razaRepo;
        this.especieRepo = especieRepo;
    }

    public List<RazaResponseDTO> listarTodas() {
        return razaRepo.findAll().stream().map(RazaResponseDTO::from).toList();
    }

    public List<RazaResponseDTO> listarPorEspecie(Integer idEspecie) {
        return razaRepo.findByEspecieId(idEspecie).stream().map(RazaResponseDTO::from).toList();
    }

    public RazaResponseDTO buscarPorId(Integer id) {
        return RazaResponseDTO.from(buscarEntidad(id));
    }

    public RazaResponseDTO crear(String nombre, Integer idEspecie) {
        Especie esp = especieRepo.findById(idEspecie)
                .orElseThrow(() -> new NotFoundException("Especie no encontrada"));
        Raza r = new Raza();
        r.setNombreRaza(nombre);
        r.setEspecie(esp);
        return RazaResponseDTO.from(razaRepo.save(r));
    }

    private Raza buscarEntidad(Integer id) {
        return razaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Raza no encontrada con id: " + id));
    }
}
