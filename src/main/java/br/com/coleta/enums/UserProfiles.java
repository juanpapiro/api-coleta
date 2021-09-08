package br.com.coleta.enums;

public enum UserProfiles {
	
	USER(1, "User"),
	STATION(2, "Station"),
	ADMIN(3, "Admin");
	
	public Integer idProfile;
	public String descProfile;
	
	UserProfiles(Integer idProfile, String descProfile) {
		this.idProfile = idProfile;
		this.descProfile = descProfile;
	}

	public Integer getIdProfile() {
		return idProfile;
	}

	public String getDescProfile() {
		return descProfile;
	}

}
