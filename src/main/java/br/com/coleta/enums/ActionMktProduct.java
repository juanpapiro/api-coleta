package br.com.coleta.enums;

public enum ActionMktProduct {
	
	FIND("Busca"),
	INSERT("Inclusão"),
	UPDATE("Atualização");
	
	private String descAction;
	
	ActionMktProduct(String descAction) {
		this.descAction = descAction;
	}
	
	public String getDescAction() {
		return descAction;
	}

}
