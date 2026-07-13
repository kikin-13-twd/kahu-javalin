package com.kahu.service;

import com.kahu.dto.ServicioDTO;
import com.kahu.dto.response.ServicioResponseDTO;
import com.kahu.entity.Servicio;
import com.kahu.entity.TipoConsulta;
import com.kahu.exception.NotFoundException;
import com.kahu.repository.ServicioRepository;
import com.kahu.repository.TipoConsultaRepository;

import java.util.List;

public class ServicioService {
    private final ServicioRepository servicioRepo;
    private final TipoConsultaRepository tipoRepo;

    public ServicioService(ServicioRepository servicioRepo, TipoConsultaRepository tipoRepo) {
        this.servicioRepo = servicioRepo;
        this.tipoRepo = tipoRepo;
    }

    public List<ServicioResponseDTO> listarTodos() {
        return servicioRepo.findAll().stream().map(ServicioResponseDTO::from).toList();
    }

    public List<ServicioResponseDTO> listarPorTipo(Integer idTipo) {
        return servicioRepo.findByTipoConsultaId(idTipo).stream().map(ServicioResponseDTO::from).toList();
    }

    public ServicioResponseDTO buscarPorId(Integer id) {
        return ServicioResponseDTO.from(buscarEntidad(id));
    }

    public ServicioResponseDTO crear(ServicioDTO dto) {
        TipoConsulta tipo = tipoRepo.findById(dto.getIdTipoConsulta())
                .orElseThrow(() -> new NotFoundException("TipoConsulta no encontrado"));
        Servicio s = new Servicio();
        s.setNombreServicio(dto.getNombreServicio());
        s.setPrecio(dto.getPrecio());
        s.setTiempoServicioMinutos(dto.getTiempoServicioMinutos());
        s.setTipoConsulta(tipo);
        return ServicioResponseDTO.from(servicioRepo.save(s));
    }

    public ServicioResponseDTO actualizar(Integer id, ServicioDTO dto) {
        Servicio s = buscarEntidad(id);
        TipoConsulta tipo = tipoRepo.findById(dto.getIdTipoConsulta())
                .orElseThrow(() -> new NotFoundException("TipoConsulta no encontrado"));
        s.setNombreServicio(dto.getNombreServicio());
        s.setPrecio(dto.getPrecio());
        s.setTiempoServicioMinutos(dto.getTiempoServicioMinutos());
        s.setTipoConsulta(tipo);
        return ServicioResponseDTO.from(servicioRepo.save(s));
    }

    public void eliminar(Integer id) {
        if (!servicioRepo.existsById(id)) {
            throw new NotFoundException("Servicio no encontrado con id: " + id);
        }
        servicioRepo.deleteById(id);
    }

    private Servicio buscarEntidad(Integer id) {
        return servicioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado con id: " + id));
    }
}
