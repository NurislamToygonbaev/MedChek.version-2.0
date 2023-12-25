package java12.dao;

import java12.models.Doctor;

import java.util.List;

public interface DoctorDao {

    Doctor findById(Long doctorId);

    String add(Long hospitalId, Doctor doctor);

    String remove(Long id);

    List<Doctor> getAll();

    String assignDoctorToDepartment(Long departmentId, List<Long> doctorsId);

    List<Doctor> getAllDoctorsByHospitalId(Long id);

    List<Doctor> getAllDoctorsByDepartmentId(Long id);

    String updateById(Long id, Doctor doctor);
}
