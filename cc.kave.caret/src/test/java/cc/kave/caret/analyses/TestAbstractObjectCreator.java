package cc.kave.caret.analyses;

import java.util.LinkedList;
import java.util.List;

import cc.kave.commons.assertions.Asserts;

public class TestAbstractObjectCreator implements IAbstractObjectCreator {

	private List<Object> aos = new LinkedList<>();

	@Override
	public Object next() {
		Object o = new Object();
		aos.add(o);
		return o;
	}

	public Object get(int i) {
		Asserts.assertTrue(aos.size() > i);
		return aos.get(i);
	}
}