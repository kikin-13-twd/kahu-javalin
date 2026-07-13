package com.kahu.service;

import com.kahu.dto.AnimalDTO;
import com.kahu.entity.Animal;
import com.kahu.entity.Raza;
import com.kahu.entity.Rol;
import com.kahu.entity.Usuario;
import com.kahu.exception.ForbiddenException;
import com.kahu.repository.AnimalRepository;
import com.kahu.repository.RazaRepository;
import com.kahu.repository.UsuarioRepository;
import com.kahu.security.AuthContext;
import com.kahu.security.RoleConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceOwnershipTest {

    @Mock private AnimalRepository animalRepo;
    @Mock private RazaRepository razaRepo;
    @Mock private UsuarioRepository usuarioRepo;

    @InjectMocks private AnimalService animalService;

    private Usuario duenio;
    private Animal animalAjeno;

    @BeforeEach
    void setUp() {
        Rol rol = new Rol(3, RoleConstants.CLIENTE);
        duenio = new Usuario(1, "Juan", "Perez", "juan@test.com", "hash", "555", null, LocalDate.now(), rol);
        Usuario otro = new Usuario(2, "Maria", "Lopez", "maria@test.com", "hash", "555", null, LocalDate.now(), rol);

        animalAjeno = new Animal(10, "Firulais", 3, "M", "Mediano", true, new Raza(), otro);

        AuthContext.set(new AuthContext.UserSession(1, "juan@test.com", RoleConstants.CLIENTE));
    }

    @AfterEach
    void tearDown() {
        AuthContext.clear();
    }

    @Test
    void cliente_listarTodos_soloVeSusAnimales() {
        Animal miAnimal = new Animal(5, "Max", 2, "M", "Pequeno", false, new Raza(), duenio);
        when(animalRepo.findByDuenioId(1)).thenReturn(List.of(miAnimal));

        var result = animalService.listarTodos();

        assertEquals(1, result.size());
        assertEquals("Max", result.get(0).getNombre());
        verify(animalRepo).findByDuenioId(1);
        verify(animalRepo, never()).findAll();
    }

    @Test
    void cliente_buscarAnimalAjeno_lanza403() {
        when(animalRepo.findById(10)).thenReturn(Optional.of(animalAjeno));

        assertThrows(ForbiddenException.class, () -> animalService.buscarPorId(10));
    }

    @Test
    void cliente_crear_asignaDuenioAutomaticamente() {
        Raza raza = new Raza(1, "Labrador", null);
        when(razaRepo.findById(1)).thenReturn(Optional.of(raza));
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(duenio));
        when(animalRepo.save(any(Animal.class))).thenAnswer(inv -> inv.getArgument(0));

        var dto = new AnimalDTO("Nuevo", 1, "M", "Pequeno", false, 1, null);
        animalService.crear(dto);

        verify(usuarioRepo).findById(1);
        verify(animalRepo).save(argThat(a -> a.getDuenio() != null && a.getDuenio().getIdUsuario().equals(1)));
    }
}
