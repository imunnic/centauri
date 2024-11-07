package es.mde.kiron.servicios;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  private final JavaMailSender mailSender;
  private final Logger logger = LoggerFactory.getLogger(EmailService.class);
  @Value("${spring.mail.username}")
  String from;


  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void recuperarPassword(String email, String password) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(email);
      helper.setFrom(from);
      helper.setSubject("Recuperación de contraseña Centauri");
      helper.setText("La constraseña de recuperación que puede utilizar es la siguiente:\n" +
          password);
      mailSender.send(message);
      logger.info("Correo electrónico de recuperación de contraseña enviado");
    } catch (Exception e) {
      logger.error("Error en el envío del correo electrónico desde " +
          from + ":\n" + e.toString());
      throw new IllegalStateException("Error al enviar el correo electrónico", e);
    }
  }
}
