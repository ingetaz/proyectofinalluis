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

import com.tlaxcala.dto.ExamDTO;
import com.tlaxcala.model.Exam;
import com.tlaxcala.service.IExamService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
public class ExamController {

    private final IExamService service;

    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    private ExamDTO convertToDto(Exam obj) {
        return mapper.map(obj, ExamDTO.class);
    }

    private Exam convertToEntity(ExamDTO dto) {
        return mapper.map(dto, Exam.class);
    }

    @GetMapping
    public ResponseEntity<List<ExamDTO>> findAll() {
        // podemos ocupar el api de programación funcional para el manejo de listas
        // mediante
        // la propiedad stream
        // cuando se hace referencia de un método dentro de un lambda yo puedo aplicar
        // una abreviación
        // List<ExamDTO> listExample = service.findAll().stream().map(e ->
        // convertToDto(e)).toList();
        List<ExamDTO> list = service.findAll().stream().map(this::convertToDto).toList();
        // List<ExamDTO> list = service.findAll().stream().map(e ->
        /*
         * List<ExamRecord> list = service.findAll()
         * .stream().
         * map(e ->
         * new ExamRecord(e.getIdExam(), e.getFirstName(), e.getLastName(),
         * e.getDni(),
         * e.getAddress(), e.getPhone(), e.getEmail())
         * ).toList();
         */
        /*
         * {
         * 
         * ExamDTO dto = new ExamDTO();
         * dto.setIdExam(e.getIdExam());
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
    public ResponseEntity<ExamDTO> save(@Valid @RequestBody ExamDTO dto) {
        Exam obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdExam())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamDTO> findById(@PathVariable("id") Integer id) {
        Exam obj = service.findById(id);
        return new ResponseEntity<>(convertToDto(obj), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exam> update(@PathVariable("id") Integer id, @RequestBody ExamDTO dto) throws Exception {
        Exam obj = service.update(convertToEntity(dto), id);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Exam> delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // hateoas -> para tipo get
    @GetMapping("/hateoas/{id}")
    public EntityModel<ExamDTO> findByHateoas(@PathVariable("id") Integer id) {
        EntityModel<ExamDTO> resource = EntityModel.of(convertToDto(service.findById(id))); // la salida

        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id)); // la generación del link

        resource.add(link1.withRel("exam-info1")); // agregamos el link con una llave
        resource.add(link1.withRel("exam-info2"));

        return resource;
    }

}
