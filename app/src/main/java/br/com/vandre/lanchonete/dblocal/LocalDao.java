package br.com.vandre.lanchonete.dblocal;

import java.util.List;

public interface LocalDao<T> {

	String getTableName();
	
	void insert(T... elementos);

	void update(T... elementos);

	void delete(T... elementos);
	
	T selectByCodigo(String codigo);
	
	T select(String sql, String... parametros);
	
	List<T> selectAll();
	
	List<T> selectAll(String sql, String... parametros);
	
}
