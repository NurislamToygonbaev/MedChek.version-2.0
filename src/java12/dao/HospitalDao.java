package java12.dao;

import java12.models.Hospital;
import java12.models.Patient;

import java.util.List;
import java.util.Map;

public interface HospitalDao {

    Hospital findById(Long hospitalId);

    Boolean add(Hospital hospital);

    String delete(Long id);

    List<Hospital> getAll();

    List<Patient> getAllPatientFromHospital(Long id);

    Map<String, Hospital> getAllHospitalByAddress (String address);
}
