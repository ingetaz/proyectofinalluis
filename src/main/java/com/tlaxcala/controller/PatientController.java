package com.tlaxcala.controller;

import java.net.URI;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.tlaxcala.dto.PatientDTO;
//import com.tlaxcala.dto.PatientRecord;
import com.tlaxcala.model.Patient;
import com.tlaxcala.service.IPatientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final IPatientService service;

    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    private PatientDTO convertToDto(Patient obj) {
        return mapper.map(obj, PatientDTO.class);
    }

    private Patient convertToEntity(PatientDTO dto) {
        return mapper.map(dto, Patient.class);
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> findAll() {
        // podemos ocupar el api de programación funcional para el manejo de listas
        // mediante
        // la propiedad stream
        // cuando se hace referencia de un método dentro de un lambda yo puedo aplicar
        // una abreviación
        // List<PatientDTO> listExample = service.findAll().stream().map(e ->
        // convertToDto(e)).toList();
        List<PatientDTO> list = service.findAll().stream().map(this::convertToDto).toList();
        // List<PatientDTO> list = service.findAll().stream().map(e ->
        /*
         * List<PatientRecord> list = service.findAll()
         * .stream().
         * map(e ->
         * new PatientRecord(e.getIdPatient(), e.getFirstName(), e.getLastName(),
         * e.getDni(),
         * e.getAddress(), e.getPhone(), e.getEmail())
         * ).toList();
         */
        /*
         * {
         * 
         * PatientDTO dto = new PatientDTO();
         * dto.setIdPatient(e.getIdPatient());
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
    public ResponseEntity<PatientDTO> save(@Valid @RequestBody PatientDTO dto) {
        Patient obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdPatient())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> findById(@PathVariable("id") Integer id) {
        Patient obj = service.findById(id);
        return new ResponseEntity<>(convertToDto(obj), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> update(@PathVariable("id") Integer id, @RequestBody PatientDTO dto) throws Exception {
        Patient obj = service.update(convertToEntity(dto), id);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Patient> delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // hateoas -> para tipo get
    @GetMapping("/hateoas/{id}")
    public EntityModel<PatientDTO> findByHateoas(@PathVariable("id") Integer id) {
        EntityModel<PatientDTO> resource = EntityModel.of(convertToDto(service.findById(id))); // la salida

        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id)); // la generación del link

        resource.add(link1.withRel("patient-info1")); // agregamos el link con una llave
        resource.add(link1.withRel("patient-info2"));

        return resource;
    }

    @GetMapping("/pageable")
    public ResponseEntity<Page<PatientDTO>> listPage(Pageable pageable) {
        Page<PatientDTO> page = service.listPage(pageable).map(p -> mapper.map(p, PatientDTO.class));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

}
