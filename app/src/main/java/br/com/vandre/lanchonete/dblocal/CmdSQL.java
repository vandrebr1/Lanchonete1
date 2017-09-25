package br.com.vandre.lanchonete.dblocal;

import java.util.List;

public interface CmdSQL {
	
	List<String> create();
	List<String> upgrade(int oldVersion, int newVersion);

}
