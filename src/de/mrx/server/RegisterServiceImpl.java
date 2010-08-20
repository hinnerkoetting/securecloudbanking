package de.mrx.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.jdo.PersistenceManager;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.mrx.client.RegisterService;
import de.mrx.client.SCBIdentityDTO;
import de.mrx.shared.SCBException;

public class RegisterServiceImpl extends RemoteServiceServlet implements
		RegisterService {

	private final Logger log = Logger.getLogger(RegisterServiceImpl.class
			.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = -1791669577170197531L;

	public void register(SCBIdentityDTO identity) throws SCBException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			SCBIdentity id = new SCBIdentity(identity);
			// At the moment, directly activate the user
			id.setActivated(true);
			Properties props = new Properties();

			Session session = Session.getDefaultInstance(props, null);

			Multipart outboundMultipart = new MimeMultipart();

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart
					.setText(
							"Congratulations. You have activated your account at Secure Cloud Banking",
							"text/html");
			outboundMultipart.addBodyPart(messageBodyPart);

			outboundMultipart.addBodyPart(createRegistrationAttachment());
			
			

			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(
					"activation@securecloudbanking.appspotmail.com",
					"support@securecloudbanking.appspotmail.com"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(id
					.getEmail(), id.getName()));
			msg.setSubject("Your Example.com account has been activated");
			msg
					.setText("Congratulations. You have activated your account at Secure Cloud Banking");
			msg.setContent(outboundMultipart);
			Transport.send(msg);

			pm.makePersistent(id);
			log.info("Registration received: " + id);

		}
		// catch (MessagingException e){
		// log.severe(e.getMessage());
		// e.printStackTrace();
		// throw new SCBException("Registration currently not possible1",e);
		// }
		//		
		// catch (UnsupportedEncodingException e){
		// log.severe(e.getMessage());
		// e.printStackTrace();
		// throw new SCBException("Registration currently not possible2",e);
		// }
		//		
		// catch (DocumentException e){
		// log.severe(e.getMessage());
		// e.printStackTrace();
		// throw new SCBException("Einladung konnte nicht verschickt werden!",
		// e);
		// }

		catch (Exception e) {
			log.severe(e.getMessage());
			e.printStackTrace();
			throw new SCBException(
					"Registrierung konnte nicht durchgeführt werden!", e);
		} finally {
			pm.close();
		}

	}

	private MimeBodyPart createRegistrationAttachment()
			throws DocumentException, MessagingException, IOException {
		Document document = new Document();
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, byteOut);

		document.open();
		Paragraph titel=new Paragraph("Registration At Secure Cloud Banking");
		titel.getFont().setStyle(Font.BOLD);
		document.add(titel);
		document.add(new Paragraph("By using services of SCB I accept following regulations:"));
		document.add(new Paragraph("1. SCB only offers a playground for security analysis"));
		document.add(new Paragraph("2. SCB doesn't offer real bank services"));
		document.close();
		byte[] invitationAttachment = byteOut.toByteArray();

		MimeBodyPart mimeAttachment = new MimeBodyPart();
		mimeAttachment.setFileName("invitation.pdf");
		ByteArrayDataSource mimePartDataSource = new ByteArrayDataSource(
				new ByteArrayInputStream(invitationAttachment), "application/pdf");
		mimeAttachment.setDataHandler(new DataHandler(mimePartDataSource));

		return mimeAttachment;

	}
	
	
	

	public void activate(String accountNr, String invitationCode)
			throws SCBException {
		// TODO Auto-generated method stub
		
	}
}
