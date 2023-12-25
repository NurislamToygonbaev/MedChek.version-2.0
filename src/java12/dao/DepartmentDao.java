package java12.dao;

import java12.models.Department;

import java.util.List;

public interface DepartmentDao {

    Boolean add(Long hospitalId, Department department);

    String remove(Long id);

    List<Department> getAll(Long hospitalId);

    List<Department> getAllDepartments();

    String update(Long id, Department department);

    Department findDepartmentByName(String name);
}
