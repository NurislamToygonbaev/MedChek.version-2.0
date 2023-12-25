package java12.services;

import java12.models.Department;

import java.util.List;

public interface DepartmentService {

    List<Department> getAllDepartmentByHospital(Long id);

    Department findDepartmentByName(String name);
}
