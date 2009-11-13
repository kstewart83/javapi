package pithreads.framework.utils;

public class Pair<F, S> {
	private final F first;
	private final S second;
	
	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}
	
	public F getFirst() {
		return first;
	}
	
	public S getSecond() {
		return second;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o==null) return false;
		if(o==this) return true;
		if(o.getClass()!=this.getClass())
			return false;
		Pair<?,?> po = (Pair<?,?>) o;
		return po.getFirst().equals(first) && po.getSecond().equals(second);
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("(");
		buf.append(first);
		buf.append(",");
		buf.append(second);
		buf.append(")");
		return buf.toString();
	}
}
