package com.svaad.requestDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LogInRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String email;
	private String uname;
	private String fbId;

	private AuthData authData;
	
	private String displayPicUrl;
	
	private String birthday;
	
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	
	public AuthData getAuthData() {
		return authData;
	}

	public void setAuthData(AuthData authData) {
		this.authData = authData;
	}

	public String getDisplayPicUrl() {
		return displayPicUrl;
	}

	public void setDisplayPicUrl(String displayPicUrl) {
		this.displayPicUrl = displayPicUrl;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}




	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}




	public static class AuthData{
		
		private FBData facebook;
		
		public static class FBData{
			private String id;
			
			private String access_token;
			
			private String expiration_date;

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getAccess_token() {
				return access_token;
			}

			public void setAccess_token(String access_token) {
				this.access_token = access_token;
			}

			public String getExpiration_date() {
				return expiration_date;
			}

			public void setExpiration_date(String expiration_date) {
				this.expiration_date = expiration_date;
			}
			
		}

		public FBData getFacebook() {
			return facebook;
		}

		public void setFacebook(FBData facebook) {
			this.facebook = facebook;
		}
	}
}
