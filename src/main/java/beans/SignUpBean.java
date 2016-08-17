package beans;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;

import com.tn.cinema.entities.Manager;
import com.tn.cinema.services.ManagerServiceLocal;

import utility.Utils;

@ManagedBean(name = "signup")
@RequestScoped
public class SignUpBean {

	public String firstName;
	private String name;
	private Long mobile_phone;
	private String email;
	private String password;
	private UploadedFile uploadedFile;

	@EJB
	private ManagerServiceLocal managerServiceLocal;

	public void doSignUp() {

		// the manager has attached an image
		if (!uploadedFile.getFileName().isEmpty()) {
			if (Utils.isValideFile(uploadedFile)) {
				Manager m = new Manager();
				m.setFirstName(firstName);
				m.setName(name);
				m.setEmail(email);
				m.setPassword(password);
				m.setMobilePhone(mobile_phone);
				m.setImage(getImageContent());

				if (managerServiceLocal.addManager(m)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Your account has ben  created successfully, " + "We'll contact you sooner  ", null));
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Oops something wrong !", null));
				}

			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalide image -_-' !", null));
			}
			// the manager has not attached an image
		} else {
			Manager m = new Manager();
			m.setFirstName(firstName);
			m.setName(name);
			m.setEmail(email);
			m.setPassword(password);
			m.setMobilePhone(mobile_phone);
			m.setImage(getImageContent());

			if (managerServiceLocal.addManager(m)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Your account has ben  created successfully, " + "We'll contact you sooner  ", null));
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Oops something wrong !", null));
			}
		}

	}

	public byte[] getImageContent() {
		return uploadedFile.getContents();
	}

	@PostConstruct
	public void getDefaultImageContent() {
		File file = new File(SignUpBean.class.getResource("/images/defaultUser.png").getFile());
		try {
			Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getMobile_phone() {
		return mobile_phone;
	}

	public void setMobile_phone(Long mobile_phone) {
		this.mobile_phone = mobile_phone;
	}

	public String getEmail() {
		return email;
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

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

}
