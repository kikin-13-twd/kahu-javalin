package com.kahu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ANIMAL")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Animal")
    private Integer idAnimal;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Edad_Anios", nullable = false)
    private Integer edadAnios;

    @Column(name = "Sexo", nullable = false, length = 20)
    private String sexo;

    @Column(name = "Tamano", nullable = false, length = 50)
    private String tamano;

    @Column(name = "Esterilizado", nullable = false)
    private Boolean esterilizado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Raza", nullable = false)
    private Raza raza;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Usuario_Duenio")
    private Usuario duenio;
}
