package com.kahu.service;

import com.kahu.dto.ReporteClinicoDTO;
import com.kahu.dto.response.ReporteClinicoResponseDTO;
import com.kahu.entity.Cita;
import com.kahu.entity.ReporteClinico;
import com.kahu.exception.BusinessException;
import com.kahu.exception.NotFoundException;
import com.kahu.repository.CitaRepository;
import com.kahu.repository.ReporteClinicoRepository;

import java.util.List;

public class ReporteClinicoService {
    private final ReporteClinicoRepository reporteRepo;
    private final CitaRepository citaRepo;

    public ReporteClinicoService(ReporteClinicoRepository reporteRepo, CitaRepository citaRepo) {
        this.reporteRepo = reporteRepo;
        this.citaRepo = citaRepo;
    }

    public List<ReporteClinicoResponseDTO> listarTodos() {
        return reporteRepo.findAll().stream().map(ReporteClinicoResponseDTO::from).toList();
    }

    public ReporteClinicoResponseDTO buscarPorId(Integer id) {
        return ReporteClinicoResponseDTO.from(buscarEntidad(id));
    }

    public ReporteClinicoResponseDTO buscarPorCita(Integer idCita) {
        return ReporteClinicoResponseDTO.from(
                reporteRepo.findByCitaId(idCita)
                        .orElseThrow(() -> new NotFoundException("No existe reporte para la cita: " + idCita))
        );
    }

    public ReporteClinicoResponseDTO crear(ReporteClinicoDTO dto) {
        Cita cita = citaRepo.findById(dto.getIdCita())
                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));
        if (reporteRepo.findByCitaId(dto.getIdCita()).isPresent()) {
            throw new BusinessException("Ya existe un reporte para esta cita");
        }
        ReporteClinico r = new ReporteClinico();
        r.setDiagnostico(dto.getDiagnostico());
        r.setTratamiento(dto.getTratamiento());
        r.setCita(cita);
        cita.setEstado("Completada");
        citaRepo.save(cita);
        return ReporteClinicoResponseDTO.from(reporteRepo.save(r));
    }

    public ReporteClinicoResponseDTO actualizar(Integer id, ReporteClinicoDTO dto) {
        ReporteClinico r = buscarEntidad(id);
        r.setDiagnostico(dto.getDiagnostico());
        r.setTratamiento(dto.getTratamiento());
        return ReporteClinicoResponseDTO.from(reporteRepo.save(r));
    }

    private ReporteClinico buscarEntidad(Integer id) {
        return reporteRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Reporte no encontrado con id: " + id));
    }
}
