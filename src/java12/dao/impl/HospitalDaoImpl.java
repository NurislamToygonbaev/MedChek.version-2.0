package java12.dao.impl;

import java12.MyGeneratorId;
import java12.dao.HospitalDao;
import java12.database.DataBase;
import java12.exception.NotFoundException;
import java12.models.Hospital;
import java12.models.Patient;

import java.util.*;
import java.util.stream.Collectors;

public class
HospitalDaoImpl implements HospitalDao {

    private final DataBase dataBase;

    public HospitalDaoImpl(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public Hospital findById(Long hospitalId) {
        return dataBase.getAll().stream()
                .filter(hospital -> hospital.getId().equals(hospitalId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Hospital with id: " + hospitalId + " not found"));
    }

    @Override
    public Boolean add(Hospital hospital) {
        boolean b = dataBase.getAll().stream()
                .anyMatch(hospital1 -> hospital1.getHospitalName().equalsIgnoreCase(hospital.getHospitalName()));
        if (b) throw new NotFoundException("Hospital with name: " + hospital.getHospitalName() + " is already have");
        hospital.setId(MyGeneratorId.generatorHospital());
        return dataBase.save(hospital);
    }

    @Override
    public String delete(Long id) {
        Optional<Hospital> first = dataBase.getAll().stream()
                .filter(hospital -> hospital.getId().equals(id))
                .findFirst();
        if (first.isPresent()) {
            dataBase.remove(first.get());
            return "Successfully deleted";
        } else throw new NotFoundException("Hospital with id: " + id + " not found");
    }

    @Override
    public List<Hospital> getAll() {
        return dataBase.getAll();
    }

    @Override
    public List<Patient> getAllPatientFromHospital(Long id) {
        Optional<Hospital> first = dataBase.getAll().stream()
                .filter(hospital -> hospital.getId().equals(id))
                .findFirst();
        if (first.isPresent()) return first.get().getPatients();
        else throw new NotFoundException("Hospital with id: " + id + " not found");
    }

    @Override
    public Map<String, Hospital> getAllHospitalByAddress(String address) {
        List<Hospital> hospitals = dataBase.getAll().stream()
                .filter(hospital -> hospital.getAddress().equalsIgnoreCase(address))
                .toList();
        if (!hospitals.isEmpty()) {
            Hospital firstHospital = hospitals.get(0);
            return Collections.singletonMap(firstHospital.getAddress(), firstHospital);
        } else {
            throw new NotFoundException("Hospital with address: " + address + " not found");
        }
    }


}
