package interfaces;

public interface IMutatorRepository<T> extends IAccessorRepository<T> {
	int add(T entity);
	boolean remove(int id);
	boolean update(T entity);
	int upsert(T entity);
}
