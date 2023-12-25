package java12.services.impl;

import java12.dao.DepartmentDao;
import java12.exception.NotFoundException;
import java12.models.Department;
import java12.services.DepartmentService;
import java12.services.GenericService;

import java.util.List;

public class DepartmentServiceImpl implements GenericService<Department>, DepartmentService {

    private final DepartmentDao dao;

    public DepartmentServiceImpl(DepartmentDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Department> getAllDepartmentByHospital(Long id) {
        try {
           return dao.getAll(id);
        } catch (NotFoundException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Department findDepartmentByName(String name) {
        try {
           return dao.findDepartmentByName(name);
        } catch (NotFoundException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String add(Long hospitalId, Department department) {
        try {
            dao.add(hospitalId, department);
            return "Successfully added";
        } catch (NotFoundException | IllegalArgumentException e){
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
    public String updateById(Long id, Department department) {
        try {
           return dao.update(id, department);
        } catch (NotFoundException e){
            return e.getMessage();
        }
    }
}
