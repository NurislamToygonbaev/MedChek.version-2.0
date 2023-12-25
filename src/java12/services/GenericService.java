package java12.services;

public interface GenericService<T> {

    String add(Long  hospitalId, T t);

    String removeById(Long id);

    String updateById(Long id, T t);
}
