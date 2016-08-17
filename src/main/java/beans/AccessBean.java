package beans;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.tn.cinema.entities.Manager;
import com.tn.cinema.entities.User;
import com.tn.cinema.services.UsersFacadeLocal;

@ManagedBean(name = "access")
@RequestScoped
public class AccessBean {

	@EJB
	private UsersFacadeLocal usersFacadeLocal;

	@ManagedProperty("#{identity}")
	private IdentityBean identityBean;

	private String email;
	private String password;

	public AccessBean() {
	}

	public String getEmail() {
		return email;
	}

	public String doLogin() {
		String navigateTo = null;
		User found = usersFacadeLocal.authenticate(email, password);

		if (found != null && found instanceof Manager) {
			if (!found.isLocked()) {
					
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, " • Your account is locked for the time beeing, we'll contact you soon •", null));
			} else {
				// set du identified user
				identityBean.setIdentifiedUser(found);
				navigateTo = "/manager/home?faces-redirect=true";
			}

		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong credits", null));
		}

		return navigateTo;
	}

	public String doLogout() {
		String navigateTo = null;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
		navigateTo = "/home?faces-redirect=true";
		return navigateTo;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public IdentityBean getIdentityBean() {
		return identityBean;
	}

	public void setIdentityBean(IdentityBean identityBean) {
		this.identityBean = identityBean;
	}

}
