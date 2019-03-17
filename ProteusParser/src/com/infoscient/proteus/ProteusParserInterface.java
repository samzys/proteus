package com.infoscient.proteus;

import com.infoscient.proteus.modelica.ClassDef;

public interface ProteusParserInterface {
	
	/**
	 * @param template
	 * @return
	 */
	public ClassDef[] paserMO(String template);

}
