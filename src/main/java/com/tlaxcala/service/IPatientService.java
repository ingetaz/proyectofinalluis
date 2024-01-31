package com.tlaxcala.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

//import java.util.List;

import com.tlaxcala.model.Patient;

public interface IPatientService extends ICRUD<Patient, Integer> {

   // DRY Principle: Don't repeat yourself! X
   // method: obtenga los pacientes ingresados a la UCI
   //List<Patient> getUCIPatients();

   Page<Patient> listPage(Pageable pageable);

}
