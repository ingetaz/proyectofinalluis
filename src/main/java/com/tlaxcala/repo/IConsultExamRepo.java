package com.tlaxcala.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tlaxcala.model.ConsultExam;

public interface IConsultExamRepo extends IGenericRepo<ConsultExam, Integer> { 

    // feature -> query
    // en native query -> hago referencia al nombre de la tabla
    @Modifying
    @Query(value = "INSERT INTO consult_exam(id_consult, id_exam) VALUES (:idConsult, :idExam)", nativeQuery = true)
    Integer saveExam(@Param("idConsult") Integer idConsult, @Param("idExam") Integer idExam);

    // other querys
    @Query("SELECT new ConsultExam(ce.exam) FROM ConsultExam ce WHERE ce.consult.idConsult = :idConsult")
    // opción 2: @Query("SELECT new com.tlaxcala.model.ConsultExam(ce.exam) FROM ConsultExam ce WHERE ce.consult.idConsult = :idConsult")
    List<ConsultExam> getExamsByConsultId(@Param("idConsult") Integer idConsult);

}
