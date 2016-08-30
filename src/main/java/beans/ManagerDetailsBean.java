package beans;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.tn.cinema.entities.Manager;
import com.tn.cinema.entities.MovieTheater;
import com.tn.cinema.entities.User;
import com.tn.cinema.entities._3DMovieTheater;
import com.tn.cinema.services.ManagerServiceLocal;
import com.tn.cinema.services.UsersFacadeLocal;

@ManagedBean(name = "managerDetails", eager = true)
@SessionScoped
public class ManagerDetailsBean {

	private StreamedContent imageContent;
	private int imageHeight;
	private int imageWidth;
	private String oldPassword;
	private String newPassword;

	private MovieTheater identifiedManagerTheater;
	private boolean _3DTheater;
	private String projectionType;

	@EJB
	private UsersFacadeLocal usersFacadeLocal;

	@EJB
	private ManagerServiceLocal managerServiceLocal;

	@ManagedProperty("#{identity}")
	private IdentityBean identityBean;

	public ManagerDetailsBean() {

	}

	@PostConstruct
	public void init() {
		doLoadImage();
		identifiedManagerTheater = ((Manager) identityBean.getIdentifiedUser()).getMovieTheaters().get(0);
		if (identifiedManagerTheater instanceof _3DMovieTheater) {
			_3DTheater = true;
			projectionType=((_3DMovieTheater)identifiedManagerTheater).getProjectionType();
		}
	}

	private void doLoadImage() {
		imageContent = new DefaultStreamedContent(
				new ByteArrayInputStream(identityBean.getIdentifiedUser().getImage()));

		InputStream in = new ByteArrayInputStream(identityBean.getIdentifiedUser().getImage());

		BufferedImage buf = null;
		try {
			buf = ImageIO.read(in);
		} catch (IOException e) {
		}
		imageHeight = buf.getHeight();
		imageWidth = buf.getWidth();
	}

	public StreamedContent getUserImage() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {
			return new DefaultStreamedContent(new ByteArrayInputStream(identityBean.getIdentifiedUser().getImage()));
		}
	}

	public void updatePassword() {

		User found = usersFacadeLocal.authenticate(identityBean.getIdentifiedUser().getEmail(), oldPassword);

		if (found != null && found instanceof Manager) {
			// update
			found.setPassword(newPassword);
			if (managerServiceLocal.updateManager((Manager) found)) {

				// reload curr user
				identityBean.setIdentifiedUser(
						usersFacadeLocal.authenticate(identityBean.getIdentifiedUser().getEmail(), newPassword));
				newPassword = "";
				oldPassword = "";

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Password updated with success", null));
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Oops.. somthing went wrong !", null));
			}

		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong password, try again ", null));
		}
	}

	public StreamedContent getImageContent() {
		return imageContent;
	}

	public void setImageContent(StreamedContent imageContent) {
		this.imageContent = imageContent;
	}

	public IdentityBean getIdentityBean() {
		return identityBean;
	}

	public void setIdentityBean(IdentityBean identityBean) {
		this.identityBean = identityBean;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public MovieTheater getIdentifiedManagerTheater() {
		return identifiedManagerTheater;
	}

	public void setIdentifiedManagerTheater(MovieTheater identifiedManagerTheater) {
		this.identifiedManagerTheater = identifiedManagerTheater;
	}

	public boolean is_3DTheater() {
		return _3DTheater;
	}

	public void set_3DTheater(boolean _3dTheater) {
		_3DTheater = _3dTheater;
	}

	public String getProjectionType() {
		return projectionType;
	}

	public void setProjectionType(String projectionType) {
		this.projectionType = projectionType;
	}
	
}
