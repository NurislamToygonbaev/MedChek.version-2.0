package java12.dao.impl;

import java12.MyGeneratorId;
import java12.dao.DepartmentDao;
import java12.database.DataBase;
import java12.exception.NotFoundException;
import java12.models.Department;
import java12.models.Hospital;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentDaoImpl implements DepartmentDao {

    private final DataBase dataBase;

    public DepartmentDaoImpl(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public Boolean add(Long hospitalId, Department department) {
        boolean b = dataBase.getAll().stream()
                .flatMap(hospital -> hospital.getDepartments().stream())
                .anyMatch(department1 -> department1.getDepartmentName().equalsIgnoreCase(department.getDepartmentName()));
        if (b) throw new NotFoundException("Department with name: " + department.getDepartmentName() + " already have");
        Optional<Hospital> first = dataBase.getAll().stream()
                .filter(hospital -> hospital.getId().equals(hospitalId))
                .findFirst();
        if (first.isPresent()) {
            Hospital hospital = first.get();
            department.setId(MyGeneratorId.generatorDepartment());
            return hospital.getDepartments().add(department);
        } else throw new NotFoundException("Hospital with id: " + hospitalId + " not found");
    }

    @Override
    public String remove(Long id) {
        Optional<Department> first = dataBase.getAll().stream()
                .flatMap(hospital -> hospital.getDepartments().stream())
                .filter(department -> department.getId().equals(id))
                .findFirst();
        first.ifPresent(department -> {
            dataBase.getAll().forEach(hospital -> hospital.getDepartments().removeIf(d -> d.getId().equals(id)));
        });
        return first.map(department -> "Successfully deleted").orElseThrow(() ->
                new NotFoundException("Department with id: " + id + " not found"));

    }

    @Override
    public List<Department> getAll(Long hospitalId) {
        Optional<Hospital> first = dataBase.getAll().stream()
                .filter(hospital -> hospital.getId().equals(hospitalId))
                .findFirst();
        return first.map(Hospital::getDepartments)
                .orElseThrow(() -> new NotFoundException("Hospital with id: " + hospitalId + " not found"));
    }

    @Override
    public List<Department> getAllDepartments() {
        List<Department> allDepartments = new ArrayList<>();
        for (Hospital hospital : dataBase.getAll()) allDepartments.addAll(hospital.getDepartments());
        if (!allDepartments.isEmpty()) return allDepartments;
        else throw new IllegalArgumentException("No departments found in any hospital");
    }

    @Override
    public String update(Long id, Department department) {
        Optional<Department> first = getAllDepartments().stream()
                .filter(department1 -> department1.getId().equals(id))
                .findFirst();
        if (first.isPresent()) {
            Department hospital = first.get();
            hospital.setDepartmentName(department.getDepartmentName());
            return "Successfully updated";
        } else throw new NotFoundException("Department with id: " + id + " not found!");
    }

    @Override
    public Department findDepartmentByName(String name) {
        return getAllDepartments().stream()
                .filter(department -> department.getDepartmentName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Department with name: " + name + " not found"));
    }
}
