package java12.services;

import java12.models.Patient;

import java.util.List;
import java.util.Map;

public interface PatientService {

    String addPatientsToHospital(Long id, List<Patient> patients);

    Patient getPatientById(Long id);

    Map<Integer, List<Patient>> getPatientByAge();

    List<Patient> sortPatientsByAge(String ascOrDesc);
}
