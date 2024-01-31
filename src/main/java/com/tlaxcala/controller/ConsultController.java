package com.tlaxcala.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
//import java.util.Map;

//import org.cloudinary.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
import com.tlaxcala.dto.ConsultDTO;
import com.tlaxcala.dto.ConsultListExamDTO;
import com.tlaxcala.dto.ConsultProcDTO;
import com.tlaxcala.dto.FilterConsultDTO;
import com.tlaxcala.dto.IConsultProcDTO;
import com.tlaxcala.model.Consult;
import com.tlaxcala.model.Exam;
import com.tlaxcala.model.MediaFile;
import com.tlaxcala.service.IConsultService;
import com.tlaxcala.service.IMediaFileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/consults")
@RequiredArgsConstructor
public class ConsultController {

    private final IConsultService service;
    private final IMediaFileService mfService;
    //private final Cloudinary cloudinary;

    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    private ConsultDTO convertToDto(Consult obj) {
        return mapper.map(obj, ConsultDTO.class);
    }

    private Consult convertToEntity(ConsultDTO dto) {
        return mapper.map(dto, Consult.class);
    }

    @GetMapping
    public ResponseEntity<List<ConsultDTO>> findAll() {
        // podemos ocupar el api de programación funcional para el manejo de listas
        // mediante
        // la propiedad stream
        // cuando se hace referencia de un método dentro de un lambda yo puedo aplicar
        // una abreviación
        // List<ConsultDTO> listExample = service.findAll().stream().map(e ->
        // convertToDto(e)).toList();
        List<ConsultDTO> list = service.findAll().stream().map(this::convertToDto).toList();
        // List<ConsultDTO> list = service.findAll().stream().map(e ->
        /*
         * List<ConsultRecord> list = service.findAll()
         * .stream().
         * map(e ->
         * new ConsultRecord(e.getIdConsult(), e.getFirstName(), e.getLastName(),
         * e.getDni(),
         * e.getAddress(), e.getPhone(), e.getEmail())
         * ).toList();
         */
        /*
         * {
         * 
         * ConsultDTO dto = new ConsultDTO();
         * dto.setIdConsult(e.getIdConsult());
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
    public ResponseEntity<ConsultDTO> save(@Valid @RequestBody ConsultListExamDTO dto) {
        Consult cons = this.convertToEntity(dto.getConsult());
        
        // alternativa 1
        //List<Exam> exams = dto.getLstExam().stream().map(e -> mapper.map(e, Exam.class)).toList();
        // alternativa 2
        List<Exam> exams = mapper.map(dto.getLstExam(), new TypeToken<List<Exam>>(){}.getType());

        Consult obj = service.saveTransactional(cons, exams);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdConsult())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultDTO> findById(@PathVariable("id") Integer id) {
        Consult obj = service.findById(id);
        return new ResponseEntity<>(convertToDto(obj), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consult> update(@PathVariable("id") Integer id, @RequestBody ConsultDTO dto) throws Exception {
        Consult obj = service.update(convertToEntity(dto), id);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Consult> delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // hateoas -> para tipo get
    @GetMapping("/hateoas/{id}")
    public EntityModel<ConsultDTO> findByHateoas(@PathVariable("id") Integer id) {
        EntityModel<ConsultDTO> resource = EntityModel.of(convertToDto(service.findById(id))); // la salida

        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id)); // la generación del link

        resource.add(link1.withRel("consult-info1")); // agregamos el link con una llave
        resource.add(link1.withRel("consult-info2"));

        return resource;
    }

    @PostMapping("/search/others")
    public ResponseEntity<List<ConsultDTO>> searchByOthers(@RequestBody FilterConsultDTO filterDTO) {
        List<Consult> consults = service.search(filterDTO.getDni(), filterDTO.getFullname());

        //List<ConsultDTO> consultDTOS = consults.stream().map(e-> mapper.map(e, ConsultDTO.class)).toList();
        List<ConsultDTO> consultDTOs = mapper.map(consults, new TypeToken<List<ConsultDTO>>(){}.getType());

        return new ResponseEntity<>(consultDTOs, HttpStatus.OK);

    }

    @GetMapping("/search/date")
    public ResponseEntity<List<ConsultDTO>> searchByDates(
        @RequestParam(value = "date1", defaultValue = "2023-09-28", required = true) String date1,
        @RequestParam(value = "date2") String date2)
    {
        List<Consult> consults = service.searchByDates(LocalDateTime.parse(date1), LocalDateTime.parse(date2));
        List<ConsultDTO> consultDTOs = mapper.map(consults, new TypeToken<List<ConsultDTO>>(){}.getType());

        return new ResponseEntity<>(consultDTOs, HttpStatus.OK);
    }

    @GetMapping("/callProcedureNative")
    public ResponseEntity<List<ConsultProcDTO>> callProcedureOrFunctionNative() {
        List<ConsultProcDTO> consults = service.callProcedureOrFunctionNative();
        return new ResponseEntity<>(consults, HttpStatus.OK);
    }

    @GetMapping("/callProcedureProjection")
    public ResponseEntity<List<IConsultProcDTO>> callProcedureOrFunctionProjection() {
        List<IConsultProcDTO> consults = service.callProcedureOrFunctionProjection();
        return new ResponseEntity<>(consults, HttpStatus.OK);
    }

    // forma 1: es mantenerlo como binario
    // forma 2: devolverlo procesado en el formato al cliente
    @GetMapping(value = "/generateReport", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateReport() throws Exception {
        byte[] data = service.generateReport();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    // forma 1: recuperar el archivo y guardarlo en bdd tal cual, se guardaría la imagen completa
    // forma 2: recuperar el archivo pero desde un repo externo como un path
    @PostMapping(value = "/saveFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveFile(@RequestParam("file") MultipartFile file) throws Exception {

        // forma 1: adecuada solo para apps muy pequeñas o de nicho donde el core de la app no sea la subida de archivos
        MediaFile mf = new MediaFile();
        mf.setFileType(file.getContentType()); // recuperar el tipo del archivo
        mf.setFilename(file.getOriginalFilename()); // recuperar el nombre del archivo
        mf.setValue(file.getBytes()); // arreglo de bytes con el recurso

        mfService.save(mf);

        /*// forma 2 (repositorio externo): gestionarlo desde un repositorio externo para el tema del escalado y el performance
        File f = this.convertToFile(file); // convertir un multipart a file
        Map response = cloudinary.uploader().upload(f, ObjectUtils.asMap("resource_type", "auto"));
        JSONObject json = new JSONObject(response); // recurso recuperado en JSON
        String url = json.getString("url"); // devuelve la url del path desde cloudinary

        // mfService.update(url); // servicio que recupera la url de cloudinary y lo guarda en bdd*/

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/readFile/{idFile}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> readFile(@PathVariable("idFile") Integer idFile) throws IOException {

        byte[] arr = mfService.findById(idFile).getValue();

        return new ResponseEntity<>(arr, HttpStatus.OK);
    }

    // método utilitario
    public File convertToFile(MultipartFile multipartFile) throws Exception {
        File file = new File(multipartFile.getOriginalFilename()); // obtenemos el nombre
        FileOutputStream outputStream = new FileOutputStream(file); // creamos un flujo para el file
        outputStream.write(multipartFile.getBytes()); // recupero el file para la salida
        outputStream.close();
        return file;
    }

}
