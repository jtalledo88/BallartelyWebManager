package pe.com.foxsoft.ballartelyweb.spring.util;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

public class SessionTimeoutListener implements PhaseListener {
	
	private static final long serialVersionUID = 4053548447925271572L;

	@Override
	public void afterPhase(PhaseEvent arg0) {

	}

	@Override
	public void beforePhase(final PhaseEvent event) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
//        if (!facesContext.getPartialViewContext().isAjaxRequest() || facesContext.getRenderResponse()) { // not ajax or too late
//            return;
//        }
 
        final HttpServletRequest request = HttpServletRequest.class.cast(facesContext.getExternalContext().getRequest());
        if (!(getLoginPath().equals(request.getServletPath()) && !getIndexPath().equals(request.getServletPath())) && 
        		(request.getSession(false) == null || request.getSession(false).getAttribute(Constantes.SESSION_USUARIO_ATTR) == null)) { // isLoginRedirection()
            final String redirect = facesContext.getExternalContext().getRequestContextPath() + getIndexPath();
            try {
                facesContext.getExternalContext().redirect(redirect);
            } catch (final Exception e) {
                System.err.println(e.getMessage());
            }
        }
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}
	
	private String getLoginPath() {
        return "/paginas/inicio/login.xhtml";
    }
	
	private String getIndexPath() {
        return "/index.jsp";
    }

}
