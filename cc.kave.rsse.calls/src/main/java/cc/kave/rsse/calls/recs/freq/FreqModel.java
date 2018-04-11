package cc.kave.rsse.calls.recs.freq;

import java.util.Map;

import cc.kave.commons.model.naming.codeelements.IMethodName;

public class FreqModel {

	// context > method > probability
	public Map<IMethodName, Map<IMethodName, Double>> frequencies;
}