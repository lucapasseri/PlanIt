package com.example.luca.planit;

public class InviteToGroupWrapper {
	private final String username;
	private final String email;
	private final String nameGroup;
	private final String groupId;
	
	/**
	 * 
	 * @param username
	 * @param email
	 * @param nameGroup
	 * @param groupId
	 */
	private InviteToGroupWrapper(String username, String email, String nameGroup, String groupId) {
		super();
		this.username = username;
		this.email = email;
		this.nameGroup = nameGroup;
		this.groupId = groupId;
	}

    public static InviteToGroupWrapper getGroupWrapperInstanceByUsername(String username,String nameGroup, String groupId) {
        return new InviteToGroupWrapper(username,"",nameGroup,groupId);
    }

    public static InviteToGroupWrapper getGroupWrapperInstanceByEmail(String email, String nameGroup, String groupId) {
        return new InviteToGroupWrapper("",email,nameGroup,groupId);
    }

    public boolean isUsernameGroupWrapper(){
        return this.email.isEmpty();
    }
    public boolean isMailGroupWrapper(){
        return this.username.isEmpty();
    }
	
	public String getUsername() {
		if(this.isMailGroupWrapper()){
			throw new IllegalStateException();
		}
		return username;
	}


	public String getEmail() {
		if(this.isUsernameGroupWrapper()){
			throw new IllegalStateException();
		}
		return email;
	}



	public String getNameGroup() {
		return nameGroup;
	}

	public String getGroupId() {
		return groupId;
	}

		
	
}
