package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import com.tn.cinema.entities.Manager;
import com.tn.cinema.entities.Movie;
import com.tn.cinema.entities.Session;
import com.tn.cinema.entities.SessionID;
import com.tn.cinema.services.MovieServiceLocal;
import com.tn.cinema.services.SessionServiceLocal;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class SessionBean implements Serializable {

	private Session session;

	private List<Session> sessions;

	private List<Movie> movies;
	private List<Movie> filteredMovies;

	@ManagedProperty("#{identity}")
	private IdentityBean identityBean;

	@EJB
	private SessionServiceLocal sessionServiceLocal;

	@EJB
	private MovieServiceLocal movieServiceLocal;

	private Date currentDate = new Date();

	private Date date;

	private Movie selectedMovie;

	private boolean movieSelected;

	@PostConstruct
	public void init() {
		session = new Session();
		sessions = new ArrayList<Session>();
		// sessions will get all exist sessions for connected user fmi his movie
		// theater
		sessions = sessionServiceLocal
				.findAllSessionsByMovieTheater(((Manager) identityBean.getIdentifiedUser()).getMovieTheaters().get(0));

		movies = movieServiceLocal.findAllMovies();
	}

	// extract hour form given day
	public Long getHour() {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		return Long.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
	}

	public String createNewSession() {
		// setting up the session id
		SessionID sid = new SessionID();
		sid.setDate(date);
		sid.setStartTime(getHour());
		sid.setTheaterID(((Manager) identityBean.getIdentifiedUser()).getMovieTheaters().get(0).getId());
		sid.setMovieID(selectedMovie.getId());
		session.setId(sid);

		// setting up the session
		session.setMovie(selectedMovie);
		session.setMovieTheater(((Manager) identityBean.getIdentifiedUser()).getMovieTheaters().get(0));
		session.setNbrSpectators(0L);
		session = new Session(0L, selectedMovie, ((Manager) identityBean.getIdentifiedUser()).getMovieTheaters().get(0),
				getHour(), date);
		if (sessions.contains(session)) {
			FacesMessage msg = new FacesMessage("Dublicated", "This session has already been added");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} else {
			// call to ejb for insertion
			if(date.after(currentDate)){
				if (sessionServiceLocal.addSession(session)) {
					FacesMessage msg = new FacesMessage("Success ",
							"The session has been added successfully");
					FacesContext.getCurrentInstance().addMessage(null, msg);
					sessions.add(session);
				} else {
					FacesMessage msg = new FacesMessage("Oops .. !", "Something went wrong try later");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			}else{
				FacesMessage msg = new FacesMessage("Invalide date !", "Why don't you buy a time machine before posting your session");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
			
			

			session = new Session();
		}

		return null;
	}

	public String deleteSession(Session session) {
		if (sessionServiceLocal.deleteSession(session)) {
			FacesMessage msg = new FacesMessage("Success ",
					"The session has been deleted successfully");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			sessions.remove(session);
			System.out.println(sessions.size());
		} else {
			FacesMessage msg = new FacesMessage("Oops .. !", "Something went wrong try later");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		return null;
	}
	
	
	public void onRowSelect(SelectEvent event) {
		movieSelected = true;
	}

	public void onRowUnselect(UnselectEvent event) {
		movieSelected = false;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	public List<Movie> getMovies() {
		return movies;
	}

	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}

	public List<Movie> getFilteredMovies() {
		return filteredMovies;
	}

	public void setFilteredMovies(List<Movie> filteredMovies) {
		this.filteredMovies = filteredMovies;
	}

	public void setIdentityBean(IdentityBean identityBean) {
		this.identityBean = identityBean;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getCurrentDate() {
		// am makin' sure calendar minimum +2h
		return new Date(currentDate.getTime() + TimeUnit.HOURS.toMillis(2));
	}

	public Movie getSelectedMovie() {
		return selectedMovie;
	}

	public void setSelectedMovie(Movie selectedMovie) {
		this.selectedMovie = selectedMovie;
	}

	public boolean isMovieSelected() {
		return movieSelected;
	}

	public void setMovieSelected(boolean movieSelected) {
		this.movieSelected = movieSelected;
	}


}
