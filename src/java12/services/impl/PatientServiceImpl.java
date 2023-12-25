package java12.services.impl;

import java12.dao.PatientDao;
import java12.exception.NotFoundException;
import java12.models.Patient;
import java12.services.GenericService;
import java12.services.PatientService;

import java.util.List;
import java.util.Map;

public class PatientServiceImpl implements GenericService<Patient>, PatientService {

    private final PatientDao dao;

    public PatientServiceImpl(PatientDao dao) {
        this.dao = dao;
    }

    @Override
    public String add(Long hospitalId, Patient patient) {
        try {
            dao.add(hospitalId, patient);
            return "Successfully added";
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @Override
    public String removeById(Long id) {
        try {
            return dao.delete(id);
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @Override
    public String updateById(Long id, Patient patient) {
        try {
            return dao.updateById(id, patient);
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @Override
    public String addPatientsToHospital(Long id, List<Patient> patients) {
        try {
            return dao.addPatientsToHospital(id, patients);
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @Override
    public Patient getPatientById(Long id) {
        try {
            return dao.findById(id);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Map<Integer, List<Patient>> getPatientByAge() {
        return dao.getPatientByAge();
    }

    @Override
    public List<Patient> sortPatientsByAge(String ascOrDesc) {
        try {
           return dao.sortPatientsByAge(ascOrDesc);
        } catch (NotFoundException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
