package de.pbc.stata;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface Term {
	
	// PUBLIC ------------------------------------------------------- //
	
	public String getName();
	
	public String getLabel();
	
	public List<Variable> getVariables();
	
	public boolean isBase();
	
	// FACTORY ------------------------------------------------------ //
	
	public static Term create(String name) {
		return new TermImpl(name);
	}
	
	// INNER CLASSES ------------------------------------------------ //
	
	static class TermImpl implements Term {
		
		// VARIABLES ---------------------------------------------------- //
		
		private String name;
		
		private List<Variable> vars;
		
		// CONSTRUCTOR -------------------------------------------------- //
		
		public TermImpl(String name) {
			this.name = name.trim();
			vars = Arrays.stream(this.name.split("#")).map(Variable::create).collect(Collectors.toList());
		}
		
		// PUBLIC ------------------------------------------------------- //
		
		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String getLabel() {
			return vars.stream().collect(Collectors.groupingBy((e) -> e.getName())).entrySet().stream().map((e) -> {
				int power = e.getValue().size();
				if (power <= 3)
					return e.getValue().get(0).getLabel() + (power == 1 ? "" : power == 2 ? "²" : "³");
				else
					return e.getValue().stream().map((e1) -> e1.getLabel()).collect(Collectors.joining(" * "));
			}).collect(Collectors.joining(" * "));
		}
		
		@Override
		public List<Variable> getVariables() {
			return vars;
		}
		
		@Override
		public boolean isBase() {
			return vars.size() == 1 && vars.get(0).isBase();
		}
		
		@Override
		public String toString() {
			return getLabel();
		}
		
	}
	
}