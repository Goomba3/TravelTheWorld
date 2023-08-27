package interfaces;

import java.util.List;

public interface IAccessorRepository<T> extends IBaseRepository<T> {
	List<T> get();
	T get(int id);

}
