package java12.dao.impl;

import java12.MyGeneratorId;
import java12.dao.DoctorDao;
import java12.database.DataBase;
import java12.exception.NotFoundException;
import java12.models.Department;
import java12.models.Doctor;
import java12.models.Hospital;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DoctorDaoImpl implements DoctorDao {

    private final DataBase dataBase;

    public DoctorDaoImpl(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public Doctor findById(Long doctorId) {
        return getAll().stream()
                .filter(doctor -> doctor.getId().equals(doctorId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Doctor with id: " + doctorId + " not found"));
    }

    @Override
    public String add(Long hospitalId, Doctor doctor) {
        Optional<Hospital> first = dataBase.getAll().stream()
                .filter(hospital -> hospital.getId().equals(hospitalId))
                .findFirst();
        if (first.isPresent()) {
            Hospital hospital = first.get();
            doctor.setId(MyGeneratorId.generatorDoctor());
            hospital.getDoctors().add(doctor);
            return "Successfully added";
        } else throw new NotFoundException("Hospital with id: " + hospitalId + " not found!");
    }

    @Override
    public String remove(Long id) {
        Optional<Doctor> first = dataBase.getAll().stream()
                .flatMap(hospital -> Stream.concat(hospital.getDoctors().stream(),
                        hospital.getDepartments().stream()
                                .flatMap(department -> department.getDoctors().stream())))
                .filter(doctor -> doctor.getId().equals(id))
                .findFirst();

        first.ifPresent(doctor -> {
            for (Hospital hospital : dataBase.getAll()) {
                hospital.getDoctors().remove(doctor);
                hospital.getDepartments().forEach(department -> department.getDoctors().remove(doctor));
            }
        });

        return first.map(doctor -> "Successfully deleted").orElseThrow(() ->
            new NotFoundException("Doctor with id: " + id + " not found"));
    }

    @Override
    public List<Doctor> getAll() {
        List<Doctor> doctors = new ArrayList<>();
        for (Hospital hospital : dataBase.getAll()) {
            doctors.addAll(hospital.getDoctors());
            for (Department department : hospital.getDepartments()) {
                doctors.addAll(department.getDoctors());
            }
        }
        if (!doctors.isEmpty()) return doctors;
        else throw new NotFoundException("No doctors found in any hospital");
    }

    @Override
    public String assignDoctorToDepartment(Long departmentId, List<Long> doctorsId) {
        for (Hospital hospital : dataBase.getAll()) {
            for (Department department : hospital.getDepartments()) {
                if (departmentId.equals(department.getId())) {
                    List<Doctor> doctorsToAdd = hospital.getDoctors().stream()
                            .filter(doctor -> doctorsId.contains(doctor.getId()))
                            .toList();
                    if (doctorsToAdd.isEmpty()) {
                        throw new IllegalArgumentException("Doctors with ids: " + doctorsId + " not found!");
                    }
                    department.getDoctors().addAll(doctorsToAdd);
                    hospital.getDoctors().removeAll(doctorsToAdd);
                    return "Successfully assign doctors to department";
                }
            }
        }
        throw new NotFoundException("Department with id: " + departmentId + " not found");
    }

    @Override
    public List<Doctor> getAllDoctorsByHospitalId(Long id) {
        Optional<Hospital> first = dataBase.getAll().stream()
                .filter(hospital -> hospital.getId().equals(id))
                .findFirst();
        return first.map(Hospital::getDoctors)
                .orElseThrow(() -> new NotFoundException("Hospital with id: " + id + " not found"));
    }

    @Override
    public List<Doctor> getAllDoctorsByDepartmentId(Long id) {
        Optional<Department> first = dataBase.getAll().stream()
                .flatMap(hospital -> hospital.getDepartments().stream())
                .filter(department -> department.getId().equals(id))
                .findFirst();
        return first.map(Department::getDoctors)
                .orElseThrow(() -> new NotFoundException("Department with id: " + id + " not found"));
    }

    @Override
    public String updateById(Long id, Doctor doctor) {
        Optional<Doctor> first = getAll().stream()
                .filter(doctor1 -> doctor1.getId().equals(id))
                .findFirst();
        if (first.isPresent()) {
            Doctor doctor1 = first.get();
            doctor1.setFirstName(doctor.getFirstName());
            doctor1.setLastName(doctor.getLastName());
            doctor1.setGender(doctor.getGender());
            doctor1.setExperienceYear(doctor.getExperienceYear());
            return "Successfully updated";
        } else throw new NotFoundException("Doctor with id: " + id + " not found");
    }
}
