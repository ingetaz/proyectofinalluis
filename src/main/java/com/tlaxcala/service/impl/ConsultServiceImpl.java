package com.tlaxcala.service.impl;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.tlaxcala.dto.ConsultProcDTO;
import com.tlaxcala.dto.IConsultProcDTO;
import com.tlaxcala.model.Consult;
import com.tlaxcala.model.Exam;
import com.tlaxcala.repo.IGenericRepo;
import com.tlaxcala.repo.IConsultExamRepo;
import com.tlaxcala.repo.IConsultRepo;
import com.tlaxcala.service.IConsultService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class ConsultServiceImpl extends CRUDImpl<Consult, Integer> implements IConsultService {

    private final IConsultRepo consultRepo;
    private final IConsultExamRepo ceRepo;

    @Override
    protected IGenericRepo<Consult, Integer> getRepo() {
        return consultRepo;
    }

    @Transactional
    @Override
    public Consult saveTransactional(Consult consult, List<Exam> exams) {
        // consulta con su maestro detalle
        consultRepo.save(consult);
        // list exams
        exams.forEach(ex -> ceRepo.saveExam(consult.getIdConsult(), ex.getIdExam()));

        return consult;
    }

    @Override
    public List<Consult> search(String dni, String fullname) {
        return consultRepo.search(dni, fullname);
    }

    @Override
    public List<Consult> searchByDates(LocalDateTime date1, LocalDateTime date2) {
       final int OFFSET_DAYS = 1;
       return consultRepo.searchByDates(date1, date2.plusDays(OFFSET_DAYS));
    }

    @Override
    public List<ConsultProcDTO> callProcedureOrFunctionNative() {
      List<ConsultProcDTO> lst = new ArrayList<>();

      /*
       * [3, "02/09/2023"]
       * [2, "23/09/2023"]
       */

       consultRepo.callProcedureOrFunctionNative().forEach(e -> {
        ConsultProcDTO dto = new ConsultProcDTO();
        dto.setQuantity((Integer) e[0]);
        dto.setConsultdate((String) e[1]);

        lst.add(dto);
       });

       return lst;
    }

    @Override
    public List<IConsultProcDTO> callProcedureOrFunctionProjection() {
        return consultRepo.callProcedureOrFunctionProjection();
    }

    @Override
    public byte[] generateReport() throws Exception {
        byte[] data = null;
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("txt_title", "Report Title");

        File file = new ClassPathResource("/reports/consults.jasper").getFile();
        JasperPrint print = JasperFillManager.fillReport(file.getPath(), parameters, new JRBeanCollectionDataSource(callProcedureOrFunctionNative()));
        data = JasperExportManager.exportReportToPdf(print);

        return data;
        
    }

}
