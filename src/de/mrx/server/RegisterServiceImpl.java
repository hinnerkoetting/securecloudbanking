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
import com.itextpdf.text.pdf.PdfWriter;

import de.mrx.client.RegisterService;
import de.mrx.client.SCBIdentityDTO;
import de.mrx.shared.SCBException;
import de.mrx.shared.UserAlreadyUsedException;

/**
 * implementation class for the RegisterService
 * @see de.client.RegisterService
 */
public class RegisterServiceImpl extends RemoteServiceServlet implements
		RegisterService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8998459064091585454L;
	private final Logger log = Logger.getLogger(RegisterServiceImpl.class
			.getName());


	/**
	 * ask for registration. Later the activiation should be either confirmed by an SCB-Employee or by an activation link
	 * During the registration, an email is written to the new customer
	 */
	public void register(SCBIdentityDTO identity) throws SCBException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			SCBIdentity existingIDentity=SCBIdentity.getByEmail(pm,identity.getEmail());
			if (existingIDentity!=null){
				throw new UserAlreadyUsedException();
			}
			
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
			pm.currentTransaction().begin();
			pm.makePersistent(id);
			log.info("Registration received: " + id);
			
			pm.currentTransaction().commit();
			

		}
		catch (SCBException e){
			log.warning("Problem Registering: "+e.getMessage());
			throw e;
		}
		catch (Exception e) {
			
			e.printStackTrace();
			throw new SCBException("Registrierung konnte nicht durchgeführt werden!", e);
		} finally {
			if (pm.currentTransaction().isActive()) {
				pm.currentTransaction().rollback();
			}
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
