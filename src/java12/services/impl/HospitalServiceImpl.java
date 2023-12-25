package java12.services.impl;

import java12.dao.HospitalDao;
import java12.exception.NotFoundException;
import java12.models.Hospital;
import java12.models.Patient;
import java12.services.HospitalService;

import java.util.List;
import java.util.Map;

public class HospitalServiceImpl implements HospitalService {

    private final HospitalDao hospitalDao;

    public HospitalServiceImpl(HospitalDao hospitalDao) {
        this.hospitalDao = hospitalDao;
    }

    @Override
    public String addHospital(Hospital hospital) {
        try {
            hospitalDao.add(hospital);
            return "successfully added";
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @Override
    public Hospital findHospitalById(Long id) {
        try {
            return hospitalDao.findById(id);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Hospital> getAllHospital() {
        return hospitalDao.getAll();
    }

    @Override
    public List<Patient> getAllPatientFromHospital(Long id) {
        try {
            return hospitalDao.getAllPatientFromHospital(id);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String deleteHospitalById(Long id) {
        try {
            return hospitalDao.delete(id);
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    @Override
    public Map<String, Hospital> getAllHospitalByAddress(String address) {
        try {
            return hospitalDao.getAllHospitalByAddress(address);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
