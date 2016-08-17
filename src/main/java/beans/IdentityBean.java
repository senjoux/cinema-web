package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.tn.cinema.entities.Administrator;
import com.tn.cinema.entities.Manager;
import com.tn.cinema.entities.User;

@ManagedBean(name="identity")
@SessionScoped
public class IdentityBean {


	private User identifiedUser;
	
	public IdentityBean(){}
	
	/*
	@PostConstruct
	public void cc(){
		identifiedUser=new Manager();
	}
	 */

	public User getIdentifiedUser() {
		return identifiedUser;
	}

	public void setIdentifiedUser(User identifiedUser) {
		this.identifiedUser = identifiedUser;
	}
	
	public Boolean hasRole(String role) {
		Boolean response = false;
		switch (role) {
		case "Manager":
			response = identifiedUser instanceof Manager;
			break;
		case "Admin":
			response = identifiedUser instanceof Administrator;
			break;
		}
		return response;
	}
}
