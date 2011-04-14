package pithreads.framework;

public class ValidFlag {
	public volatile boolean valid;  // volatile => invalidation broadcast
	
	public ValidFlag() {
		valid = true;
	}
	
	@Override
	public boolean equals(Object o) {
		return o == this; // referential equality
	}
	
	@Override
	public String toString() {
		if(valid)
			return "valid";
		else
			return "invalid";
	}
}
