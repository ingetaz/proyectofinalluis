package com.tlaxcala.controller;

import java.net.URI;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tlaxcala.dto.SpecialtyDTO;
import com.tlaxcala.model.Specialty;
import com.tlaxcala.service.ISpecialtyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/specialties")
@RequiredArgsConstructor
public class SpecialtyController {

    private final ISpecialtyService service;

    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    private SpecialtyDTO convertToDto(Specialty obj) {
        return mapper.map(obj, SpecialtyDTO.class);
    }

    private Specialty convertToEntity(SpecialtyDTO dto) {
        return mapper.map(dto, Specialty.class);
    }

    @GetMapping
    public ResponseEntity<List<SpecialtyDTO>> findAll() {
        // podemos ocupar el api de programación funcional para el manejo de listas
        // mediante
        // la propiedad stream
        // cuando se hace referencia de un método dentro de un lambda yo puedo aplicar
        // una abreviación
        // List<SpecialtyDTO> listExample = service.findAll().stream().map(e ->
        // convertToDto(e)).toList();
        List<SpecialtyDTO> list = service.findAll().stream().map(this::convertToDto).toList();
        // List<SpecialtyDTO> list = service.findAll().stream().map(e ->
        /*
         * List<SpecialtyRecord> list = service.findAll()
         * .stream().
         * map(e ->
         * new SpecialtyRecord(e.getIdSpecialty(), e.getFirstName(), e.getLastName(),
         * e.getDni(),
         * e.getAddress(), e.getPhone(), e.getEmail())
         * ).toList();
         */
        /*
         * {
         * 
         * SpecialtyDTO dto = new SpecialtyDTO();
         * dto.setIdSpecialty(e.getIdSpecialty());
         * dto.setFirstName(e.getFirstName());
         * dto.setLastName(e.getLastName());
         * dto.setDni(e.getDni());
         * dto.setPhone(e.getPhone());
         * dto.setEmail(e.getEmail());
         * dto.setAddress(e.getAddress());
         * return dto;
         * }
         * ).toList();
         */
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SpecialtyDTO> save(@Valid @RequestBody SpecialtyDTO dto) {
        Specialty obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdSpecialty())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyDTO> findById(@PathVariable("id") Integer id) {
        Specialty obj = service.findById(id);
        return new ResponseEntity<>(convertToDto(obj), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Specialty> update(@PathVariable("id") Integer id, @RequestBody SpecialtyDTO dto) throws Exception {
        Specialty obj = service.update(convertToEntity(dto), id);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Specialty> delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // hateoas -> para tipo get
    @GetMapping("/hateoas/{id}")
    public EntityModel<SpecialtyDTO> findByHateoas(@PathVariable("id") Integer id) {
        EntityModel<SpecialtyDTO> resource = EntityModel.of(convertToDto(service.findById(id))); // la salida

        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id)); // la generación del link

        resource.add(link1.withRel("specialty-info1")); // agregamos el link con una llave
        resource.add(link1.withRel("specialty-info2"));

        return resource;
    }

}

