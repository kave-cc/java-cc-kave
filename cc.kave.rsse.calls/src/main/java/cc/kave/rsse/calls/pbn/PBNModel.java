package cc.kave.rsse.calls.pbn;

import cc.kave.commons.model.naming.codeelements.IMethodName;

public class PBNModel {

	public int numPatterns;
	public double[] patternProbabilities;

	// [ctxIdx, pattern] == true
	public IMethodName[] contextNodes;
	public double[][] contextProbabilities;

	// [methodIdx, pattern] = probability for true (false = 1-p(true))
	public IMethodName[] callNodes;
	public double[][] callProbabilityTrue;
	
	public void validateModel() {
		
	}
}