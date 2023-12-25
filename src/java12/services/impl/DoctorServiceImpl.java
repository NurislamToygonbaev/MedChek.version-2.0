package java12.services.impl;

import java12.dao.DoctorDao;
import java12.exception.NotFoundException;
import java12.models.Doctor;
import java12.services.DoctorService;
import java12.services.GenericService;

import java.util.List;

public class DoctorServiceImpl implements GenericService<Doctor>, DoctorService {

    private final DoctorDao dao;

    public DoctorServiceImpl(DoctorDao dao) {
        this.dao = dao;
    }

    @Override
    public Doctor findDoctorById(Long id) {
        try {
           return dao.findById(id);
        } catch (NotFoundException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String assignDoctorToDepartment(Long departmentId, List<Long> doctorsId) {
        try {
           return dao.assignDoctorToDepartment(departmentId, doctorsId);
        } catch (NotFoundException | IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @Override
    public List<Doctor> getAllDoctorsByHospitalId(Long id) {
        try {
           return dao.getAllDoctorsByHospitalId(id);
        } catch (NotFoundException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Doctor> getAllDoctorsByDepartmentId(Long id) {
        try {
            return dao.getAllDoctorsByDepartmentId(id);
        } catch (NotFoundException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String add(Long hospitalId, Doctor doctor) {
        try {
           return dao.add(hospitalId, doctor);
        } catch (NotFoundException e){
            return e.getMessage();
        }
    }

    @Override
    public String removeById(Long id) {
        try {
           return dao.remove(id);
        } catch (NotFoundException e){
            return e.getMessage();
        }
    }

    @Override
    public String updateById(Long id, Doctor doctor) {
        try {
           return dao.updateById(id, doctor);
        } catch (NotFoundException e){
            return e.getMessage();
        }
    }
}
