package proteus.gwt.server.businesslogic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import proteus.gwt.server.domain.Content_type_mod;
import proteus.gwt.server.domain.Files;
import proteus.gwt.server.domain.Users;
import proteus.gwt.server.security.Cryptography;
import proteus.gwt.server.util.ServerContext;

public class ProteuswebProxy {
	private static Logger logger = Logger.getAnonymousLogger();
	private static EntityManager em;

	public static Users login(String name, String password) {
		// logger.info(String.format("Logging in: %s,%s", name, password));
		em = PersistenceManagerWeb.get().getEntityManager();
		// count number of matching users
		// login successful when there is only 1
		Query q = em
				.createQuery(
						"select users from Users users where users.name= :name and users.pass= :password")
				.setParameter("name", name).setParameter("password",
						Cryptography.getInstance().getMD5Hash(password));
		try {
			Users user = (Users) q.getSingleResult();
			return user;
		} catch (NoResultException e) {
			return null;
		}
	}

	public static void main(String[] args) {
		ProteuswebProxy uproxy = new ProteuswebProxy();
		String name = "username";
		String pass = "password";
		try {
			Users users = uproxy.login(name, pass);
			System.out.println(users.getPass());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Content_type_mod locateFileID(long mid) {
		em = PersistenceManagerWeb.get().getEntityManager();
		Query q = em
				.createQuery(
						"select content_type_mod from Content_type_mod content_type_mod where content_type_mod.field_mid_value= :mid")
				.setParameter("mid", mid);
		try {
			return (Content_type_mod) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public static Files getFileLocation(int uid, int fid) {
		em = PersistenceManagerWeb.get().getEntityManager();
		Query q = em
				.createQuery(
						"select files from Files files where files.fid= :fid and files.uid = :uid")
				.setParameter("fid", fid).setParameter("uid", uid);
		//This is for super user to edit the file.
		if(uid==7)
			q = em.createQuery("select files from Files files where files.fid =:fid").setParameter("fid", fid);
			
		try {
			return (Files) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public static File writeMOfile(BufferedReader r, String path) throws IOException {
		// Read content
		File file = new File(path);
			PrintWriter w = new PrintWriter(new FileWriter(file));
			String line;
			while ((line = r.readLine()) != null) {
				w.println(line);
			}
			w.flush();
			w.close();
		return file;
	}

	public static Users checkLogin(String name, String pwd) {
		// logger.info(String.format("Logging in: %s,%s", name, password));
		em = PersistenceManagerWeb.get().getEntityManager();
		// count number of matching users
		// login successful when there is only 1
		Query q = em.createQuery(
				"select users from Users users where users.pass= :password")
				.setParameter("password", pwd);
		try {
			List<Users> userList = (List<Users>) q.getResultList();
			for(Users u: userList) {
				String nameHash = Cryptography.getInstance().getMD5Hash(
						u.getName());
				if(name.equals(nameHash)) {
					return u;
				}
			}
			return null;
		} catch (NoResultException e) {
			return null;
		}
	}
}
