package java12.dao.impl;

import java12.MyGeneratorId;
import java12.dao.PatientDao;
import java12.database.DataBase;
import java12.exception.NotFoundException;
import java12.models.Hospital;
import java12.models.Patient;

import java.util.*;
import java.util.stream.Collectors;

public class PatientDaoImpl implements PatientDao {

    private final DataBase dataBase;

    public PatientDaoImpl(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public Patient findById(Long patientId) {
        return getAll().stream()
                .filter(patient -> patient.getId().equals(patientId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Patient with id: " + patientId + " not found"));
    }

    @Override
    public Boolean add(Long hospitalId, Patient patient) {
        patient.setId(MyGeneratorId.generatorPatient());
        Optional<Hospital> first = dataBase.getAll().stream()
                .filter(hospital -> hospital.getId().equals(hospitalId))
                .findFirst();
        return first.map(hospital -> hospital.getPatients().add(patient))
                .orElseThrow(() -> new NotFoundException("Hospital with id: " + hospitalId + " not found"));
    }

    @Override
    public String delete(Long id) {
        List<Patient> patients = getAll();
        Optional<Patient> first = patients.stream()
                .filter(patient -> patient.getId().equals(id))
                .findFirst();
        if (first.isPresent()) {
            patients.remove(first.get());
            return "Successfully deleted";
        } else throw new NotFoundException("Patient with id: " + id + " not found");
    }

    @Override
    public List<Patient> getAll() {
        List<Patient> patients = new ArrayList<>();
        for (Hospital hospital : dataBase.getAll()) {
            patients.addAll(hospital.getPatients());
        }
        if (!patients.isEmpty()) return patients;
        else throw new NotFoundException("No patients found in any hospital");
    }

    @Override
    public String updateById(Long id, Patient patient) {
        Optional<Patient> first = getAll().stream()
                .filter(patient1 -> patient1.getId().equals(id))
                .findFirst();
        if (first.isPresent()) {
            Patient patient1 = first.get();
            patient1.setFirstName(patient.getFirstName());
            patient1.setLastName(patient.getLastName());
            patient1.setAge(patient.getAge());
            patient1.setGender(patient.getGender());
            return "Successfully updated";
        } else throw new NotFoundException("Patient with id: " + id + " not found");
    }

    @Override
    public String addPatientsToHospital(Long id, List<Patient> patients) {
        patients.forEach(patient -> patient.setId(MyGeneratorId.generatorPatient()));
        Optional<Hospital> hospitalOptional = dataBase.getAll().stream()
                .filter(hospital -> hospital.getId().equals(id))
                .findFirst();
        hospitalOptional.ifPresent(hospital -> hospital.getPatients().addAll(patients));
        if (hospitalOptional.isPresent()) return "Successfully added Patients";
        else throw new NotFoundException("Hospital with id: " + id + " not found");
    }

    @Override
    public Map<Integer, List<Patient>> getPatientByAge() {
        return dataBase.getAll().stream()
                .flatMap(hospital -> hospital.getPatients().stream())
                .collect(Collectors.groupingBy(Patient::getAge));
    }

    @Override
    public List<Patient> sortPatientsByAge(String ascOrDesc) {
        List<Patient> patients = dataBase.getAll().stream()
                .flatMap(hospital -> hospital.getPatients().stream())
                .toList();
        Comparator<Patient> comparator;
        if (ascOrDesc.equalsIgnoreCase("asc")) {
            comparator = Comparator.comparing(Patient::getAge);
        } else if (ascOrDesc.equalsIgnoreCase("desc")) {
            comparator = Comparator.comparing(Patient::getAge).reversed();
        } else throw new NotFoundException("Enter only asc or desc. You wrote: " + ascOrDesc);
        return patients.stream().sorted(comparator).toList();
    }
}
