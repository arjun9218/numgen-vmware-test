package com.vmwaretest.numgen.model;

public class NumGenAttributes {
	public String Goal;
	public String Step;
	
	public String getGoal() {
		return Goal;
	}

	public void setGoal(String goal) {
		Goal = goal;
	}

	public String getStep() {
		return Step;
	}

	public void setStep(String step) {
		Step = step;
	}

	@Override
	public String toString() {
		return "NumGenAttributes [Goal=" + Goal + ", Step=" + Step + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Goal == null) ? 0 : Goal.hashCode());
		result = prime * result + ((Step == null) ? 0 : Step.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NumGenAttributes other = (NumGenAttributes) obj;
		if (Goal == null) {
			if (other.Goal != null)
				return false;
		} else if (!Goal.equals(other.Goal))
			return false;
		if (Step == null) {
			if (other.Step != null)
				return false;
		} else if (!Step.equals(other.Step))
			return false;
		return true;
	}
	
}
