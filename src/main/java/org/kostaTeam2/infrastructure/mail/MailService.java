package org.kostaTeam2.infrastructure.mail;

import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import jakarta.servlet.ServletContext;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class MailService {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String from; // 지정 없으면 username 사용

    public MailService(ServletContext ctx) {
        // 1) ENV 먼저(배포 환경에서는 OS에 환경 변수 세팅)
        String envHost = getenv("SMTP_HOST");
        String envPort = getenv("SMTP_PORT");
        String envUser = getenv("SMTP_USERNAME");
        String envPass = getenv("SMTP_PASSWORD");
        String envFrom = getenv("SMTP_FROM");

        // 2) web.xml의 context param으로 부터 읽어오기
        this.host     = coalesce(envHost, ctxParam(ctx, "smtp.host"), "smtp.naver.com");
        this.port     = parseInt(coalesce(envPort, ctxParam(ctx, "smtp.port"), "587"), 587);
        this.username = required(coalesce(envUser, ctxParam(ctx, "smtp.username")), "smtp.username / SMTP_USERNAME");
        this.password = required(coalesce(envPass, ctxParam(ctx, "smtp.password")), "smtp.password / SMTP_PASSWORD");
        this.from     = coalesce(envFrom, ctxParam(ctx, "smtp.from"), this.username);
    }

    /**
     * 메일 이걸로 보내면 됨
     * @param to (수신자 메일주소)
     * @param subject (제목)
     * @param body (메일 본문)
     */
    public void sendMail(String to, String subject, String body) {
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", String.valueOf(port));
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");   // TLS 587
            props.put("mail.smtp.ssl.enable", "false");
            props.put("mail.smtp.ssl.trust", host);
            // props.put("mail.debug", "true"); // 필요 시 주석 해제

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            MimeMessage message = new MimeMessage(session);

            // From
            message.setFrom(new InternetAddress(from));

            // To
            Address[] tos = InternetAddress.parse(to, false);
            message.setRecipients(Message.RecipientType.TO, tos);

            // 제목 인코딩
            String encSubject = MimeUtility.encodeText(subject, StandardCharsets.UTF_8.name(), "B");
            message.setSubject(encSubject);

            // 본문 인코딩
            message.setContent(body, "text/plain; charset=UTF-8");

            Transport.send(message);
        } catch (MessagingException me) {
            throw new RuntimeException("메일 전송 실패: " + me.getMessage(), me);
        } catch (Exception e) {
            throw new RuntimeException("메일 구성 실패: " + e.getMessage(), e);
        }
    }

    private static String getenv(String key) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v : null;
    }

    private static String ctxParam(ServletContext ctx, String key) {
        if (ctx == null) return null;
        String v = ctx.getInitParameter(key);
        return (v != null && !v.isBlank()) ? v : null;
    }

    @SafeVarargs
    private static <T> T coalesce(T... vals) {
        if (vals == null) return null;
        for (T v : vals) if (v != null) return v;
        return null;
    }

    private static int parseInt(String s, int def) {
        try { return (s == null || s.isBlank()) ? def : Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { return def; }
    }

    private static String required(String v, String name) {
        if (v == null || v.isBlank())
            throw new IllegalStateException("필수 SMTP 설정 누락: " + name);
        return v;
    }
}
