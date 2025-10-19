package org.kostaTeam2.presentation.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		//session 유지 기간: 30분
		event.getSession().setMaxInactiveInterval(1800);
	}
}
