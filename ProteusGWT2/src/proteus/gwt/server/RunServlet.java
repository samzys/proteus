package proteus.gwt.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import proteus.gwt.server.modelica.omc.ConnectException;
import proteus.gwt.server.modelica.omc.OMCProxy;
import proteus.gwt.shared.types.Constants;

public class RunServlet extends HttpServlet implements Constants {

	private static Map<String, RunInfo> runMap = Collections
			.synchronizedMap(new HashMap<String, RunInfo>());

	private File workDir;

	private RunQueue runQueue;

	public void init() throws ServletException {
		Enumeration<String> paramNames = getServletConfig()
				.getInitParameterNames();
		System.out.println("param names");
		while (paramNames.hasMoreElements()) {
			System.out.println(paramNames.nextElement());
		}
		String wdpath = getServletConfig().getInitParameter("WorkDir");
		System.out.println(wdpath);
		if (wdpath == null) {
			throw new ServletException("Work directory not specified!");
		}
		workDir = new File(wdpath);
		if (!workDir.exists()) {
			workDir.mkdirs();
		}

		OMCProxy omcProxy = new OMCProxy();
		// Comment out to prevent loading library
		// *
		try { // Load library
			omcProxy.getStandardLibrary();
			// Change to workDir
			omcProxy.sendExpression("cd(\""
					+ workDir.getAbsolutePath().replace("\\", "/") + "\")");
		} catch (ConnectException e) {
			throw new ServletException(e);
		}
		// */

		runQueue = new RunQueue(omcProxy, workDir);
		new Thread(runQueue).start();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String type = req.getParameter("type");
		if (type == null) {
			sendError(resp);
			return;
		}
		if (type.equals("status")) {
			String id = req.getParameter("id");
			sendStatusCode(resp, id);
		} else if (type.equals("result")) {
			String id = req.getParameter("id");
			sendResult(resp, id);
		}
	}

	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BufferedReader r = new BufferedReader(req.getReader());
		String line, prefix;

		// Read header
		line = r.readLine();
		prefix = HEADER_PREFIX_MODEL;
		if (line == null || !line.startsWith(prefix)) {
			sendError(resp);
			return;
		}
		String model = line.substring(prefix.length()).trim();
		line = r.readLine();
		prefix = HEADER_PREFIX_START_TIME;
		if (line == null || !line.startsWith(prefix)) {
			sendError(resp);
			return;
		}
		double startTime;
		try {
			startTime = Double.parseDouble(line.substring(prefix.length())
					.trim());
		} catch (NumberFormatException e) {
			sendError(resp);
			return;
		}

		line = r.readLine();
		prefix = HEADER_PREFIX_STOP_TIME;
		if (line == null || !line.startsWith(prefix)) {
			sendError(resp);
			return;
		}
		double stopTime;
		try {
			stopTime = Double.parseDouble(line.substring(prefix.length())
					.trim());
		} catch (NumberFormatException e) {
			sendError(resp);
			return;
		}

		RunParam runParam = new RunParam(model, startTime, stopTime);

		// Read content
		String id = randomAlphaNum(5);

		File tempFile = new File(workDir + "/" + id + ".mo");
		PrintWriter w = new PrintWriter(new FileWriter(tempFile));
		while ((line = r.readLine()) != null) {
			w.println(line);
		}
		w.flush();
		w.close();
		RunInfo runInfo = new RunInfo(id, tempFile, runParam);
		runQueue.add(runInfo);
		runMap.put(id, runInfo);
		w = resp.getWriter();
		w.println(id);
	}

	private void sendOk(HttpServletResponse resp) throws IOException {
		PrintWriter pw = resp.getWriter();
		pw.print(RESP_OK);
		pw.flush();
	}

	private void sendError(HttpServletResponse resp) throws IOException {
		PrintWriter pw = resp.getWriter();
		pw.print(RESP_ERROR);
		pw.flush();
	}

	private void sendStatusCode(HttpServletResponse resp, String id)
			throws IOException {
		RunInfo runInfo = null;
		if (id == null || (runInfo = runMap.get(id)) == null) {
			sendError(resp);
			return;
		}
		PrintWriter pw = resp.getWriter();
		pw.print(runInfo.getStatusCode());
		pw.flush();
	}

	private void sendResult(HttpServletResponse resp, String id)
			throws IOException {
		RunInfo runInfo = null;
		if (id == null || (runInfo = runMap.get(id)) == null) {
			sendError(resp);
			return;
		}
		if (runInfo.statusCode != RunInfo.STATUS_DONE) {
			sendError(resp);
			return;
		}
		PrintWriter pw = resp.getWriter();
		BufferedReader r = new BufferedReader(new FileReader(runInfo.plotFile));
		String line;
		while ((line = r.readLine()) != null) {
			pw.println(line);
		}
		pw.flush();
	}

	private static String randomAlphaNum(int length) {
		byte[] buf = new byte[length];
		for (int i = 0; i < buf.length; i++) {
			int n = (int) (Math.random() * 62);
			if (n < 10) {
				buf[i] = (byte) (0x30 + n);
			} else if (n < 36) {
				n -= 10;
				buf[i] = (byte) (0x41 + n);
			} else {
				n -= 36;
				buf[i] = (byte) (0x61 + n);
			}
		}
		return new String(buf);
	}

	public static class RunInfo implements Constants {

		public final String id;

		public final File file;

		public final RunParam runParam;

		public File plotFile;

		public int statusCode;

		public RunInfo(String id, File file, RunParam runParam) {
			this.id = id;
			this.file = file;
			this.runParam = runParam;
		}

		public int getStatusCode() {
			return statusCode;
		}
	}

	public static class RunParam {
		public final String model;

		public final double startTime;

		public final double stopTime;

		public RunParam(String model, double startTime, double stopTime) {
			this.model = model;
			this.startTime = startTime;
			this.stopTime = stopTime;
		}
	}

	public static class RunQueue implements Runnable {
		private List<RunInfo> queue = new LinkedList<RunInfo>();

		private RunInfo current;

		private boolean stop;

		private File workDir;

		private OMCProxy omcProxy;

		public RunQueue(OMCProxy proxy, File workDir) {
			this.omcProxy = proxy;
			this.workDir = workDir;
		}

		public synchronized void add(RunInfo runInfo) {
			queue.add(runInfo);
		}

		public synchronized void cancel(RunInfo runInfo) {
			if (runInfo == current) {
				// Cancel running task
			}
			queue.remove(runInfo);
		}

		private void log(String message) {
			System.out.println(message);
		}

		public void run() {
			try {
				RUN_LOOP: while (true) {
					current = null;
					while (queue.isEmpty()) {
						Thread.sleep(1000);
					}
					current = queue.remove(0);
					log("Running: " + current.id);
					File file = current.file;
					String model = current.runParam.model;
					// Oct 20, sam Run Simx remotely
					// Run task
					if (model.indexOf(".MultiBody") != -1) {
						// simx compiler
						File plotFile = new File(workDir + "/" + model
								+ "_res.plt");
						try {
							String[] cmd = new String[6];
							cmd[0] = "wscript.exe";
							cmd[1] = "../webapps/proteus/simx.vbs";
							cmd[2] = "//I";
							cmd[3] = model;
							cmd[4] = file.getAbsolutePath().replace("\\", "/");
							cmd[5] = plotFile.getAbsolutePath().replace("\\", "/");
							Process proc = Runtime.getRuntime().exec(cmd);
							// proc.waitFor();
							while (!stop) {
								try {
									proc.exitValue();
								} catch (IllegalThreadStateException e) { // not
									// finished
									Thread.sleep(500);
									continue;
								}
								break; // break if finished
							}
							if (stop) {
								proc.destroy();
							}
						} catch (Exception e) {
							StringBuilder errorStr = new StringBuilder();
							errorStr.append("\nERROR:\n");
							errorStr.append(e.getMessage() + "\n");
							log(errorStr.toString());
							current.statusCode = RunInfo.STATUS_ERROR;
							return;
						}
						if (stop) {
							log("Simulation Stopped.");
							return;
						}
						String result = "";
						log("=================\n");
						log("Compiler message:\n");
						log("=================\n");
						log(result + "compiler message is not supported yet\n");
						if (stop) {
							log("Simulation Stopped.");
							current.statusCode = RunInfo.STATUS_CANCELED;
							return;
						}
						System.out.println("WorkDir = " + workDir);
						System.out.println("Plot file: " + plotFile);
						
						if (!plotFile.exists()) {
							log("Can't find plot: " + model
									+ ", won't be able to plot\n");
							return;
						}
						current.plotFile = plotFile;
						current.statusCode = RunInfo.STATUS_DONE;
					} else {
						// omc compiler
						try {
							log("current dir = "
									+ omcProxy.sendExpression("cd()"));

							double startTime = current.runParam.startTime;
							double stopTime = current.runParam.stopTime;
							// copy(file, workDir);
							if (stop) {
								log("Simulation stopped");
								current.statusCode = RunInfo.STATUS_CANCELED;
								continue RUN_LOOP;
							}

							log("Loading source file...");
							omcProxy.loadSourceFile(file.getAbsolutePath()
									.replace("\\", "/"));
							log(" Done\n");
							if (stop) {
								log("Simulation Stopped.");
								current.statusCode = RunInfo.STATUS_CANCELED;
								continue RUN_LOOP;
							}
							log("Simulating model (" + model + ")...");
							String result = omcProxy.sendExpression("simulate("
									+ model + ", startTime=" + startTime
									+ ", stopTime=" + stopTime + ")");
							log(" Done\n");
							if (stop) {
								log("Simulation Stopped.");
								current.statusCode = RunInfo.STATUS_CANCELED;
								continue RUN_LOOP;
							}
							// Guess if an error occurred, ought to be a better
							// way :(
							if (result.contains("Error occured")) { // [sic]
								log("ERROR:\n");
								log(result + "\n");
								current.statusCode = RunInfo.STATUS_ERROR;
								continue RUN_LOOP;
							}
							log("=================\n");
							log("Compiler message:\n");
							log("=================\n");
							log(result + "\n");
							if (stop) {
								log("Simulation Stopped.");
								current.statusCode = RunInfo.STATUS_CANCELED;
								continue RUN_LOOP;
							}
							File f = new File(workDir + "/" + model
									+ "_res.plt");
							if (!f.exists()) {
								log("Can't find plot: " + model
										+ ", won't be able to plot\n");
								current.statusCode = RunInfo.STATUS_ERROR;
								continue RUN_LOOP;
							}
							current.plotFile = f;
							current.statusCode = RunInfo.STATUS_DONE;
						} catch (Exception e) {
							current.statusCode = RunInfo.STATUS_ERROR;
						}
					}// end of checking model
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private void SimXRun() {

		}

		private void copy(File file, File todir) throws IOException {
			if (!todir.isDirectory()) {
				throw new IOException(todir + " must be a directory");
			}
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				File d = new File(todir + "/" + file.getName());
				d.mkdir();
				for (File f : files) {
					copy(f, d);
				}
			} else {
				FileInputStream in = new FileInputStream(file);
				FileOutputStream out = new FileOutputStream(todir
						.getAbsolutePath()
						+ "/" + file.getName());
				byte[] buf = new byte[4096];
				int n;
				while ((n = in.read(buf)) > 0) {
					out.write(buf, 0, n);
				}
				out.flush();
			}
		}
	}
}
