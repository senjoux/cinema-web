package beans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.apache.commons.lang3.math.NumberUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.tn.cinema.entities.Session;
import com.tn.cinema.services.MovieServiceLocal;
import com.tn.cinema.services.SessionServiceLocal;


@ManagedBean
@SessionScoped
public class PublicSessionsBean {

	private List<Session> sessions;

	@EJB
	private SessionServiceLocal sessionServiceLocal;

	@EJB
	private MovieServiceLocal movieServiceLocal;

	@PostConstruct
	public void init() {
		sessions = new ArrayList<Session>();
		sessions = sessionServiceLocal.findAllSessions();
	}

	public StreamedContent getMovieCover() throws IOException {
	
		FacesContext context = FacesContext.getCurrentInstance();

	    if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
	        return new DefaultStreamedContent();
	    } else {
	        String id = context.getExternalContext().getRequestParameterMap().get("movieId");
	        byte[] bytes =  NumberUtils.isNumber(id) ? movieServiceLocal.findMovieByID(Integer.valueOf(id)).getCover(): null;
	        return bytes == null ? null : new DefaultStreamedContent(new ByteArrayInputStream(bytes));
	    }
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

}
