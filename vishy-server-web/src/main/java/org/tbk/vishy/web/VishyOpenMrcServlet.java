package org.tbk.vishy.web;


import com.github.theborakompanioni.openmrc.web.OpenMrcHttpRequestService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class VishyOpenMrcServlet extends HttpServlet {

    private final OpenMrcHttpRequestService openMrcService;

    public VishyOpenMrcServlet(OpenMrcHttpRequestService openMrcService) {
        super();
        this.openMrcService = Objects.requireNonNull(openMrcService);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "");
    }
}
