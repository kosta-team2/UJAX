package org.kostaTeam2.presentation.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.kostaTeam2.application.service.MemberService;
import org.kostaTeam2.application.service.MemberServiceImpl;
import org.kostaTeam2.domain.model.member.MemberRepository;
import org.kostaTeam2.infrastructure.dao.MemberDao;
import org.kostaTeam2.presentation.controller.api.RestController;
import org.kostaTeam2.presentation.controller.page.Controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class HandlerMappingListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext application = sce.getServletContext();

		String fileName = application.getInitParameter("fileName");
		String apiFileName = application.getInitParameter("apiFileName");

		try {
			// 1) DataSource: 톰캣 JNDI에서 lookup (톰캣이 커넥션풀 생명주기 관리)
			DataSource ds = (DataSource)new InitialContext().lookup("java:comp/env/jdbc/mySql");

			// 2) Repo/Service 싱글턴 조립
			//Repository
			MemberRepository memberRepo = new MemberDao();
			//Service
			MemberService memberSvc = new MemberServiceImpl(ds, memberRepo);

			ResourceBundle rb1 = ResourceBundle.getBundle(fileName);
			ResourceBundle rb2 = ResourceBundle.getBundle(apiFileName);

			Map<String, Controller> controllerMap = new HashMap<>();
			Map<String, RestController> apiControllerMap = new HashMap<>();

			for (String key : rb1.keySet()) {
				String value = rb1.getString(key);
				Class<?> className = Class.forName(value);

				Object con = null;
				//생성자 조사
				for (var ctor : className.getDeclaredConstructors()) {
					var pts = ctor.getParameterTypes();

					if (pts.length == 1 && pts[0] == MemberService.class) {
						ctor.setAccessible(true);
						con = ctor.newInstance(memberSvc);
						break;
					}
				}

				//없으면 기본 생성자
				if (con == null) {
					con = className.getDeclaredConstructor().newInstance();
				}

				controllerMap.put(key, (Controller)con);
			}

			for (String key : rb2.keySet()) {
				String value = rb2.getString(key);
				Class<?> className = Class.forName(value);

				Object con = null;
				//생성자 조사
				for (var ctor : className.getDeclaredConstructors()) {
					var pts = ctor.getParameterTypes();

					if (pts.length == 1 && pts[0] == MemberService.class) {
						ctor.setAccessible(true);
						con = ctor.newInstance(memberSvc);
						break;
					}
				}

				//없으면 기본 생성자
				if (con == null) {
					con = className.getDeclaredConstructor().newInstance();
				}

				apiControllerMap.put(key, (RestController)con);
			}

			application.setAttribute("controllerMap", controllerMap);
			application.setAttribute("apiControllerMap", apiControllerMap);
			application.setAttribute("path", application.getContextPath());

		} catch (Exception e) {
			throw new RuntimeException("HandlerMapping 초기화 실패", e);
		}

	}
}
