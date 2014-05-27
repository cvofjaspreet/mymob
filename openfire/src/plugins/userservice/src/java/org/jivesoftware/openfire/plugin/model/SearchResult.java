package org.jivesoftware.openfire.plugin.model;

import java.util.Set;

import org.jivesoftware.openfire.user.User;

/**
 * 
 * Search result model to stone user search results 
 * 
 * @author jaspreet
 *
 */

public class SearchResult {
	private Set<User> users;
	private boolean available;

	

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	

}
