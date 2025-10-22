package org.kostaTeam2.presentation.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.kostaTeam2.application.service.MemberService;
import org.kostaTeam2.application.service.MemberServiceImpl;
import org.kostaTeam2.application.service.ProblemService;
import org.kostaTeam2.application.service.ProblemServiceImpl;
import org.kostaTeam2.application.service.workspace.WorkspaceProblemService;
import org.kostaTeam2.application.service.workspace.WorkspaceProblemServiceImpl;
import org.kostaTeam2.application.service.workspace.WorkspaceService;
import org.kostaTeam2.application.service.workspace.WorkspaceServiceImpl;
import org.kostaTeam2.domain.member.MemberRepository;
import org.kostaTeam2.domain.problem.ProblemRepository;
import org.kostaTeam2.domain.workspace.WorkspaceMemberRepository;
import org.kostaTeam2.domain.workspace.WorkspaceProblemRepository;
import org.kostaTeam2.domain.workspace.WorkspaceRepository;
import org.kostaTeam2.infrastructure.dao.MemberDao;
import org.kostaTeam2.infrastructure.dao.ProblemDao;
import org.kostaTeam2.infrastructure.dao.WorkspaceDao;
import org.kostaTeam2.infrastructure.dao.WorkspaceMemberDao;
import org.kostaTeam2.infrastructure.dao.WorkspaceProblemDao;
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
            DataSource ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/mySql");

            // 2) Repository & Service 조립
            MemberRepository memberRepo = new MemberDao();
            ProblemRepository problemRepo = new ProblemDao();
            WorkspaceRepository workspaceRepo = new WorkspaceDao();
            WorkspaceMemberRepository workspaceMemberRepo = new WorkspaceMemberDao();
            WorkspaceProblemRepository workspaceProblemRepo = new WorkspaceProblemDao();

            MemberService memberSvc = new MemberServiceImpl(ds, memberRepo);
            ProblemService problemSvc = new ProblemServiceImpl(ds, problemRepo);
            WorkspaceService workspaceSvc = new WorkspaceServiceImpl(ds, workspaceRepo, workspaceMemberRepo);
            WorkspaceProblemService workspaceProblemSvc = new WorkspaceProblemServiceImpl(ds, workspaceProblemRepo, problemRepo);

            // 3) properties 파일 로드
            ResourceBundle rb1 = ResourceBundle.getBundle(fileName);
            ResourceBundle rb2 = ResourceBundle.getBundle(apiFileName);

            Map<String, Controller> controllerMap = new HashMap<>();
            Map<String, RestController> apiControllerMap = new HashMap<>();

            // 4) Page Controller 매핑
            for (String key : rb1.keySet()) {
                String value = rb1.getString(key);
                Class<?> clazz = Class.forName(value);

                Object controllerInstance = null;

                for (var ctor : clazz.getDeclaredConstructors()) {
                    var pts = ctor.getParameterTypes();

                    if (pts.length == 1 && pts[0] == MemberService.class) {
                        ctor.setAccessible(true);
                        controllerInstance = ctor.newInstance(memberSvc);
                        break;
                    } else if (pts.length == 1 && pts[0] == ProblemService.class) {
                        ctor.setAccessible(true);
                        controllerInstance = ctor.newInstance(problemSvc);
                        break;
                    } else if (pts.length == 1 && pts[0] == WorkspaceService.class) {
                        ctor.setAccessible(true);
                        controllerInstance = ctor.newInstance(workspaceSvc);
                        break;
                    } else if (pts.length == 1 && pts[0] == WorkspaceProblemService.class) {
                        ctor.setAccessible(true);
                        controllerInstance = ctor.newInstance(workspaceProblemSvc);
                        break;
                    }
                }

                if (controllerInstance == null) {
                    controllerInstance = clazz.getDeclaredConstructor().newInstance();
                }

                controllerMap.put(key, (Controller) controllerInstance);
            }

            // 5) API Controller 매핑
            for (String key : rb2.keySet()) {
                String value = rb2.getString(key);
                Class<?> clazz = Class.forName(value);

                Object controllerInstance = null;

                for (var ctor : clazz.getDeclaredConstructors()) {
                    var pts = ctor.getParameterTypes();

                    if (pts.length == 1 && pts[0] == MemberService.class) {
                        ctor.setAccessible(true);
                        controllerInstance = ctor.newInstance(memberSvc);
                        break;
                    } else if (pts.length == 1 && pts[0] == ProblemService.class) {
                        ctor.setAccessible(true);
                        controllerInstance = ctor.newInstance(problemSvc);
                        break;
                    } else if (pts.length == 1 && pts[0] == WorkspaceService.class) {
                        ctor.setAccessible(true);
                        controllerInstance = ctor.newInstance(workspaceSvc);
                        break;
                    } else if (pts.length == 1 && pts[0] == WorkspaceProblemService.class) {
                        ctor.setAccessible(true);
                        controllerInstance = ctor.newInstance(workspaceProblemSvc);
                        break;
                    }
                }

                if (controllerInstance == null) {
                    controllerInstance = clazz.getDeclaredConstructor().newInstance();
                }

                apiControllerMap.put(key, (RestController) controllerInstance);
            }

            // 6) ServletContext 등록
            application.setAttribute("controllerMap", controllerMap);
            application.setAttribute("apiControllerMap", apiControllerMap);
            application.setAttribute("path", application.getContextPath());
            application.setAttribute("problemService", problemSvc);

        } catch (Exception e) {
            throw new RuntimeException("HandlerMapping 초기화 실패", e);
        }
    }
}
