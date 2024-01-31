package com.tlaxcala.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tlaxcala.dto.IConsultProcDTO;
import com.tlaxcala.model.Consult;

public interface IConsultRepo extends IGenericRepo<Consult, Integer> { 

    // ej: Jorge Rubilar -> fullname
    // BK -> BDD -> jorge rubilar
    // jpql -> SELECT * -> es implicito
    // jqpl -> apuntamos a la entidad -> Referencias son a nivel de objetos
    // jqpl -> es la sentencia por defecto de una query en spring con jpa
    // filter -> field dni y field fullname -> firstname y un lastname
    // no necesito especificar el atributo value como en natives query
    @Query("FROM Consult c WHERE c.patient.dni = :dni OR LOWER(c.patient.firstName) LIKE %:fullname% OR LOWER(c.patient.lastName) LIKE %:fullname%")
    List<Consult> search(@Param("dni") String dni, @Param("fullname") String fullname);

    // range of dates: api java de localdatetime o localdate
    // >=   | <
    // 15-09-2023 al 30-09-2023
    @Query("FROM Consult c WHERE c.consultDate BETWEEN :date1 AND :date2")
    List<Consult> searchByDates(@Param("date1") LocalDateTime date1, @Param("date2") LocalDateTime date2);
    
    // spring con la definición 1: trabaja los resultados como un array de objetos
    @Query(value = "select * from fn_list()", nativeQuery = true)
    List<Object[]> callProcedureOrFunctionNative();

    @Query(value = "select * from fn_list()", nativeQuery = true)
    List<IConsultProcDTO> callProcedureOrFunctionProjection();
}
