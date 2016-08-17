package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.IdentityBean;

@WebFilter("/login.jsf")
public class ConnectedUserZoneSecurityFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		IdentityBean identity = (IdentityBean) req.getSession().getAttribute("identity");

		//if not connected
		String status = "n";

		
		// if a connected manager
		if (identity != null && identity.getIdentifiedUser() != null && identity.hasRole("Manager")) {
			status = "m";
		}

		
		if (status.equalsIgnoreCase("m")) {
			resp.sendRedirect(req.getContextPath() + "/manager/home.jsf");
		}

	}

	@Override
	public void destroy() {
	}

}
