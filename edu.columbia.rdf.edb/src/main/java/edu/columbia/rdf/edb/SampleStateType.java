package edu.columbia.rdf.edb;

import org.abh.common.bioinformatics.annotation.Entity;

public class SampleStateType extends Entity implements Comparable<SampleStateType> {
	protected SampleState mType;

	public SampleStateType(int id, SampleState type) {
		super(id);
		
		mType = type;
	}
	
	public SampleState getType() {
		return mType;
	}
	
	@Override
	public String toString() {
		return mType.toString();
	}

	@Override
	public int compareTo(SampleStateType t) {
		return mType.compareTo(t.mType);
	}
	
	@Override
	public int hashCode() {
		return mType.hashCode();
	}
}
